package com.aticatac.server.networking;

import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.objectsystem.Container;
import com.aticatac.common.objectsystem.GameObject;
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
    for (GameObject c :
        Server.ServerData.INSTANCE.getGame().getRoot().getChildren().get("Player Container").getChildren().values()) {
      this.update.addPlayer(new Container(c));
    }
  }

  @Override
  public void run() {
    this.logger.trace("Running...");
    while (!Thread.currentThread().isInterrupted() && !shutdown) {
      double nanoTime = System.nanoTime();
      updatePlayers();
      tcpBroadcast();
      try {
        broadcast();
      } catch (IOException e) {
        this.logger.info("stopping due to IO");
        this.shutdown = true;
      }
      while (System.nanoTime() - nanoTime < 1000000000 / 60) {
        try {
          Thread.sleep(0);
        } catch (InterruptedException e) {
          e.printStackTrace();
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
