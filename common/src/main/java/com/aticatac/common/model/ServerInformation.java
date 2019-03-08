package com.aticatac.common.model;

import java.net.InetAddress;

/**
 * The type Server infomation.
 */
public class ServerInformation extends Model {
  private final InetAddress address;
  private final int port;

  /**
   * Instantiates a new Model.
   *
   * @param id      the id
   * @param address the address
   * @param port    the port
   */
  public ServerInformation(String id, InetAddress address, int port) {
    super(id);
    this.address = address;
    this.port = port;
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
