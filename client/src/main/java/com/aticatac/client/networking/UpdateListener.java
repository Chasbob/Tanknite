package com.aticatac.client.networking;

import com.aticatac.client.screens.Screens;
import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.Updates.Update;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/**
 * The type Update listener.
 */
class UpdateListener extends Thread {
    //    final BlockingQueue<Update> updates;
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
//        this.updates = updates;
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
        logger.trace("Packet received!");
        Update update = ModelReader.toModel(bytes, Update.class);
        //TODO refactor to use queue all the way down
        this.logger.trace("Player count: " + update.getPlayers().size());
        if (update.isChanged()) {
            Screens.INSTANCE.setUpdate(update);
            this.logger.trace("added update to queue.");
        }
    }
}
