package com.aticatac.client.networking;

import com.aticatac.common.model.ServerInformation;
import java.util.ArrayList;

/**
 * The enum Servers.
 */
public enum Servers {
  /**
   * Instance servers.
   */
  INSTANCE;
  private final ArrayList<ServerInformation> servers;
  private final PopulateServers pop;

  Servers() {
    System.out.println("Servers enum starting");
    this.servers = new ArrayList<>();
    this.pop = new PopulateServers(this.servers);
    this.pop.start();
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
  public ArrayList<ServerInformation> getServers() {
    return servers;
  }
}
