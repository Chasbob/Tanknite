package com.aticatac.server.bus.listener;

import com.aticatac.server.bus.event.PlayerInputEvent;
import com.aticatac.server.objectsystem.IO.inputs.PlayerInput;
import com.aticatac.server.objectsystem.entities.Tank;
import com.google.common.eventbus.Subscribe;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerInputListener {
  private final ConcurrentHashMap<String, Tank> playerMap;

  public PlayerInputListener(final ConcurrentHashMap<String, Tank> playerMap) {
    this.playerMap = playerMap;
  }

  @Subscribe
  public void processPlayerInput(PlayerInputEvent event) {
    if (playerMap.containsKey(event.commandModel.id)) {
      playerMap.get(event.commandModel.id).addFrame(new PlayerInput(event.commandModel));
    }
  }
}
