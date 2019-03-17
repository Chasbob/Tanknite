package com.aticatac.server.bus.event;

import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.entities.Bullet;

public class ShootEvent {
  private final Entity shooter;
  private final Bullet bullet;

  public ShootEvent(final Entity shooter, final Bullet bullet) {
    this.shooter = shooter;
    this.bullet = bullet;
  }

  public Bullet getBullet() {
    return bullet;
  }
}
