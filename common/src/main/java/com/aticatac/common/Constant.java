package com.aticatac.common;

/**
 * The type Constant.
 *
 * @author Charles de Freitas
 */
public final class Constant {
    private static final int port = 5500;
    private static final int discoveryPort = 5501;
    private static final String server = "server.lan";
    private static final String multicast = "225.4.5.6";

    /**
     * Gets port.
     *
     * @return the port
     */
    public static int getPort() {
        return port;
    }

    /**
     * Gets multicast.
     *
     * @return the multicast
     */
    public static String getMulticast() {
        return multicast;
    }

    /**
     * Gets server.
     *
     * @return the server
     */
    public static String getServer() {
        return server;
    }

    /**
     * Gets discovery port.
     *
     * @return the discovery port
     */
    public static int getDiscoveryPort() {
        return discoveryPort;
    }
}
