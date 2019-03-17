package com.aticatac.server.bus.listener;

import com.aticatac.common.objectsystem.Container;
import com.aticatac.server.bus.event.BulletsChangedEvent;
import com.aticatac.server.bus.event.PlayersChangedEvent;
import com.google.common.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class UpdateChangesListener {
  private final ConcurrentHashMap<String, Container> players;
  private final ArrayList<Container> projectiles;

  public UpdateChangesListener(final ConcurrentHashMap<String, Container> players, final ArrayList<Container> projectiles) {
    System.out.println("Update changes listener");
    this.players = players;
    this.projectiles = projectiles;
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
        players.replace(e.getContainer().getId(), e.getContainer());
        break;
    }
  }

  @Subscribe
  private void bulletsChanged(BulletsChangedEvent e) {
    switch (e.getAction()){
      case ADD:
        break;
      case REMOVE:
        break;
      case UPDATE:
        break;
    }
  }
}
