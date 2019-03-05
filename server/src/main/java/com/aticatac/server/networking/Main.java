package com.aticatac.server.networking;

import org.apache.log4j.Logger;

/**
 * The type Main.
 *
 * @author Charles de Freitas
 */
public class Main {
    Logger logger = Logger.getLogger(Main.class);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
