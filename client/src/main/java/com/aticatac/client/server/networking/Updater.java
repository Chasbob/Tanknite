package com.aticatac.client.server.networking;

import com.aticatac.client.server.bus.event.BulletsChangedEvent;
import com.aticatac.client.server.bus.event.PlayersChangedEvent;
import com.aticatac.client.server.bus.event.PowerupsChangedEvent;
import com.aticatac.client.server.objectsystem.Entity;
import com.aticatac.client.server.objectsystem.entities.Bullet;
import com.aticatac.client.server.objectsystem.entities.Tank;
import com.aticatac.common.Stoppable;
import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.objectsystem.containers.Container;
import com.aticatac.common.objectsystem.containers.KillLogEvent;
import com.aticatac.common.objectsystem.containers.PlayerContainer;
import com.google.common.eventbus.Subscribe;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.apache.log4j.Logger;

import static com.aticatac.client.bus.EventBusFactory.serverEventBus;

/**
 * The type EmptyUpdate.
 *
 * @author Charles de Freitas
 */
public class Updater implements Runnable, Stoppable {
  /**
   * The Data.
   */
  final Server.ServerData data = Server.ServerData.INSTANCE;
  private final Logger logger;
  private final ModelReader modelReader;
  private final Update update;
  private final ConcurrentHashMap<String, PlayerContainer> players;
  private final ConcurrentHashMap<String, Container> projectiles;
  private final ConcurrentHashMap<Integer, Container> powerups;
  private final ConcurrentHashMap<String, Container> newShots;
  private final CopyOnWriteArraySet<KillLogEvent> killLogEvents;
  private final CopyOnWriteArraySet<KillLogEvent> powerupEvent;
  private boolean run;

  /**
   * Instantiates a new Updater.
   */
  Updater() {
    this.logger = Logger.getLogger(getClass());
    this.update = new Update();
    this.run = true;
    this.modelReader = new ModelReader();
    players = update.getPlayers();
    projectiles = update.projectileMap();
    powerups = update.getPowerups();
    newShots = update.getNewShots();
    serverEventBus.register(this);
    updatePlayers();
    killLogEvents = update.getKillLogEvents();
    powerupEvent = update.getPowerupEvent();
  }

  @Override
  public void shutdown() {
    this.run = false;
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
    for (Entity e : data.getGame().getPowerups()) {
      this.update.addPowerup(e.getContainer());
      this.logger.trace(e.getContainer());
    }
    for (Tank c : data.getGame().getPlayerMap().values()) {
      this.logger.trace("Adding tank: " + c.getName());
      this.update.addPlayer(c.getPlayerContainer());
    }
  }

  @Override
  public void run() {
    this.logger.trace("Running...");
    while (!Thread.currentThread().isInterrupted() && run) {
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
        this.logger.info(e);
        this.players.put(e.getPlayerContainer().getId(), e.getPlayerContainer());
        break;
      case REMOVE:
        this.logger.info(e);
//        this.players.remove(e.getPlayerContainer().getId());
        this.players.get(e.getPlayerContainer().getId()).setAlive(false);
        break;
      case UPDATE:
        this.players.put(e.getPlayerContainer().getId(), e.getPlayerContainer());
        break;
      default:
        break;
    }
  }

  @Subscribe
  private void killLogEvent(KillLogEvent event) {
    new Thread(() -> {
      killLogEvents.add(event);
      double nanoTime = System.nanoTime();
      while (System.nanoTime() - nanoTime < 5000000000d) {
        try {
          Thread.sleep(0);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      killLogEvents.remove(event);
    }).start();
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
