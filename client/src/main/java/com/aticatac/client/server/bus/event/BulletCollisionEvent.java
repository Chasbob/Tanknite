package com.aticatac.client.server.bus.event;

import com.aticatac.client.server.objectsystem.Entity;
import com.aticatac.client.server.objectsystem.entities.Bullet;

/**
 * The type Bullet collision event.
 */
public class BulletCollisionEvent {
  private final Bullet bullet;
  private final Entity hit;

  /**
   * Instantiates a new Bullet collision event.
   *
   * @param bullet the bullet
   * @param hit    the hit
   */
  public BulletCollisionEvent(final Bullet bullet, final Entity hit) {
    this.bullet = bullet;
    this.hit = hit;
  }

  /**
   * Gets bullet.
   *
   * @return the bullet
   */
  public Bullet getBullet() {
    return bullet;
  }

  /**
   * Gets hit.
   *
   * @return the hit
   */
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
