package com.aticatac.server.bus.event;

import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.entities.Bullet;

public class BulletCollisionEvent {
  private final Bullet bullet;
  private final Entity hit;

  public BulletCollisionEvent(final Bullet bullet, final Entity hit) {
    this.bullet = bullet;
    this.hit = hit;
  }

  public Bullet getBullet() {
    return bullet;
  }

  public Entity getHit() {
    return hit;
  }

  @Override
  public String toString() {
    return "BulletCollisionEvent{" +
        "bullet=" + bullet +
        ", hit=" + hit +
        '}';
  }
}
