package com.aticatac.client.server.bus.service;

import com.aticatac.client.server.ai.AIInput;
import com.aticatac.client.server.ai.PlayerState;
import com.aticatac.client.server.ai.PowerUpState;
import com.aticatac.client.server.objectsystem.Entity;
import com.aticatac.client.server.objectsystem.entities.Tank;
import com.aticatac.common.Stoppable;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.aticatac.client.bus.EventBusFactory.serverEventBus;

public class AIUpdateService implements Stoppable {
  private final ConcurrentHashMap<String, Tank> playerMap;
  private final ArrayList<PowerUpState> powerUpStates;
  private final CopyOnWriteArraySet<Entity> powerups;

  public AIUpdateService(final ConcurrentHashMap<String, Tank> playerMap, final CopyOnWriteArraySet<Entity> powerups) {
    this.playerMap = playerMap;
    powerUpStates = new ArrayList<>();
    this.powerups = powerups;
  }

  public void update(final ConcurrentHashMap<String, Tank> playerMap, final CopyOnWriteArraySet<Entity> powerups) {
    ArrayList<PlayerState> playerStates = new ArrayList<>();
    for (Tank t : playerMap.values()) {
      if (t.isAlive()) {

        playerStates.add(t.getPlayerState());
      }
    }
    for (Entity e : powerups) {
      powerUpStates.add(new PowerUpState(e.getType(), e.getPosition()));
    }
    serverEventBus.post(new AIInput(30, playerStates, powerUpStates));
  }

  @Override
  public void shutdown() {
    playerMap.clear();
    powerUpStates.clear();
    powerups.clear();
  }
}
