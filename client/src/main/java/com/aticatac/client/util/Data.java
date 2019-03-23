package com.aticatac.client.util;

import com.aticatac.client.networking.Client;
import com.aticatac.client.networking.Servers;
import com.aticatac.common.model.Command;
import com.aticatac.common.model.ServerInformation;
import com.aticatac.common.model.Updates.Response;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.objectsystem.Container;
import com.aticatac.server.networking.Server;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 * The enum Data.
 */
public enum Data {
  /**
   * Instance game.
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
  private Container playerPos;
  private ArrayList<Container> playerList;
  private boolean serverSelected;
  private boolean manualConfigForServer;
  private boolean isHosting;

  Data() {
    this.logger = Logger.getLogger(getClass());
    client = new Client();
    serverSelected = false;
    manualConfigForServer = false;
    isHosting = false;
    players = new HashMap<>();
    this.update = new Update();
    this.clients = new ArrayList<>();
  }

  public void initialise() {
    try {
      this.localhost = new ServerInformation("localhost", InetAddress.getByName("127.0.0.1"), 5000);
    } catch (UnknownHostException e) {
      this.logger.error(e);
      throw new ExceptionInInitializerError(e);
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
   *
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
   * Connect response.
   *
   * @param id           the id
   * @param singlePlayer the single player
   *
   * @return the response
   */
  public Response connect(String id, boolean singlePlayer) {
    if (singlePlayer) {
      return this.client.connect(this.localhost, id);
    } else {
      if (this.serverSelected) {
        return this.client.connect(this.currentInformation, id);
      } else {
        //this is just if the user has not selected a server instead of there being no response.
        return Response.NO_SERVER;
      }
    }
  }

  /**
   * Connect response.
   *
   * @param id           the id
   * @param singleplayer the singleplayer
   * @param host         the host
   *
   * @return the response
   *
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

  public void submit(final int bearing) {
    this.client.submit(bearing);
  }}
