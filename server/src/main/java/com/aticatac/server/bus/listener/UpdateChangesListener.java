package com.aticatac.server.bus.listener;

import com.aticatac.common.objectsystem.Container;
import com.aticatac.server.bus.event.BulletsChangedEvent;
import com.aticatac.server.bus.event.PlayersChangedEvent;
import com.aticatac.server.bus.event.PowerupsChangedEvent;
import com.google.common.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

public class UpdateChangesListener {
  private final ConcurrentHashMap<String, Container> players;
  private final ArrayList<Container> projectiles;
  private final ArrayList<Container> powerups;
  private final Logger logger;

  public UpdateChangesListener(final ConcurrentHashMap<String, Container> players, final ArrayList<Container> projectiles, final ArrayList<Container> powerups) {
    System.out.println("Update changes listener");
    this.players = players;
    this.projectiles = projectiles;
    this.powerups = powerups;
    this.logger = Logger.getLogger(getClass());
  }

  @Subscribe
  private void playersChanged(PlayersChangedEvent e) {
    switch (e.action) {
      case ADD:
        players.put(e.getContainer().getId(), e.getContainer());
        break;
      case REMOVE:
        players.remove(e.getContainer().getId());
        break;
      case UPDATE:
        players.put(e.getContainer().getId(), e.getContainer());
        break;
    }
  }

  @Subscribe
  private void powerupsChanged(PowerupsChangedEvent e) {
    switch (e.getAction()) {
      case ADD:
        powerups.add(e.getContainer());
        break;
      case REMOVE:
        powerups.remove(e.getContainer());
        break;
      case UPDATE:
        powerups.add(e.getContainer());
        break;
    }
  }

  @Subscribe
  private void bulletsChanged(BulletsChangedEvent e) {
    switch (e.getAction()) {
      case ADD:
        projectiles.add(e.getBullet());
        break;
      case REMOVE:
        projectiles.remove(e.getBullet());
        break;
      case UPDATE:
        projectiles.add(e.getBullet());
        break;
    }
  }
}
