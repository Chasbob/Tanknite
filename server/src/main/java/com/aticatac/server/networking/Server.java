package com.aticatac.server.networking;

import com.aticatac.common.model.Command;
import com.aticatac.common.model.CommandModel;
import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.Shutdown;
import com.aticatac.server.gamemanager.Manager;
import com.aticatac.server.networking.listen.NewClients;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;

/**
 * The type Server.
 *
 * @author Charles de Freitas
 */
public class Server extends Thread {
  private final Logger logger;
  private final ExecutorService executorService;
  private volatile boolean shutdown;

  /**
   * Instantiates a new Server.
   */
  public Server() {
    //TODO check if additional users are allowed.
    this.logger = Logger.getLogger(getClass());
    executorService = Executors.newFixedThreadPool(20);
    this.shutdown = false;
  }

  @Override
  public void run() {
    this.logger.trace("Running...");
    try {
      this.executorService.submit(new Discovery());
      this.logger.trace("added discovery");
      this.executorService.submit(new NewClients());
      this.logger.trace("added new clients");
      this.executorService.submit(new Updater());
      this.logger.trace("added updater");
    } catch (IOException e) {
      this.logger.error(e);
      return;
    }
    new Thread(() -> {
      while (!this.shutdown) {
        CommandModel current = ServerData.INSTANCE.popCommand();
        if (current != null) {
          if (current.getCommand() == Command.QUIT) {
            ServerData.INSTANCE.removeClient(current.getId());
            this.logger.info("Removing " + current.getId());
            this.logger.info("Clients: " + ServerData.INSTANCE.clients.size());
          } else {
            Manager.INSTANCE.playerInput(current);
          }
        }
      }
      this.executorService.shutdown();
      ServerData.INSTANCE.shutdown();
    }).start();
  }

  /**
   * Shutdown.
   */
  public void shutdown() {
    this.shutdown = true;
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
    private ServerSocket serverSocket;
    private MulticastSocket multicastSocket;
    private DatagramSocket broadcastSocket;
    private String id;
    private boolean singlePlayer;
    private InetAddress server;
    private InetAddress multicast;
    private int port;
    private int broadcastPort;

    ServerData() {
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
        this.serverSocket = new ServerSocket(this.port);
      } catch (IOException e) {
        this.logger.error(e);
      }
    }

    public void removeClient(String id) {
      clients.get(id).shutdown();
      clients.remove(id);
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
        byte[] b = ModelReader.toBytes(new Shutdown());
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
      this.singlePlayer = singlePlayer;
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
