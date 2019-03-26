package com.aticatac.client.server.networking;

import java.net.Inet4Address;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * The type Data.
 *
 * @author Charles de Freitas
 */
public enum Data {
  /**
   * Instance data.
   */
  INSTANCE;
  private List<InterfaceAddress> broadcast;

  Data() {
    this.broadcast = listAddresses();
  }

  /**
   * Gets broadcast.
   *
   * @return the broadcast
   */
  public List<InterfaceAddress> getInterfaces() {
    return broadcast;
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
              output.add(ia);
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
