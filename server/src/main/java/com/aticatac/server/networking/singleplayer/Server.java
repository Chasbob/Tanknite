package com.aticatac.server.networking.singleplayer;

import org.apache.log4j.Logger;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * The type Server.
 */
class Server {
    private final Logger logger;
    private ServerThread thread;

    /**
     * Instantiates a new Server.
     *
     * @throws SocketException the socket exception
     */
    Server() throws SocketException {
        this.logger = Logger.getLogger(Server.class);
        DatagramSocket serverSocket = new DatagramSocket(9800);
        this.thread = new ServerThread(serverSocket);
        this.thread.start();
    }

    /**
     * Start.
     */
    void run() {
        logger.info("Running server");
        this.thread.run();
    }

    /**
     * Stop.
     */
    void stop() {
        logger.info("Stopping server");
        this.thread.interrupt();
    }
}
