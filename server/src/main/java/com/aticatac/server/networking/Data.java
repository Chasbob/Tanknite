package com.aticatac.server.networking;

import java.net.*;
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
    private InetAddress server;
    private InetAddress multicast;
    private List<InterfaceAddress> broadcast = listIPAddresses();
    private int port = 5500;
    private int broadcastPort = 5000;

    Data() {
        try {
            server = InetAddress.getLocalHost();
            multicast = InetAddress.getByName("225.4.5.6");
        } catch (UnknownHostException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Gets broadcast port.
     *
     * @return the broadcast port
     */
    public int getBroadcastPort() {
        return broadcastPort;
    }

    /**
     * Gets broadcast.
     *
     * @return the broadcast
     */
    public List<InterfaceAddress> getInterfaces() {
        return broadcast;
    }

    private List<InterfaceAddress> listIPAddresses() {
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

    /**
     * Gets multicast.
     *
     * @return the multicast
     */
    public InetAddress getMulticast() {
        return multicast;
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
     * Gets server.
     *
     * @return the server
     */
    public InetAddress getServer() {
        return server;
    }
}
