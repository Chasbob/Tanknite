package com.aticatac.common.model;

import java.net.InetAddress;

/**
 * The type Server infomation.
 */
public class ServerInformation extends Model {
  private final InetAddress address;
  private final int port;
  private final int maxPlayers;
  private int playerCount;

  /**
   * Instantiates a new Model.
   *
   * @param id         the id
   * @param address    the address
   * @param port       the port
   * @param maxPlayers the max players
   */
  public ServerInformation(String id, InetAddress address, int port, int maxPlayers) {
    this(id, address, port, maxPlayers, 0);
  }

  /**
   * Instantiates a new Model.
   *
   * @param id          the id
   * @param address     the address
   * @param port        the port
   * @param maxPlayers  the max players
   * @param playerCount the player count
   */
  public ServerInformation(String id, InetAddress address, int port, int maxPlayers, int playerCount) {
    super(id);
    this.address = address;
    this.port = port;
    this.maxPlayers = maxPlayers;
    this.playerCount = playerCount;
  }

  public ServerInformation(String id, InetAddress address, int port) {
    this(id, address, port, 0, 0);
  }

  /**
   * Gets max players.
   *
   * @return the max players
   */
  public int getMaxPlayers() {
    return maxPlayers;
  }

  /**
   * Gets player count.
   *
   * @return the player count
   */
  public int getPlayerCount() {
    return playerCount;
  }

  /**
   * Sets player count.
   *
   * @param playerCount the player count
   */
  public void setPlayerCount(int playerCount) {
    this.playerCount = playerCount;
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
   * Gets address.
   *
   * @return the address
   */
  public InetAddress getAddress() {
    return address;
  }

  @Override
  public String toString() {
    return super.toString() + address + ":" + port;
  }
}
