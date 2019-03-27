package com.aticatac.database;

import com.aticatac.common.model.DBinfo;
import com.aticatac.common.model.ModelReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * The type Discovery.
 */
@SuppressWarnings("ALL")
public class Discovery implements Runnable {
  private final Logger logger;
  private final ModelReader modelReader;
  private final DatagramSocket socket;
  private List<DatagramPacket> packets;
  private boolean shutdown;

  /**
   * Instantiates a new Discovery.
   *
   * @param name the name
   *
   * @throws IOException the io exception
   */
  Discovery() throws IOException {
    logger = Logger.getLogger(getClass());
    this.modelReader = new ModelReader();
    packets = new ArrayList<>();
    buildPackets();
    shutdown = false;
    socket = new DatagramSocket();
  }

  private void buildPackets() throws IOException {
    for (InterfaceAddress current : listAddresses()) {
      if (current.getBroadcast() == null) {
        continue;
      }
      DBinfo information = new DBinfo(current.getAddress(), 6000);
      byte[] bytes = modelReader.toBytes(information);
      DatagramPacket packet = new DatagramPacket(bytes, bytes.length, current.getBroadcast(), 6000);
      this.logger.info(current.getBroadcast());
      packets.add(packet);
    }
  }

  @Override
  public void run() {
    this.logger.trace("Running...");
    try {
      broadcast();
    } catch (IOException e) {
      this.logger.error(e);
      return;
    }
    this.logger.trace("Finished!");
  }

  public void shutdown() {
    this.shutdown = true;
  }

  private void broadcast() throws IOException {
    for (DatagramPacket packet : this.packets) {
      logger.trace("Sent packet to: " + packet.getAddress());
    }
  }

  private List<InterfaceAddress> listAddresses() {
    List<InterfaceAddress> output = new ArrayList<>();
    Enumeration<NetworkInterface> net;
    try {
      net = NetworkInterface.getNetworkInterfaces();
    } catch (SocketException e) {
      throw new RuntimeException(e);
    }
    while (net.hasMoreElements()) {
      NetworkInterface element = net.nextElement();
      try {
        if (element.isVirtual() || element.isLoopback()) {
          continue;
        }
        List<InterfaceAddress> addresses = element.getInterfaceAddresses();
        for (InterfaceAddress ia : addresses) {
          if (ia.getAddress() instanceof Inet4Address) {
            if (ia.getAddress().isSiteLocalAddress()) {
              if (ia.getBroadcast() != null && ia.getAddress() != null) {
                output.add(ia);
              }
            }
          }
        }
      } catch (SocketException e) {
        e.printStackTrace();
      }
    }
    return output;
  }
}