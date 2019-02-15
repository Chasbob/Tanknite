package com.aticatac.client;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
//    private InetAddress server;
    private int discoveryPort = 5000;

    Data() {
//        try {
//            server = InetAddress.getByName("server.lan");
//        } catch (UnknownHostException e) {
//            throw new ExceptionInInitializerError(e);
//        }
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort() {
        return discoveryPort;
    }

//    /**
//     * Gets server.
//     *
//     * @return the server
//     */
//    public InetAddress getServer() {
//        return server;
//    }
}

