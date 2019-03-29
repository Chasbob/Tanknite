package com.aticatac.client.server.bus.event;

import com.aticatac.client.server.objectsystem.Entity;
import com.aticatac.client.server.objectsystem.entities.Bullet;
import java.util.Objects;

/**
 * The type Shoot event.
 */
public class ShootEvent {
  private final Entity shooter;
  private final Bullet bullet;

  /**
   * Instantiates a new Shoot event.
   *
   * @param shooter the shooter
   * @param bullet  the bullet
   */
  public ShootEvent(final Entity shooter, final Bullet bullet) {
    this.shooter = shooter;
    this.bullet = bullet;
  }

  @Override
  public int hashCode() {
    return Objects.hash(shooter, getBullet());
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final ShootEvent that = (ShootEvent) o;
    return shooter.equals(that.shooter) &&
        getBullet().equals(that.getBullet());
  }

  /**
   * Gets bullet.
   *
   * @return the bullet
   */
  public Bullet getBullet() {
    return bullet;
  }
}
