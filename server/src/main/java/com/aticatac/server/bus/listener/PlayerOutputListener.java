package com.aticatac.server.bus.listener;

import com.aticatac.server.objectsystem.IO.outputs.PlayerOutput;
import com.aticatac.server.objectsystem.entities.Bullet;
import com.google.common.eventbus.Subscribe;
import java.util.concurrent.CopyOnWriteArraySet;

public class PlayerOutputListener {
  private final CopyOnWriteArraySet<Bullet> bullets;

  public PlayerOutputListener(final CopyOnWriteArraySet<Bullet> bullets) {
    this.bullets = bullets;
  }

  @Subscribe
  public void processPlayerOutput(PlayerOutput output) {
    bullets.addAll(output.getNewBullets());
  }
}
