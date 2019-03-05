package com.aticatac.server.networking;

import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.objectsystem.Container;
import com.aticatac.server.gamemanager.Manager;
import java.io.IOException;
import java.net.DatagramPacket;
import org.apache.log4j.Logger;

/**
 * The type EmptyUpdate.
 *
 * @author Charles de Freitas
 */
public class Updater implements Runnable {
    private final Logger logger;
    private Update update;
    private boolean changes;

    /**
     * Instantiates a new Updater.
     */
    Updater() {
        this.logger = Logger.getLogger(getClass());
        this.update = new Update(true);
        this.changes = true;
    }

    private void updateClientNames() {
        for (String client :
        Server.ServerData.INSTANCE.getClients().keySet()) {
            if (!this.update.getPlayers().contains(client)) {
                this.logger.info("Adding: " + client + " to clientNames.");
                this.update.addPlayer(client);
                this.changes = true;
            }
        }
    }
    /**
     * Add object.
     *
     * @param objects the objects
     */
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

    @Override
    public void run() {
        this.logger.trace("Running...");
        while (!Thread.currentThread().isInterrupted()) {
            double stime = System.nanoTime();
            try {
                updateClientNames();
                this.update.setRootContainer(new Container(Manager.INSTANCE.getRoot()));
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
            } catch (IOException e) {
                this.logger.error(e);
                return;
            }
            while (System.nanoTime() - stime < 1000000000 / 60) {
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        this.logger.warn("Finished!");
    }

    private void updateObjects() {
    }

    private void broadcast(Update update) throws IOException {
        this.logger.trace("Broadcasting...");
        this.logger.trace("Player count: " + this.update.getPlayers().size());
        byte[] bytes = ModelReader.toBytes(update);
        this.logger.warn(bytes.length);
        final Server.ServerData s = Server.ServerData.INSTANCE;
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, s.getServer(), s.getPort());
        this.logger.trace("Packet: " + packet.getAddress().toString() + ":" + packet.getPort());
        s.multicastPacket(packet);
    }
}
