package com.aticatac.server.networking;

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
  private final List<DatagramPacket> packets;
  private final Logger logger;
  private final ModelReader modelReader;

  /**
   * Instantiates a new Discovery.
   *
   * @param name the name
   * @throws IOException the io exception
   */
  Discovery(String name) throws IOException {
    this.name = name;
    this.modelReader = new ModelReader();
    this.packets = buildPackets(Server.ServerData.INSTANCE.getId());
    this.logger = Logger.getLogger(Discovery.class);
  }

  private List<DatagramPacket> buildPackets(String id) throws IOException {
    List<DatagramPacket> output = new ArrayList<>();
    for (InterfaceAddress current : Data.INSTANCE.getInterfaces()) {
      if (current.getBroadcast() == null) {
        continue;
      }
      ServerInformation information = new ServerInformation(id, current.getAddress(), Server.ServerData.INSTANCE.getPort());
      byte[] bytes = modelReader.toBytes(information);
      DatagramPacket packet = new DatagramPacket(bytes, bytes.length, current.getBroadcast(), CommonData.INSTANCE.getDiscoveryPort());
      output.add(packet);
    }
    return output;
  }

  @Override
  public void run() {
    this.logger.trace("Running...");
    while (!Thread.currentThread().isInterrupted()) {
      try {
        Thread.sleep(100);
        broadcast();
      } catch (InterruptedException | IOException e) {
        this.logger.error(e);
        return;
      }
    }
    this.logger.warn("Finished!");
  }

  private void broadcast() throws IOException {
    for (DatagramPacket packet : this.packets) {
      Server.ServerData.INSTANCE.broadcastPacket(packet);
      logger.trace("Sent packet to: " + packet.getAddress());
    }
  }
}
