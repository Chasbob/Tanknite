package com.aticatac.server.networking;

import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.objectsystem.Converter;
import com.aticatac.common.objectsystem.GameObject;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type EmptyUpdate.
 *
 * @author Charles de Freitas
 */
public class Updater extends Thread {
    private final MulticastSocket multicastSocket;
    private final InetAddress address;
    private final Logger logger;
    private ConcurrentHashMap<String, Client> clients;
    private Update update;
    private boolean changes;

    /**
     * Instantiates a new EmptyUpdate.
     *
     * @param address the address
     * @throws IOException the io exception
     */
    Updater(InetAddress address, ConcurrentHashMap<String, Client> clients) throws IOException {
        this.logger = Logger.getLogger(getClass());
        this.address = address;
        this.multicastSocket = new MulticastSocket();
        this.update = new Update(true);
        this.changes = true;
        this.clients = clients;
    }

    private void updateClientNames() {
        for (String client :
                this.clients.keySet()) {
            if (!this.update.getPlayers().contains(client)) {
                this.logger.info("Adding: " + client + " to clientNames.");
                this.update.addPlayer(client);
                this.changes = true;
            }
        }
    }

    public void addObject(GameObject objects) {
        try {
            this.update.setObj(Converter.deconstruct(objects));
        } catch (Exception unchecked) {
            unchecked.printStackTrace();
        }
    }
    /**
     * Add client.
     *
     * @param client the client
     */
    public void addClient(String client) {
        if (!this.update.getPlayers().contains(client)) {
            this.update.addPlayer(client);
            this.changes = true;
        }
    }
//    /**
//     * Sets update model.
//     *
//     * @param update the update model
//     */
//    synchronized void setUpdateModel(Update update) {
//        //TODO optimise setting new model by calculating a differential
//        // as to send less data.
//        this.update = update;
//    }

    @Override
    public void run() {
        this.logger.trace("Running...");
        super.run();
        while (!this.isInterrupted()) {
            try {
                updateClientNames();
                if (this.changes) {
                    this.logger.info("Changes detected.");
                    this.logger.trace("players: " + this.update.getPlayers().toString());
                    this.logger.trace("Broadcasting...");
                    broadcast(this.update);
                    this.logger.trace("Setting changes to false.");
                    this.changes = false;
                } else {
                    this.logger.trace("Broadcasting no changes.");
                    broadcast(this.update);
                }
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    this.logger.error(e);
//                }
            } catch (IOException e) {
                this.logger.error(e);
            }
        }
    }

    private void broadcast(Update update) throws IOException {
        this.logger.trace("Broadcasting...");
        this.logger.trace("Player count: " + this.update.getPlayers().size());
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
