package com.aticatac.server.networking;

import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.model.Command;
import com.aticatac.common.model.CommandModel;
import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.Shutdown;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.server.components.ai.RunAI;
import com.aticatac.server.networking.listen.NewClients;
import com.aticatac.server.test.Survival;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * The type Server.
 *
 * @author Charles de Freitas
 */
public class Server extends Thread {
  private final Logger logger;
  private final ExecutorService executorService;
  private final String name;
  private final boolean singleplayer;
  private volatile boolean shutdown;
  private String host;
  private boolean started;
  private Thread ai;

  /**
   * Instantiates a new Server.
   *
   * @param singleplayer the singleplayer
   * @param name         the name
   */
  public Server(boolean singleplayer, String name) {
    this.singleplayer = singleplayer;
    ServerData.INSTANCE.setId(name);
    //TODO check if additional users are allowed.
    this.logger = Logger.getLogger(getClass());
    executorService = Executors.newFixedThreadPool(20);
    this.shutdown = false;
    this.started = false;
    this.name = name;
    this.ai = new Thread(new RunAI());
  }

  @Override
  public void run() {
    this.logger.setLevel(Level.ALL);
    this.logger.trace("Running...");
    this.logger.trace("Waiting for host");
    new NewHost().run();
    if (ServerData.INSTANCE.getClients().size() == 1) {
      this.host = ServerData.INSTANCE.getClients().keys().nextElement();
      this.logger.info("User: " + host + " is hosting!");
      ServerData.INSTANCE.setSinglePlayer(singleplayer);
    } else {
      this.logger.warn("Failed to get user to host.");
      return;
    }
    try {
      this.logger.info("Single player: " + ServerData.INSTANCE.isSinglePlayer());
      if (!ServerData.INSTANCE.isSinglePlayer()) {
        this.executorService.submit(new Discovery(this.name));
        this.logger.trace("added discovery");
        this.executorService.submit(new NewClients());
        this.logger.trace("added new clients");
      }
    } catch (IOException e) {
      this.logger.error(e);
      return;
    }
    if (!singleplayer) {
      this.logger.trace("Setting up multi player");
      while (!this.shutdown && !this.started) {
        CommandModel current = ServerData.INSTANCE.popCommand();
        if (current != null) {
          if (current.getCommand() == Command.QUIT) {
            if (current.getId().equals(this.host)) {
              shutdown();
            } else {
              disconnectClient(current);
            }
          } else if (current.getCommand() == Command.START && current.getId().equals(this.host)) {
            this.started = true;
            Update update = new Update(true);
            update.setStart(true);
            new Updater().tcpBroadcast(update);
            double nanoTime = System.nanoTime();
            while (System.nanoTime() - nanoTime < 3000000000d) {
              try {
                Thread.sleep(0);
              } catch (InterruptedException e) {
                this.logger.error(e);
              }
            }
            this.executorService.submit(new Updater());
            this.logger.trace("added updater");
          }
        }
      }
    } else {
      this.executorService.submit(new Updater());
      this.logger.trace("added updater");
    }
//    new Thread(() -> {
//      while (!this.shutdown) {
//        double nanoTime = System.nanoTime();
//        Collection<GameObject> players = ServerData.INSTANCE.getGame().getRoot().findObject(ObjectType.PLAYER_CONTAINER).getChildren().values();
//        for (GameObject g : players) {
//          if (g.componentExists(AI.class)) {
//            ServerData.INSTANCE.getGame().playerInput(g.getName(), g.getComponent(AI.class).getDecision().getCommand());
//          }
//        }
//        while (System.nanoTime() - nanoTime < 1000000000 / 60) {
//          try {
//            Thread.sleep(0);
//          } catch (InterruptedException e) {
//            e.printStackTrace();
//          }
//        }
//      }
//    }).start();
//    while (!this.shutdown && !this.started) {
//      CommandModel current = ServerData.INSTANCE.popCommand();
//      if (current != null) {
//
//      }
//    }
    while (!this.shutdown) {
      CommandModel current = ServerData.INSTANCE.popCommand();
      if (current != null) {
        this.logger.trace(current.toString());
        if (current.getCommand() == Command.QUIT) {
          disconnectClient(current);
        } else {
          ServerData.INSTANCE.getGame().playerInput(current.getId(), current.getCommand());
        }
      }
    }
    this.logger.info("Server ended.");
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
  void shutdown() {
    this.shutdown = true;
    this.executorService.shutdown();
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

    ServerData() {
      try {
        this.game = new Survival();
      } catch (InvalidClassInstance | ComponentExistsException invalidClassInstance) {
        invalidClassInstance.printStackTrace();
      }
      this.modelReader = new ModelReader();
      this.requests = new ConcurrentLinkedQueue<>();
      this.clients = new ConcurrentHashMap<>();
      this.port = 5500;
      this.broadcastPort = 5000;
      this.logger = Logger.getLogger(ServerData.class);
      try {
        this.server = InetAddress.getLocalHost();
        this.multicast = InetAddress.getByName("225.4.5.6");
        this.multicastSocket = new MulticastSocket();
        this.broadcastSocket = new DatagramSocket();
        this.serverSocket = new ServerSocket(this.port, 5, InetAddress.getByName("127.0.0.1"));
      } catch (IOException e) {
        this.logger.error(e);
      }
    }

    public void rebind(String address) {
      try {
        this.serverSocket.close();
        this.serverSocket.bind(new InetSocketAddress(InetAddress.getByName(address), this.port));
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
     * @throws IOException the io exception
     */
    public void broadcastPacket(DatagramPacket packet) throws IOException {
      this.broadcastSocket.send(packet);
    }

    /**
     * Multicast packet.
     *
     * @param packet the packet
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
        byte[] b = modelReader.toBytes(new Shutdown());
        final ServerData s = ServerData.INSTANCE;
        DatagramPacket shutdown = new DatagramPacket(b, b.length, s.getServer(), s.getPort());
        this.broadcastSocket.send(shutdown);
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
    public boolean isSinglePlayer() {
      return singlePlayer;
    }

    /**
     * Sets single player.
     *
     * @param singlePlayer the single player
     */
    public void setSinglePlayer(boolean singlePlayer) {
      if (this.singlePlayer != singlePlayer) {
        this.singlePlayer = singlePlayer;
        try {
          if (singlePlayer) {
            this.serverSocket.close();
            this.serverSocket = new ServerSocket();
            this.serverSocket.bind(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), this.port));
          } else {
            this.serverSocket.close();
            this.serverSocket = new ServerSocket();
            this.serverSocket.bind(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), this.port));
          }
        } catch (IOException e) {
          this.logger.error(e);
        }
      }
    }

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
  }
}
