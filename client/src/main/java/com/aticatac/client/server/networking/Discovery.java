package com.aticatac.client.server.networking;

import com.aticatac.common.CommonData;
import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.ServerInformation;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InterfaceAddress;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * The type Discovery.
 */
public class Discovery implements Runnable {
  private final Logger logger;
  private final ModelReader modelReader;
  private final String name;
  private final ArrayList<ServerInformation> informations;
  private List<DatagramPacket> packets;
  private boolean run;

  /**
   * Instantiates a new Discovery.
   *
   * @param name the name
   * @throws IOException the io exception
   */
  Discovery(String name) throws IOException {
    this.name = name;
    this.modelReader = new ModelReader();
    this.informations = new ArrayList<>();
    packets = new ArrayList<>();
    buildPackets(Server.ServerData.INSTANCE.getId());
    this.logger = Logger.getLogger(Discovery.class);
    run = true;
  }

  private void buildPackets(String id) throws IOException {
    for (InterfaceAddress current : Data.INSTANCE.getInterfaces()) {
      if (current.getBroadcast() == null) {
        continue;
      }
      ServerInformation information = new ServerInformation(id, current.getAddress(), Server.ServerData.INSTANCE.getPort(), Server.ServerData.INSTANCE.getMaxPlayers(), Server.ServerData.INSTANCE.playerCount());
      informations.add(information);
      byte[] bytes = modelReader.toBytes(information);
      DatagramPacket packet = new DatagramPacket(bytes, bytes.length, current.getBroadcast(), CommonData.INSTANCE.getDiscoveryPort());
      packets.add(packet);
    }
  }

  @Override
  public void run() {
    this.logger.trace("Running...");
    while (!Thread.currentThread().isInterrupted() && run) {
      try {
        double nanoTime = System.nanoTime();
        broadcast();
        while (System.nanoTime() - nanoTime < 3000000000d) {
          try {
            Thread.sleep(0);
          } catch (InterruptedException e) {
            this.logger.error(e);
          }
        }
      } catch (IOException e) {
        this.logger.error(e);
        return;
      }
    }
    this.logger.warn("Finished!");
  }

  /**
   * Shutdown.
   */
  public void shutdown() {
    this.run = false;
  }

  private void broadcast() throws IOException {
    if (Server.ServerData.INSTANCE.refreshBroadcast()) {
      buildPackets(name);
    }
    for (DatagramPacket packet : this.packets) {
      Server.ServerData.INSTANCE.broadcastPacket(packet);
      logger.trace("Sent packet to: " + packet.getAddress());
    }
  }
}
