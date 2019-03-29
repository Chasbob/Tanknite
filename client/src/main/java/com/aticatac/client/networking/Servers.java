package com.aticatac.client.networking;

import com.aticatac.common.model.ServerInformation;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The enum Servers.
 */
public enum Servers {
  /**
   * Instance servers.
   */
  INSTANCE;
  private final ConcurrentHashMap<String, ServerInformation> servers;
  private PopulateServers pop;
  private Thread popT;
  private int port;

  Servers() {
    System.out.println("Servers enum starting");
    port = 5500;
    this.servers = new ConcurrentHashMap<>();
    try {
      this.pop = new PopulateServers(this.servers);
      popT = new Thread(this.pop);
      popT.start();
    } catch (SocketException e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets instance.
   *
   * @return the instance
   */
  public static Servers getInstance() {
    return INSTANCE;
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
   * Gets servers.
   *
   * @return the servers
   */
  public ConcurrentHashMap<String, ServerInformation> getServers() {
    return servers;
  }

  /**
   * Clear servers.
   */
  public void clearServers() {
    servers.clear();
  }
}
