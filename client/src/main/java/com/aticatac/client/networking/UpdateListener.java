package com.aticatac.client.networking;

import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.Updates.Update;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/**
 * The type Update listener.
 */
public class UpdateListener extends Thread {
    private final MulticastSocket multicastSocket;
    private final Logger logger;

    /**
     * Instantiates a new Update listener.
     *
     * @param multicastSocket the multicast socket
     */
    UpdateListener(MulticastSocket multicastSocket) {
        this.logger = Logger.getLogger(getClass());
        this.multicastSocket = multicastSocket;
    }

    @Override
    public void run() {
        logger.trace("Running...");
        super.run();
        while (!this.isInterrupted()) {
            try {
                listen();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    private void listen() throws IOException {
        logger.trace("Listening...");
        byte[] bytes = new byte[8000];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        this.multicastSocket.receive(packet);
        logger.info("Packet received!");
        Update update = ModelReader.toModelUDP(bytes, Update.class);
        logger.info(update.getId());
    }
}
