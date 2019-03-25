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
  final Server.ServerData data = Server.ServerData.INSTANCE;
  private final Logger logger;
  private final ModelReader modelReader;
  private final Update update;
  private final ConcurrentHashMap<String, Container> players;
  private final ConcurrentHashMap<String, Container> projectiles;
  private final ConcurrentHashMap<Integer, Container> powerups;
  private final ConcurrentHashMap<String, Container> newShots;
  private boolean shutdown;

  /**
   * Instantiates a new Updater.
   */
  Updater() {
    this.logger = Logger.getLogger(getClass());
    this.update = new Update();
    this.shutdown = false;
    this.modelReader = new ModelReader();
    players = update.getPlayers();
    projectiles = update.projectileMap();
    powerups = update.getPowerups();
    newShots = update.getNewShots();
    EventBusFactory.getEventBus().register(this);
    updatePlayers();
  }

  void shutdown() {
    this.shutdown = true;
  }

  private void updatePlayers() {
    this.logger.trace("updating players");
    this.update.setStart(data.isStart());
    this.logger.trace("Game started: " + data.isStart());
    this.update.clearPlayers();
    this.update.clearProjectiles();
    for (Bullet b : data.getGame().getBullets()) {
      this.update.addProjectile(b.getContainer());
      this.logger.trace(b.getContainer());
    }
    for (Entity e :
        data.getGame().getPowerups()) {
      this.update.addPowerup(e.getContainer());
      this.logger.trace(e.getContainer());
    }
    for (Tank c : data.getGame().getPlayerMap().values()) {
      this.logger.trace("Adding tank: " + c.getName());
      this.update.addPlayer(c.getContainer());
    }
  }

  @Override
  public void run() {
    this.logger.trace("Running...");
    while (!Thread.currentThread().isInterrupted() && !shutdown) {
      double nanoTime = System.nanoTime();
      this.update.setStart(data.isStart());
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
    final Server.ServerData s = Server.ServerData.INSTANCE;
    DatagramPacket packet = new DatagramPacket(bytes, bytes.length, s.getServer(), s.getPort());
    this.logger.trace("Packet: " + packet.getAddress().toString() + ":" + packet.getPort());
    s.multicastPacket(packet);
  }

  @Subscribe
  private void playersChanged(PlayersChangedEvent e) {
    switch (e.action) {
      case ADD:
        this.players.put(e.getContainer().getId(), e.getContainer());
        break;
      case REMOVE:
        this.players.remove(e.getContainer().getId());
        break;
      case UPDATE:
        this.players.put(e.getContainer().getId(), e.getContainer());
        break;
      default:
        break;
    }
  }

  @Subscribe
  private void powerupsChanged(PowerupsChangedEvent e) {
    switch (e.getAction()) {
      case ADD:
        this.powerups.put(e.getContainer().hashCode(), e.getContainer());
        break;
      case REMOVE:
        this.powerups.remove(e.getContainer().hashCode());
        break;
      case UPDATE:
        this.powerups.put(e.getContainer().hashCode(), e.getContainer());
        break;
      default:
        break;
    }
  }

  @Subscribe
  private void bulletsChanged(BulletsChangedEvent e) {
    switch (e.getAction()) {
      case ADD:
        this.projectiles.put(e.getBullet().getId(), e.getBullet());
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
          this.newShots.remove(e.getBullet().getId());
        }).start();
        break;
      case REMOVE:
        this.projectiles.remove(e.getBullet().getId());
        break;
      case UPDATE:
        this.projectiles.put(e.getBullet().getId(), e.getBullet());
        break;
      default:
        break;
    }
  }
}
