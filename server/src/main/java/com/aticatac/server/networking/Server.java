package com.aticatac.server.networking;

import com.aticatac.common.model.Command;
import com.aticatac.common.model.CommandModel;
import com.aticatac.common.model.ModelReader;
import com.aticatac.server.bus.EventBusFactory;
import com.aticatac.server.game.Survival;
import com.aticatac.server.networking.listen.NewClients;
import com.aticatac.server.objectsystem.DataServer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;

/**
 * The type Server.
 *
 * @author Charles de Freitas
 */
public class Server extends Thread {
  private final Logger logger;
  private final String id;
  private final boolean singleplayer;
  private volatile boolean shutdown;
  private String host;
  private Updater updater;
  private Discovery discovery;
  private NewClients newClients;

  /**
   * Instantiates a new Server.
   *
   * @param singleplayer the singleplayer
   * @param id           the id
   */
  public Server(boolean singleplayer, String id) {
    this.logger = Logger.getLogger(getClass());
    this.logger.info("Constructing server...");
    this.singleplayer = singleplayer;
    //TODO check if additional users are allowed.
    this.shutdown = false;
    this.id = id;
    ServerData.INSTANCE.initialise("225.4.5.6", 5500, 5000, id);
  }

  public Server(boolean singleplayer, String id, String host) {
    this(singleplayer, id);
    ServerData.INSTANCE.getGame().addPlayer("admin");
    this.host = host;
  }

  @Override
  public void run() {
    this.logger.trace("Running...");
    while (ServerData.INSTANCE.playerCount() == 0) {
      this.logger.info("Waiting for host");
      new NewHost().run();
      if (ServerData.INSTANCE.getClients().size() == 1) {
        this.host = ServerData.INSTANCE.getClients().keys().nextElement();
        this.logger.info("User: " + host + " is hosting!");
      } else {
        this.logger.warn("Failed to get user to host.");
      }
    }
    if (!singleplayer) {
      ServerData.INSTANCE.rebind("0.0.0.0");
      this.logger.trace("Setting up multi player");
      try {
        this.logger.info("Single player: " + singleplayer);
        this.discovery = new Discovery(this.id);
        new Thread(discovery).start();
        this.logger.trace("added discovery");
        newClients = new NewClients();
        new Thread(newClients).start();
        this.logger.trace("added new clients");
        updater = new Updater();
        new Thread(updater).start();
        this.logger.trace("added updater");
      } catch (IOException e) {
        this.logger.error(e);
        return;
      }
      while (!this.shutdown && !ServerData.INSTANCE.isStart()) {
        CommandModel current = ServerData.INSTANCE.popCommand();
        if (current != null) {
          if (current.getCommand() == Command.QUIT) {
            if (current.getId().equals(this.host)) {
              shutdown();
            } else {
              disconnectClient(current);
            }
          } else if (current.getCommand() == Command.FILL_AI && current.getId().equals(this.host)) {
            ServerData.INSTANCE.fillAI();
          } else if (current.getCommand() == Command.START && current.getId().equals(this.host)) {
            ServerData.INSTANCE.setStart(true);
            double nanoTime = System.nanoTime();
            while (System.nanoTime() - nanoTime < 3000000000d) {
              try {
                Thread.sleep(0);
              } catch (InterruptedException e) {
                this.logger.error(e);
              }
            }
          }
        }
      }
    } else {
      updater = new Updater();
      new Thread(updater).start();
      this.logger.trace("added updater");
    }
    ServerData.INSTANCE.clearRequests();
    ServerData.INSTANCE.startGame();
    this.logger.info("GO GO GO!");
//    this.ai.start();
    while (!this.shutdown) {
      CommandModel current = ServerData.INSTANCE.popCommand();
      if (current != null) {
        this.logger.trace(current);
        if (current.getCommand() == Command.QUIT) {
          disconnectClient(current);
        } else {
          onClientInput(current);
//          ServerData.INSTANCE.getGame().playerInput(current);
        }
      }
    }
    this.logger.info("Server ended.");
  }

  public void onClientInput(CommandModel model) {
    EventBusFactory.getEventBus().post(model);
  }

  private void disconnectClient(CommandModel model) {
    ServerData.INSTANCE.removeClient(model.getId());
    this.logger.info("Removing " + model.getId());
    this.logger.info("Clients: " + ServerData.INSTANCE.clients.size());
    // this probably wont be needed in practice
    if (ServerData.INSTANCE.getClients().size() == 0) {
      shutdown();
    }
  }

  /**
   * Shutdown.
   */
  public void shutdown() {
    this.shutdown = true;
    if (!singleplayer) {
      discovery.shutdown();
      newClients.shutdown();
    }
    updater.shutdown();
    ServerData.INSTANCE.shutdown();
  }

  /**
   * The enum Server data.
   */
  public enum ServerData {
    /**
     * Instance server data.
     */
    INSTANCE;
    private final ConcurrentLinkedQueue<CommandModel> requests;
    private final ConcurrentHashMap<String, Client> clients;
    private final Logger logger;
    private final ModelReader modelReader;
    private ServerSocket serverSocket;
    private MulticastSocket multicastSocket;
    private DatagramSocket broadcastSocket;
    private String id;
    private boolean singlePlayer;
    private InetAddress server;
    private InetAddress multicast;
    private int port;
    private int broadcastPort;
    private Survival game;
    private boolean start;
    private int maxPlayers;
    private int broadcastCount;

    ServerData() {
      this.modelReader = new ModelReader();
      this.requests = new ConcurrentLinkedQueue<>();
      this.clients = new ConcurrentHashMap<>();
      this.logger = Logger.getLogger(ServerData.class);
    }

    public void initialise(String multicast, int broadcastPort, int port, String name) {
      try {
        this.multicast = InetAddress.getByName(multicast);
      } catch (UnknownHostException e) {
        this.logger.info(e);
        throw new ExceptionInInitializerError(e);
      }
      this.broadcastPort = broadcastPort;
      this.port = port;
      this.maxPlayers = 10;
      this.id = name;
      this.game = new Survival();
      try {
        this.server = InetAddress.getLocalHost();
        this.multicastSocket = new MulticastSocket();
        this.broadcastSocket = new DatagramSocket();
        this.serverSocket = new ServerSocket(this.port, 5, InetAddress.getByName("127.0.0.1"));
      } catch (IOException e) {
        this.logger.error(e);
      }
      DataServer.INSTANCE.getPlayerCount();
      this.logger.info("Done!");
    }

    void startGame() {
      new Thread(game).start();
    }

    public int playerCount() {
      return this.game.playerCount();
    }

    public int getMaxPlayers() {
      return maxPlayers;
    }

    public void clearRequests() {
      this.requests.clear();
    }

    public boolean refreshBroadcast() {
      if (broadcastCount != game.playerCount()) {
        broadcastCount = game.playerCount();
        return true;
      } else {
        return true;
      }
    }

    /**
     * Is start boolean.
     *
     * @return the boolean
     */
    public boolean isStart() {
      return start;
    }

    /**
     * Sets start.
     *
     * @param start the start
     */
    public void setStart(boolean start) {
      this.start = start;
    }

    /**
     * Rebind.
     *
     * @param address the address
     */
    public void rebind(String address) {
      try {
        this.serverSocket.close();
        this.serverSocket = new ServerSocket(this.port, 5, InetAddress.getByName(address));
//        this.serverSocket.bind(new InetSocketAddress(InetAddress.getByName(address), this.port));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    /**
     * Remove client.
     *
     * @param id the id
     */
    public void removeClient(String id) {
      clients.get(id).shutdown();
      clients.remove(id);
    }

    /**
     * Gets game.
     *
     * @return the game
     */
    public Survival getGame() {
      return game;
    }

    /**
     * Sets game.
     *
     * @param game the game
     */
    public void setGame(Survival game) {
      this.game = game;
    }

    /**
     * Broadcast packet.
     *
     * @param packet the packet
     *
     * @throws IOException the io exception
     */
    public void broadcastPacket(DatagramPacket packet) throws IOException {
      this.broadcastSocket.send(packet);
    }

    /**
     * Multicast packet.
     *
     * @param packet the packet
     *
     * @throws IOException the io exception
     */
    public void multicastPacket(DatagramPacket packet) throws IOException {
      this.multicastSocket.send(packet);
    }

    /**
     * Shutdown.
     */
    void shutdown() {
      for (Client client : clients.values()
      ) {
        client.shutdown();
      }
      try {
        this.serverSocket.close();
        this.multicastSocket.close();
        this.broadcastSocket.close();
      } catch (IOException e) {
        this.logger.error(e);
      }
    }

    /**
     * Gets server socket.
     *
     * @return the server socket
     */
    public ServerSocket getServerSocket() {
      return serverSocket;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
      return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
      this.id = id;
    }
    /**
     * Is single player boolean.
     *
     * @return the boolean
     */
//    public boolean isSinglePlayer() {
//      return singlePlayer;
//    }
    /**
     * Sets single player.
     *
     * @param singlePlayer the single player
     */
//    public void setSinglePlayer(boolean singlePlayer) {
//      if (this.singlePlayer != singlePlayer) {
//        this.singlePlayer = singlePlayer;
//        try {
//          if (singlePlayer) {
//            this.serverSocket.close();
//            this.serverSocket = new ServerSocket();
//            this.serverSocket.bind(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), this.port));
//          } else {
//            this.serverSocket.close();
//            this.serverSocket = new ServerSocket();
//            this.serverSocket.bind(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), this.port));
//          }
//        } catch (IOException e) {
//          this.logger.error(e);
//        }
//      }
//    }

    /**
     * Put command.
     *
     * @param commandModel the command model
     */
    public void putCommand(CommandModel commandModel) {
      this.requests.add(commandModel);
    }

    /**
     * Add client.
     *
     * @param client the client
     */
    public void addClient(Client client) {
      this.clients.put(client.getId(), client);
    }

    /**
     * Gets next request.
     *
     * @return the next request
     */
    public CommandModel popCommand() {
      return this.requests.poll();
    }

    /**
     * Gets clients.
     *
     * @return the clients
     */
    public ConcurrentHashMap<String, Client> getClients() {
      return clients;
    }

    /**
     * Gets server.
     *
     * @return the server
     */
    public InetAddress getServer() {
      return server;
    }

    /**
     * Gets multicast.
     *
     * @return the multicast
     */
    public InetAddress getMulticast() {
      return multicast;
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort() {
      return port;
    }

    /**
     * Gets broadcast port.
     *
     * @return the broadcast port
     */
    public int getBroadcastPort() {
      return broadcastPort;
    }

    public void fillAI() {
      game.nAddAI(maxPlayers - game.playerCount());
    }
  }
}
