package com.aticatac.client.networking;

import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.Updates.Update;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.concurrent.BlockingQueue;

/**
 * The type Update listener.
 */
class UpdateListener extends Thread {
    private final MulticastSocket multicastSocket;
    private final Logger logger;
    final BlockingQueue<Update> updates;

    /**
     * Instantiates a new Update listener.
     *
     * @param multicastSocket the multicast socket
     */
    UpdateListener(MulticastSocket multicastSocket, BlockingQueue<Update> updates) {
        this.logger = Logger.getLogger(getClass());
        this.multicastSocket = multicastSocket;
        this.updates = updates;
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
            } catch (InvalidBytes invalidBytes) {
                invalidBytes.printStackTrace();
            }
        }
    }

    private void listen() throws IOException, InvalidBytes {
        logger.trace("Listening...");
        byte[] bytes = new byte[8000];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        this.multicastSocket.receive(packet);
        logger.info("Packet received!");
        Update update = ModelReader.toModel(bytes, Update.class);
        this.updates.add(update);
        logger.trace(update.getId());
    }
}
