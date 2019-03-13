package com.aticatac.client.networking;

import com.aticatac.common.model.Command;
import com.aticatac.common.model.CommandModel;
import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.Login;
import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.ServerInformation;
import com.aticatac.common.model.Updates.Response;
import com.aticatac.common.model.Updates.Update;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;

/**
 * The type Client.
 */
public class Client {
  private final Logger logger;
  private final ConcurrentLinkedQueue<Update> queue;
  private final ModelReader modelReader;
  private final ArrayList<String> players;
  private String id;
  private PrintStream printer;
  private BufferedReader reader;
  private boolean connected;
  private UpdateListener updateListener;

  /**
   * Instantiates a new Client.
   */
  public Client() {
    this.logger = Logger.getLogger(getClass());
    this.queue = new ConcurrentLinkedQueue<>();
    this.modelReader = new ModelReader();
    this.players = new ArrayList<>();
  }

  public Update nextUpdate() {
    return this.queue.poll();
  }

  public boolean isStarted() {
    if (this.queue.peek() != null) {
      return this.queue.peek().isStart();
    } else {
      return false;
    }
  }

  public ArrayList<String> getPlayers() {
    if (this.queue.peek() != null) {
      this.players.clear();
      this.players.addAll(this.queue.peek().getPlayers().keySet());
    }
    return this.players;
  }

  public Update peekUpdate() {
    return queue.peek();
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
   * Connect.
   *
   * @param server the server
   * @param id     the id
   * @return the boolean
   * @throws IOException  the io exception
   * @throws InvalidBytes the invalid bytes
   */
  public Response connect(ServerInformation server, String id) {
    try {
      this.connected = false;
      Login login = new Login(id);
      this.logger.trace("ID: " + id);
      this.logger.trace("login: " + modelReader.toJson(login));
      this.logger.info("Trying to connect to: " + server.getAddress() + ":" + server.getPort());
      Socket socket = new Socket(server.getAddress(), server.getPort());
      this.logger.trace("Connected to server at " + socket.getInetAddress());
      reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.printer = new PrintStream(socket.getOutputStream());
      this.printer.println(modelReader.toJson(login));
      String json = reader.readLine();
      this.logger.trace("Waiting for response...");
      Login output = modelReader.fromJson(json, Login.class);
      this.logger.trace("Authenticated = " + output.isAuthenticated());
      if (output.isAuthenticated() == Response.ACCEPTED) {
        this.logger.trace("Multicast address: " + output.getMulticast());
        initUpdateSocket(InetAddress.getByName(output.getMulticast()), server.getPort());
      } else {
        return output.isAuthenticated();
      }
      this.id = output.getId();
      this.logger.info("Exiting 'connect' cleanly.");
      this.connected = true;
      return Response.ACCEPTED;
    } catch (IOException e) {
      this.logger.warn("No Server.");
      return Response.NO_SERVER;
    } catch (InvalidBytes e) {
      this.logger.warn("Invalid response");
      return Response.INVALID_RESPONSE;
    }
  }

  private void initUpdateSocket(InetAddress address, int port) throws IOException {
    this.logger.trace("Initialising update socket...");
    this.logger.trace("Joining multicast: " + address + ":" + port);
    MulticastSocket multicastSocket = new MulticastSocket(port);
    multicastSocket.joinGroup(address);
    updateListener = new UpdateListener(multicastSocket, queue, this.reader);
    updateListener.start();
    this.logger.trace("Started update listener!");
  }

  /**
   * Quit.
   */
  public void quit() {
    this.logger.warn("Quitting...");
    sendCommand(Command.QUIT);
    updateListener.quit();
    printer.close();
  }

  /**
   * Send command.
   *
   * @param command the command
   */
  public void sendCommand(Command command) {
    if (!this.connected) {
      return;
    }
    this.logger.trace("Sending command: " + command);
    CommandModel commandModel = new CommandModel(this.id, command);
    this.logger.trace("Writing command to output stream.");
    String json = modelReader.toJson(commandModel);
    this.printer.println(json);
    this.logger.trace("Sent command: " + command);
  }
}
