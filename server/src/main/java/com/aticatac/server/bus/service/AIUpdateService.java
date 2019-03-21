package com.aticatac.server.bus.service;

import com.aticatac.server.bus.EventBusFactory;
import com.aticatac.server.components.ai.AIInput;
import com.aticatac.server.components.ai.PlayerState;
import com.aticatac.server.components.ai.PowerUpState;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.entities.Tank;
import com.google.common.eventbus.EventBus;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class AIUpdateService {
  private final EventBus bus;

  public AIUpdateService() {
    bus = EventBusFactory.getEventBus();
  }

  public void update(final ConcurrentHashMap<String, Tank> tanks, final CopyOnWriteArraySet<Entity> powerups) {
    ArrayList<PlayerState> playerStates = new ArrayList<>();
    ArrayList<PowerUpState> powerUpStates = new ArrayList<>();
    for (Tank t :
        tanks.values()) {
      playerStates.add(t.getPlayerState());
    }
    for (Entity e :
        powerups) {
      powerUpStates.add(new PowerUpState(e.type, e.getPosition()));
    }
    bus.post(new AIInput(30, playerStates, powerUpStates));
  }
}
