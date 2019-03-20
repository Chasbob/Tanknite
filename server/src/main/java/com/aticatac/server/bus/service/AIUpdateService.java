package com.aticatac.server.bus.service;

import com.aticatac.server.bus.EventBusFactory;
import com.aticatac.server.components.ai.AIInput;
import com.aticatac.server.components.ai.PlayerState;
import com.aticatac.server.components.ai.PowerUpState;
import com.aticatac.server.objectsystem.entities.Tank;
import com.google.common.eventbus.EventBus;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class AIUpdateService {
  private final EventBus bus;
  private final ConcurrentHashMap<String, Tank> playerMap;
  private final ArrayList<PowerUpState> powerUpStates;
  private final CopyOnWriteArraySet<PowerUpState> powerups;

  public AIUpdateService(final ConcurrentHashMap<String, Tank> playerMap, final CopyOnWriteArraySet<PowerUpState> powerups) {
    this.playerMap = playerMap;
    bus = EventBusFactory.getEventBus();
    powerUpStates = new ArrayList<>();
    this.powerups = powerups;
  }

  public void update() {
    ArrayList<PlayerState> playerStates = new ArrayList<>();
    for (Tank t :
        playerMap.values()) {
      playerStates.add(t.getPlayerState());
    }
    bus.post(new AIInput(PlayerState.none, 30, playerStates, new ArrayList<>(powerups)));
  }
}
