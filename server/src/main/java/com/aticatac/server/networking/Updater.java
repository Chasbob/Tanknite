package com.aticatac.server.networking;

import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.server.objectsystem.entities.Tank;
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
  private final ModelReader modelReader;
  private Update update;
  private boolean changes;
  private boolean shutdown;

  /**
   * Instantiates a new Updater.
   */
  Updater() {
    this.logger = Logger.getLogger(getClass());
    this.update = new Update(true);
    this.changes = true;
    this.shutdown = false;
    this.modelReader = new ModelReader();
  }

  private void updatePlayers() {
    this.logger.trace("updating players");
    this.update.setStart(Server.ServerData.INSTANCE.isStart());
    this.logger.trace("Game started: " + Server.ServerData.INSTANCE.isStart());
    this.update.clearPlayers();
    this.update.clearProjectiles();
    for (Tank c : Server.ServerData.INSTANCE.getGame().getPlayerMap().values()) {
      this.logger.trace("Adding tank: " + c.getName());
      this.update.addPlayer(c.getContainer());
//      for (GameObject cc : c.getChildren().values()) {
//        if (cc.getObjectType().isProjectile()) {
//          this.logger.info("Adding projectile: " + c.getName());
//          this.update.addProjectile(c.getContainer());
//        }
//      }
    }
  }

  @Override
  public void run() {
    this.logger.trace("Running...");
    while (!Thread.currentThread().isInterrupted() && !shutdown) {
      double nanoTime = System.nanoTime();
      updatePlayers();
      tcpBroadcast();
      while (System.nanoTime() - nanoTime < 1000000000 / 60) {
        try {
          Thread.sleep(0);
        } catch (InterruptedException e) {
          this.logger.error(e);
        }
      }
    }
    this.logger.warn("Finished!");
  }

  private void tcpBroadcast() {
    this.logger.trace("Broadcasting...");
    final Server.ServerData s = Server.ServerData.INSTANCE;
    for (Client c : s.getClients().values()) {
      c.sendUpdate(this.update);
    }
  }

  void tcpBroadcast(Update update) {
    this.logger.trace("Broadcasting...");
    final Server.ServerData s = Server.ServerData.INSTANCE;
    for (Client c : s.getClients().values()) {
      c.sendUpdate(update);
    }
  }

  private void broadcast() throws IOException {
    this.logger.trace("Broadcasting...");
    this.logger.trace("Player count: " + this.update.getPlayers().size());
    byte[] bytes = modelReader.toBytes(this.update);
//    this.logger.info(bytes.length);
    final Server.ServerData s = Server.ServerData.INSTANCE;
    DatagramPacket packet = new DatagramPacket(bytes, bytes.length, s.getServer(), s.getPort());
    this.logger.trace("Packet: " + packet.getAddress().toString() + ":" + packet.getPort());
    s.multicastPacket(packet);
  }
}
