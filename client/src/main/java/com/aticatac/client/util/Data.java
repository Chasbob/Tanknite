package com.aticatac.client.util;

import com.aticatac.client.networking.Client;
import com.aticatac.client.networking.Servers;
import com.aticatac.client.server.networking.Server;
import com.aticatac.common.model.*;
import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.Updates.Response;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.objectsystem.Container;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
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
  private Update update;
  private ArrayList<String> clients;
  private HashMap<String, Container> players;
  private ServerInformation localhost;
  private ServerInformation currentInformation;
  private boolean singleplayer;
  private Client client;
  private String username;
  private Color tankColour;
  private Container playerPos;
  private ArrayList<Container> playerList;
  private boolean serverSelected;
  private boolean manualConfigForServer;
  private boolean isHosting;
  private Socket dbSocket;
  private BufferedReader reader;
  private PrintStream printer;
  ModelReader modelReader;
  private boolean connected;

  Data() {
    eventBus.register(this);
    this.logger = Logger.getLogger(getClass());
    client = new Client();
    modelReader = new ModelReader();
    try {
      dbSocket = new Socket("chasbob.co.uk", 6000);
      connected = true;
    } catch (IOException e) {
      connected = false;
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
   * Is started boolean.
   *
   * @return the boolean
   */
  public boolean isStarted() {
    return client.isStarted();
  }

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
//    this.players = this.update.getPlayers();
//    if (playerPos != null) {
//      if (Math.abs(this.playerPos.getX() - this.players.get(client.getId()).getX()) > 1) {
//        this.logger.info("moved");
//      }
//    }
//    this.playerPos = this.players.get(client.getId());
//    this.playerList = getPlayers();
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
   * Connect Response.
   *
   * @param id           the id
   * @param singleplayer the singleplayer
   * @param host         the host
   * @return the Response
   * @throws UnknownHostException the unknown host exception
   */
  public Response connect(String id, boolean singleplayer, String host) throws UnknownHostException {
    this.currentInformation = new ServerInformation(host, InetAddress.getByName(host), Servers.INSTANCE.getPort(), Server.ServerData.INSTANCE.getMaxPlayers(), Server.ServerData.INSTANCE.playerCount());
    return connect(id, singleplayer);
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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Color getTankColour() {
    return tankColour;
  }

  public void setTankColour(Color tankColour) {
    this.tankColour = tankColour;
  }

  public void submit(final int bearing) {
    this.client.submit(bearing);
  }

  public void initialise() {

  }

  public boolean isConnected() {
    return connected;
  }}
