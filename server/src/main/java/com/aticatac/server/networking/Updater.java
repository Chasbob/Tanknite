package com.aticatac.server.networking;

import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.objectsystem.Container;
import com.aticatac.server.bus.EventBusFactory;
import com.aticatac.server.bus.event.BulletsChangedEvent;
import com.aticatac.server.bus.event.PlayersChangedEvent;
import com.aticatac.server.bus.event.PowerupsChangedEvent;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.entities.Bullet;
import com.aticatac.server.objectsystem.entities.Tank;
import com.google.common.eventbus.Subscribe;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

/**
 * The type EmptyUpdate.
 *
 * @author Charles de Freitas
 */
public class Updater implements Runnable {
  final Server.ServerData d = Server.ServerData.INSTANCE;
  private final Logger logger;
  private final ModelReader modelReader;
  private final Update update;
  private final ConcurrentHashMap<String, Container> players;
  private final ConcurrentHashMap<String, Container> projectiles;
  private final ConcurrentHashMap<String, Container> powerups;
  private final ConcurrentHashMap<String, Container> newShots;
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
    this.players = update.getPlayers();
    this.projectiles = update.getProjectiles();
    this.powerups = update.getPowerups();
    this.newShots = update.getNewShots();
    EventBusFactory.getEventBus().register(this);
    updatePlayers();
  }

  public void shutdown() {
    this.shutdown = true;
  }

  private void updatePlayers() {
    this.logger.trace("updating players");
    this.update.setStart(d.isStart());
    this.logger.trace("Game started: " + d.isStart());
    this.update.clearPlayers();
    this.update.clearProjectiles();
    for (Bullet b : d.getGame().getBullets()) {
      this.update.addProjectile(b.getContainer());
      this.logger.trace(b.getContainer());
    }
    for (Entity e :
        d.getGame().getPowerups()) {
      this.update.addPowerup(e.getContainer());
      this.logger.trace(e.getContainer());
    }
    for (Tank c : d.getGame().getPlayerMap().values()) {
      this.logger.trace("Adding tank: " + c.getName());
      this.update.addPlayer(c.getContainer());
    }
  }

  @Override
  public void run() {
    this.logger.trace("Running...");
    while (!Thread.currentThread().isInterrupted() && !shutdown) {
      double nanoTime = System.nanoTime();
      this.update.setStart(d.isStart());
//      updatePlayers();
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
    byte[] bytes = modelReader.toBytes(this.update);
//    this.logger.info(bytes.length);
    final Server.ServerData s = Server.ServerData.INSTANCE;
    DatagramPacket packet = new DatagramPacket(bytes, bytes.length, s.getServer(), s.getPort());
    this.logger.trace("Packet: " + packet.getAddress().toString() + ":" + packet.getPort());
    s.multicastPacket(packet);
  }

  @Subscribe
  private void playersChanged(PlayersChangedEvent e) {
    switch (e.action) {
      case ADD:
        players.put(e.getContainer().getId(),e.getContainer());
//        update.addPlayer(e.getContainer());
        break;
      case REMOVE:
        players.remove(e.getContainer().getId());
//        update.removePlayer(e.getContainer());
        break;
      case UPDATE:
        players.put(e.getContainer().getId(),e.getContainer());
//        update.addPlayer(e.getContainer());
        break;
    }
  }

  @Subscribe
  private void powerupsChanged(PowerupsChangedEvent e) {
    switch (e.getAction()) {
      case ADD:
        powerups.put(e.getContainer().getId(), e.getContainer());
//        update.addPowerup(e.getContainer());
        break;
      case REMOVE:
        powerups.remove(e.getContainer().getId());
//        update.removePowerup((e.getContainer()));
        break;
      case UPDATE:
        powerups.put(e.getContainer().getId(), e.getContainer());
//        update.addPowerup(e.getContainer());
        break;
    }
  }

  @Subscribe
  private void bulletsChanged(BulletsChangedEvent e) {
    switch (e.getAction()) {
      case ADD:
        projectiles.put(e.getBullet().getId(), e.getBullet());
//        update.addProjectile(e.getBullet());
        new Thread(() -> {
          update.addNewShot(e.getBullet());
          double nanoTime = System.nanoTime();
          while (System.nanoTime() - nanoTime < 5000000000d) {
            try {
              Thread.sleep(0);
            } catch (InterruptedException er) {
              er.printStackTrace();
            }
          }
          update.removeNewShot(e.getBullet());
        }).start();
        break;
      case REMOVE:
        projectiles.remove(e.getBullet().getId());
//        update.removeProjectile(e.getBullet());
        break;
      case UPDATE:
        projectiles.put(e.getBullet().getId(), e.getBullet());
//        update.addProjectile(e.getBullet());
        break;
    }
  }
}
