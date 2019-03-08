package com.aticatac.client.util;

import com.aticatac.client.networking.Client;
import com.aticatac.common.model.Command;
import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.ServerInformation;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.objectsystem.Container;
import com.aticatac.common.objectsystem.GameObject;
import java.io.IOException;
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
  private boolean updatePlayers;
  private Client client;
  private GameObject root;
  private Container playerPos;
  private ArrayList<Container> playerList;

  Data() {
    client = new Client();
    try {
      //TODO don't hard code the port.
      this.localhost = new ServerInformation("localhost", InetAddress.getLocalHost(), 5500);
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    players = new HashMap<>();
    this.update = new Update(true);
    this.clients = new ArrayList<>();
    logger = Logger.getLogger(getClass());
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
    return clients;
  }

  /**
   * Gets players.
   *
   * @return the players
   */
  public ArrayList<Container> getPlayers() {
    return new ArrayList<>(players.values());
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

  /**
   * Sets update.
   *
   * @param update the update
   */
  public void setUpdate(Update update) {
    this.update = update;
    this.players = this.update.getPlayers();
    if (playerPos != null) {
      if (Math.abs(this.playerPos.getX() - this.players.get(client.getId()).getX()) > 1) {
        this.logger.info("moved");
      }
    }
    this.playerPos = this.players.get(client.getId());
    this.playerList = getPlayers();
//    try {
//      this.root = new GameObject(this.update.getRootContainer());
//    } catch (InvalidClassInstance | ComponentExistsException e) {
//      this.logger.error(e);
//    }
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
  }

  /**
   * Connect boolean.
   *
   * @param id           the id
   * @param singleplayer the singleplayer
   * @return the boolean
   * @throws IOException  the io exception
   * @throws InvalidBytes the invalid bytes
   */
  public boolean connect(String id, boolean singleplayer) throws IOException, InvalidBytes {
    if (singleplayer) {
      return this.client.connect(this.localhost, id);
    } else {
      return this.client.connect(this.currentInformation, id);
    }
  }

  /**
   * Gets localhost.
   *
   * @return the localhost
   */
  public ServerInformation getLocalhost() {
    return localhost;
  }

  /**
   * Quit.
   */
  public void quit() {
    this.client.quit();
  }

  /**
   * Send command.
   *
   * @param command the command
   */
  public void sendCommand(Command command) {
    this.client.sendCommand(command);
  }

  /**
   * Gets singleplayer.
   *
   * @return the singleplayer
   */
  public boolean getSingleplayer() {
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
}
