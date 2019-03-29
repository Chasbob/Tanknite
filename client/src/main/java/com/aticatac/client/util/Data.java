package com.aticatac.client.util;

import com.aticatac.client.networking.Client;
import com.aticatac.client.networking.Servers;
import com.aticatac.client.server.networking.Server;
import com.aticatac.common.model.Command;
import com.aticatac.common.model.DBResponse;
import com.aticatac.common.model.DBlogin;
import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.ServerInformation;
import com.aticatac.common.model.Shutdown;
import com.aticatac.common.model.Updates.Response;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.objectsystem.containers.Container;
import com.badlogic.gdx.graphics.Color;
import com.google.common.eventbus.Subscribe;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;

import static com.aticatac.client.bus.EventBusFactory.eventBus;

/**
 * The enum Data.
 */
public enum Data {
  /**
   * Instance test.
   */
  INSTANCE;
  private final Logger logger;
  /**
   * The Model reader.
   */
  ModelReader modelReader;
  private Update update;
  private ArrayList<String> clients;
  private HashMap<String, Container> players;
  private ServerInformation localhost;
  private ServerInformation currentInformation;
  private boolean singleplayer;
  private Client client;
  private String username;
  private Color tankColour;
  private boolean won;
  private Container playerPos;
  private ArrayList<Container> playerList;
  private boolean serverSelected;
  private boolean manualConfigForServer;
  private boolean isHosting;
  private boolean isIso;
  private Socket dbSocket;
  private BufferedReader reader;
  private PrintStream printer;
  private boolean dbConnected;
  private boolean connectedToGame;

  Data() {
    eventBus.register(this);
    this.logger = Logger.getLogger(getClass());
    client = new Client();
    modelReader = new ModelReader();
    try {
      dbSocket = new Socket();
      dbSocket.connect(new InetSocketAddress("192.168.1.2", 6000), 5000);
      dbConnected = true;
    } catch (IOException e) {
      this.logger.info("IO on db");
      dbConnected = false;
    }
    serverSelected = false;
    manualConfigForServer = false;
    isHosting = false;
    this.tankColour = Color.CORAL;
    try {
      //TODO don't hard code the port.
      final Server.ServerData s = Server.ServerData.INSTANCE;
      this.localhost = new ServerInformation("localhost", InetAddress.getByName("127.0.0.1"), s.getPort());
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    players = new HashMap<>();
    this.update = new Update();
    this.clients = new ArrayList<>();
  }

  /**
   * Is connected to game boolean.
   *
   * @return the boolean
   */
  public boolean isConnectedToGame() {
    return connectedToGame;
  }

  /**
   * Connected to db boolean.
   *
   * @return the boolean
   */
  public boolean connectedToDB() {
    try {
      return this.dbSocket.getInetAddress().isReachable(1000);
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * Is started boolean.
   *
   * @return the boolean
   */
  public boolean isStarted() {
    return client.isStarted();
  }

  @Subscribe
  private void shutdownEvent(Shutdown e) {
    connectedToGame = false;
  }

  /**
   * Login db response.
   *
   * @param dBlogin the d blogin
   * @return the db response
   * @throws IOException  the io exception
   * @throws InvalidBytes the invalid bytes
   */
  public DBResponse login(DBlogin dBlogin) throws IOException, InvalidBytes {
    this.logger.info(dBlogin);
    String json = modelReader.toJson(dBlogin);
    logger.info(json);
    reader = new BufferedReader(new InputStreamReader(dbSocket.getInputStream()));
    printer = new PrintStream(dbSocket.getOutputStream());
    printer.println(json);
    logger.info("wrote to stream");
    String json2 = reader.readLine();
    logger.info(json2);
    DBResponse output = modelReader.fromJson(json2, DBResponse.class);
    if (output.getResponse() == DBResponse.Response.accepted) {
      connectedToGame = true;
      eventBus.post(output.getPlayer());
    }
    return output;
  }

  /**
   * Is singleplayer boolean.
   *
   * @return the boolean
   */
  public boolean isSingleplayer() {
    return singleplayer;
  }

  /**
   * Sets singleplayer.
   *
   * @param singleplayer the singleplayer
   */
  public void setSingleplayer(boolean singleplayer) {
    this.singleplayer = singleplayer;
  }

  /**
   * Next update update.
   *
   * @return the update
   */
  public Update nextUpdate() {
    return client.nextUpdate();
  }

  /**
   * Peek update update.
   *
   * @return the update
   */
  public Update peekUpdate() {
    return client.peekUpdate();
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public String getID() {
    return this.client.getId();
  }

  /**
   * Gets clients.
   *
   * @return the clients
   */
  public ArrayList<String> getClients() {
    return this.client.getPlayers();
  }

  /**
   * Gets players.
   *
   * @return the players
   */
  public ArrayList<String> getPlayers() {
    return this.client.getPlayers();
  }

  /**
   * Player count int.
   *
   * @return the int
   */
  public int playerCount() {
    return players.size();
  }

  /**
   * Gets player.
   *
   * @param i the
   * @return the player
   */
  public Container getPlayer(int i) {
    return this.playerList.get(i);
  }

  /**
   * Gets player pos.
   *
   * @return the player pos
   */
  public Container getPlayerPos() {
    return playerPos;
  }
//  /**
//   * Sets update.
//   *
//   * @param update the update
//   */
//  public void setUpdate(Update update) {
//    this.update = update;
//    this.players = this.update.getNames();
//    if (playerPos != null) {
//      if (Math.abs(this.playerPos.getX() - this.players.get(client.getId()).getX()) > 1) {
//        this.logger.info("moved");
//      }
//    }
//    this.playerPos = this.players.get(client.getId());
//    this.playerList = getNames();
//  }

  /**
   * Gets me.
   *
   * @return the me
   */
  public Container getMe() {
    return this.players.get(this.client.getId());
  }

  /**
   * Gets current information.
   *
   * @return the current information
   */
  public ServerInformation getCurrentInformation() {
    return currentInformation;
  }

  /**
   * Sets current information.
   *
   * @param currentInformation the current information
   */
  public void setCurrentInformation(ServerInformation currentInformation) {
    this.currentInformation = currentInformation;
    this.serverSelected = true;
  }

  /**
   * Is server selected boolean.
   *
   * @return the boolean
   */
  public boolean isServerSelected() {
    return serverSelected;
  }

  /**
   * Connect Response.
   *
   * @param id           the id
   * @param singlePlayer the single player
   * @return the Response
   */
  public Response connect(String id, boolean singlePlayer) {
    if (singlePlayer) {
      return this.client.connect(this.localhost, id);
    } else {
      if (this.serverSelected) {
        return this.client.connect(this.currentInformation, id);
      } else {
        //this is just if the user has not selected a server instead of there being no Response.
        return Response.NO_SERVER;
      }
    }
  }

  /**
   * Connect response.
   *
   * @param id   the id
   * @param host the host
   * @return the response
   */
  public Response connect(String id, String host) {
//    this.currentInformation = new ServerInformation(host, InetAddress.getByName(host), Servers.INSTANCE.getPort(), Server.ServerData.INSTANCE.getMaxPlayers(), Server.ServerData.INSTANCE.playerCount());
    return this.client.connect(host, Servers.INSTANCE.getPort(), id);
    //this is just if the user has not selected a server instead of there being no Response.
  }

  /**
   * Quit.
   */
  public void quit() {
    if (client != null) {
      this.client.quit();
    }
    if (singleplayer) {
    }
  }

  /**
   * Send command.
   *
   * @param command the command
   */
  public void sendCommand(Command command) {
    //this.logger.info(command);
    this.client.addCommand(command);
  }
//  public void sendCommand(Command command, int bearing) {
//    this.client.addCommand(command, bearing);
//  }

  /**
   * Is manual config for server boolean.
   *
   * @return the boolean
   */
  public boolean isManualConfigForServer() {
    return manualConfigForServer;
  }

  /**
   * Sets manual config for server.
   *
   * @param manualConfigForServer the manual config for server
   */
  public void setManualConfigForServer(boolean manualConfigForServer) {
    this.manualConfigForServer = manualConfigForServer;
  }

  /**
   * Is hosting boolean.
   *
   * @return the boolean
   */
  public boolean isHosting() {
    return isHosting;
  }

  /**
   * Sets hosting.
   *
   * @param hosting the hosting
   */
  public void setHosting(boolean hosting) {
    isHosting = hosting;
  }

  /**
   * Gets username.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets username.
   *
   * @param username the username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Gets tank colour.
   *
   * @return the tank colour
   */
  public Color getTankColour() {
    return tankColour;
  }

  /**
   * Sets tank colour.
   *
   * @param tankColour the tank colour
   */
  public void setTankColour(Color tankColour) {
    this.tankColour = tankColour;
  }

  /**
   * Submit.
   *
   * @param bearing the bearing
   */
  public void submit(final int bearing) {
    this.client.submit(bearing);
  }

  /**
   * Initialise.
   */
  public void initialise() {
  }

  /**
   * Is dbConnected boolean.
   *
   * @return the boolean
   */
  public boolean isDbConnected() {
    return dbConnected;
  }

  /**
   * Is won boolean.
   *
   * @return the boolean
   */
  public boolean isWon() {
    return won;
  }

  /**
   * Sets won.
   *
   * @param won the won
   */
  public void setWon(boolean won) {
    this.won = won;
  }

  /**
   * Is iso boolean.
   *
   * @return the boolean
   */
  public boolean isIso() {
    return isIso;
  }

  /**
   * Sets iso.
   *
   * @param iso the iso
   */
  public void setIso(boolean iso) {
    isIso = iso;
  }
}
