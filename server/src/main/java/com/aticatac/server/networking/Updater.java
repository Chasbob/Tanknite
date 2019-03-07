package com.aticatac.server.networking;

import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.objectsystem.Container;
import com.aticatac.common.objectsystem.GameObject;
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

  private void updatePlayers() {
    for (GameObject c :
    Manager.INSTANCE.getRoot().getChildren().get("Player Container").getChildren().values()) {
      this.update.addPlayer(new Container(c));
    }
  }

  @Override
  public void run() {
    this.logger.trace("Running...");
    while (!Thread.currentThread().isInterrupted()) {
      double stime = System.nanoTime();
      try {
        updatePlayers();
//        this.update.setRootContainer(new Container(Manager.INSTANCE.getRoot()));
        if (this.changes) {
          this.logger.info("Changes detected.");
          this.logger.trace("players: " + this.update.getPlayers().toString());
          this.logger.trace("Broadcasting...");
          broadcast();
          this.logger.trace("Setting changes to false.");
          this.changes = false;
        } else {
          this.logger.trace("Broadcasting no changes.");
        }
        broadcast();
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

  private void broadcast() throws IOException {
    this.logger.trace("Broadcasting...");
    this.logger.trace("Player count: " + this.update.getPlayers().size());
    byte[] bytes = ModelReader.toBytes(this.update);
//    this.logger.info(bytes.length);
    final Server.ServerData s = Server.ServerData.INSTANCE;
    DatagramPacket packet = new DatagramPacket(bytes, bytes.length, s.getServer(), s.getPort());
    this.logger.trace("Packet: " + packet.getAddress().toString() + ":" + packet.getPort());
    s.multicastPacket(packet);
  }
}
