package com.aticatac.client.networking;

import com.aticatac.common.CommonData;
import com.aticatac.common.model.ServerInformation;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

/**
 * The type Populate servers.
 */
class PopulateServers implements Runnable {
  private final ConcurrentHashMap<String, ServerInformation> servers;
  private final Logger logger;
  private final DatagramSocket socket;
  private final BroadcastListener listener;

  /**
   * Instantiates a new Populate servers.
   *
   * @param servers the servers
   * @throws SocketException the socket exception
   */
  public PopulateServers(ConcurrentHashMap<String, ServerInformation> servers) throws SocketException {
    this.servers = servers;
    this.logger = Logger.getLogger(getClass());
    this.socket = new DatagramSocket(CommonData.INSTANCE.getDiscoveryPort());
    this.listener = new BroadcastListener(this.socket);
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      double nanoTime = System.nanoTime();
      try {
        this.logger.trace("getting server");
        ServerInformation newServer = listener.call();
        this.servers.put(newServer.getId(), newServer);
      } catch (Exception e) {
        e.printStackTrace();
      }
      while (Math.abs(System.nanoTime() - nanoTime) < 1000000000) {
        try {
          Thread.sleep(0);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
