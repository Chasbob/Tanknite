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

  Servers() {
    System.out.println("Servers enum starting");
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
   * Gets servers.
   *
   * @return the servers
   */
  public ConcurrentHashMap<String, ServerInformation> getServers() {
    return servers;
  }
}
