package com.aticatac.common.model;

import java.net.InetAddress;

/**
 * The type D binfo.
 */
public class DBinfo extends Model {
  private final InetAddress address;
  private final int port;

  /**
   * Instantiates a new Model.
   *
   * @param address the address
   * @param port    the port
   */
  public DBinfo(final InetAddress address, final int port) {
    super();
    this.address = address;
    this.port = port;
  }

  /**
   * Gets address.
   *
   * @return the address
   */
  public InetAddress getAddress() {
    return address;
  }

  /**
   * Gets port.
   *
   * @return the port
   */
  public int getPort() {
    return port;
  }
}
