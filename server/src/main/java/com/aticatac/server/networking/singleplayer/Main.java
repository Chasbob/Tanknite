package com.aticatac.server.networking.singleplayer;

import org.apache.log4j.Logger;

import java.net.SocketException;

/**
 * The type Main.
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.run();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
