package com.aticatac.client.networking;

import com.aticatac.common.model.ServerInformation;
import java.util.ArrayList;

/**
 * The type Populate servers.
 */
class PopulateServers extends Thread {
  private final ArrayList<ServerInformation> servers;

  /**
   * Instantiates a new Populate servers.
   *
   * @param servers the servers
   */
  public PopulateServers(ArrayList<ServerInformation> servers) {
    this.servers = servers;
  }

  @Override
  public void run() {
    super.run();
    try {
      ServerInformation newServer = new BroadcastListener().call();
      this.servers.add(newServer);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
