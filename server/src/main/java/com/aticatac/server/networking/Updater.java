package com.aticatac.server.networking;

import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.Updates.Update;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * The type EmptyUpdate.
 *
 * @author Charles de Freitas
 */
public class Updater extends Thread {
    private final MulticastSocket multicastSocket;
    private final InetAddress address;
    private final Logger logger;
    private Update update;
    private boolean changes;

    /**
     * Instantiates a new EmptyUpdate.
     *
     * @param address the address
     * @throws IOException the io exception
     */
    Updater(InetAddress address) throws IOException {
        this.logger = Logger.getLogger(getClass());
        this.address = address;
        this.multicastSocket = new MulticastSocket();
    }

    /**
     * Sets update model.
     *
     * @param update the update model
     */
    public synchronized void setUpdateModel(Update update) {
        //TODO optimise setting new model by calculating a differential
        // as to send less data.
        this.update = update;
    }

    @Override
    public void run() {
        this.logger.trace("Running...");
        super.run();
        while (!this.isInterrupted()) {
            try {
                if (this.changes) {
                    this.logger.info("Changes detected.");
                    this.logger.trace("Broadcasting...");
                    broadcast(this.update);
                    this.logger.trace("Setting changes to false.");
                    this.changes = false;
                } else {
                    this.logger.trace("Broadcasting no changes.");
                    broadcast(new Update(false));
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    this.logger.error(e);
                }
            } catch (IOException e) {
                this.logger.error(e);
            }
        }
    }

    private void broadcast(Update update) throws IOException {
        this.logger.trace("Broadcasting...");
        byte[] bytes = ModelReader.toBytes(update);
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, this.address, Data.INSTANCE.getPort());
        this.logger.trace("Packet: " + packet.getAddress().toString() + ":" + packet.getPort());
        try {
            this.multicastSocket.send(packet);
        } catch (IOException e) {
            this.logger.error("EmptyUpdate failed!");
            this.logger.error(e);
        }
    }
}
