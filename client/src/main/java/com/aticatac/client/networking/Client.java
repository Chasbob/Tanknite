package com.aticatac.client.networking;

import com.aticatac.common.Stoppable;
import com.aticatac.common.model.Command;
import com.aticatac.common.model.CommandModel;
import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.Login;
import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.ServerInformation;
import com.aticatac.common.model.Updates.Response;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.model.Vector;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * The type Client.
 */
public class Client implements Stoppable {
  private final Logger logger;
  private final ConcurrentLinkedQueue<Update> queue;
  private final ModelReader modelReader;
  private final ArrayList<String> players;
  private final HashSet<Command> currentCommands;
  private String id;
  private PrintStream printer;
  private BufferedReader reader;
  private boolean connected;
  private UpdateListener updateListener;
  private CommandModel commandModel;

  /**
   * Instantiates a new Client.
   */
  public Client() {
    this.logger = Logger.getLogger(getClass());
    this.logger.setLevel(Level.ALL);
    this.queue = new ConcurrentLinkedQueue<>();
    this.modelReader = new ModelReader();
    this.players = new ArrayList<>();
    this.currentCommands = new HashSet<>();
    this.commandModel = new CommandModel("");
    this.logger.setLevel(Level.ALL);
  }

  /**
   * Next update update.
   *
   * @return the update
   */
  public Update nextUpdate() {
    return this.queue.poll();
  }

  /**
   * Is started boolean.
   *
   * @return the boolean
   */
  public boolean isStarted() {
    if (this.queue.peek() != null) {
      return this.queue.peek().isStart();
    } else {
      return false;
    }
  }

  /**
   * Gets players.
   *
   * @return the players
   */
  public ArrayList<String> getPlayers() {
    if (this.queue.peek() != null) {
      this.players.clear();
      assert this.queue.peek() != null;
      this.players.addAll(this.queue.peek().getPlayerNames());
    }
    return this.players;
  }

  /**
   * Peek update update.
   *
   * @return the update
   */
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
    this.logger.trace("Connecting to: " + server);
    try {
      this.connected = false;
      Login login = new Login(id);
      this.logger.trace("ID: " + id);
      this.logger.trace("login: " + modelReader.toJson(login));
      this.logger.trace("Trying to conneentct to: " + server.getAddress() + ":" + server.getPort());
      Socket socket = new Socket();
      socket.setSoTimeout(1000);
      socket.connect(new InetSocketAddress(server.getAddress(), server.getPort()));
      this.logger.trace("Connected to server at " + socket.getInetAddress());
      reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.printer = new PrintStream(socket.getOutputStream());
      this.printer.println(modelReader.toJson(login));
      String json = reader.readLine();
      this.logger.trace("Waiting for Response...");
      Login output = modelReader.fromJson(json, Login.class);
      this.logger.trace("Authenticated = " + output.isAuthenticated());
      if (output.isAuthenticated() == Response.ACCEPTED) {
        this.logger.trace("Multicast address: " + output.getMulticast());
        initUpdateSocket(InetAddress.getByName(output.getMulticast()), server.getPort());
      } else {
        return output.isAuthenticated();
      }
      this.id = output.getId();
      this.logger.trace("Exiting 'connect' cleanly.");
      this.connected = true;
      this.commandModel.setId(this.id);
      return Response.ACCEPTED;
    } catch (InvalidBytes e) {
      this.logger.warn("Invalid Response");
      return Response.INVALID_RESPONSE;
    } catch (UnknownHostException e) {
      this.logger.error("unknown host");
      return Response.UNKNOWN_HOST;
    } catch (IOException e) {
      this.logger.error("IO error");
      return Response.NO_SERVER;
    }
  }

  private void initUpdateSocket(InetAddress address, int port) {
    this.logger.trace("Initialising update socket...");
    this.logger.trace("Joining multicast: " + address + ":" + port);
    updateListener = new UpdateListener(queue, this.reader);
    updateListener.start();
    this.logger.trace("Started update listener!");
  }

  /**
   * Quit.
   */
  public void quit() {
    this.logger.warn("Quitting...");
    addCommand(Command.QUIT);
    updateListener.shutdown();
    printer.close();
  }

  /**
   * Add command.
   *
   * @param command the command
   */
  public void addCommand(Command command) {
    this.logger.info(command);
    if (command.isMovement()) {
      currentCommands.add(command);
      commandModel.setCommand(Command.MOVE);
    } else if (!command.isShoot()) {
      commandModel.setCommand(command);
      submit(0);
    } else {
      commandModel.setCommand(command);
    }
  }

  /**
   * Submit.
   *
   * @param bearing the bearing
   */
  public void submit(final int bearing) {
    if (!this.connected) {
      return;
    }
    Vector v = Vector.Zero.cpy();
    commandModel.setBearing(bearing);
    for (Command c :
        currentCommands) {
      v.add(c.vector);
    }
    commandModel.setVector(v);
    if (commandModel.getCommand() == Command.DEFAULT && currentCommands.size() > 0) {
      commandModel.setCommand(Command.MOVE);
    }
    String json = modelReader.toJson(commandModel);
    this.printer.println(json);
    currentCommands.clear();
    commandModel.reset();
  }

  /**
   * Connect response.
   *
   * @param host the host
   * @param port the port
   * @param id   the id
   * @return the response
   */
  public Response connect(final String host, final int port, final String id) {
    try {
      return connect(new ServerInformation(id, InetAddress.getByName(host), port), id);
    } catch (UnknownHostException e) {
      return Response.UNKNOWN_HOST;
    }
  }

  @Override
  public void shutdown() {
    queue.clear();
    players.clear();
    currentCommands.clear();
    printer.close();
    try {
      reader.close();
    } catch (IOException ignored) {
    }
    updateListener.shutdown();

  }

}
