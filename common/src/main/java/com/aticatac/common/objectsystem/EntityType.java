package com.aticatac.common.objectsystem;

/**
 * The enum Entity type.
 */
public enum EntityType {
  /**
   * None entity type.
   */
  NONE,
  /**
   * Tank entity type.
   */
  TANK(13, 6),
  /**
   * Bullet entity type.
   */
  BULLET(2, 10),
  /**
   * Wall entity type.
   */
  WALL(13),
  /**
   * Outofbounds entity type.
   */
  OUTOFBOUNDS,
  /**
   * Ammo powerup entity type.
   */
  AMMO_POWERUP(10),
  /**
   * Speed powerup entity type.
   */
  SPEED_POWERUP(10),
  /**
   * Health powerup entity type.
   */
  HEALTH_POWERUP(10),
  /**
   * Damage powerup entity type.
   */
  DAMAGE_POWERUP(10),
  /**
   * Bulletspray powerup entity type.
   */
  BULLETSPRAY_POWERUP(10),
  /**
   * Freezebullet powerup entity type.
   */
  FREEZEBULLET_POWERUP(10);
  /**
   * The Radius.
   */
  public final int radius;
  /**
   * The Velocity.
   */
  public final int velocity;

  EntityType(final int radius, final int velocity) {
    this.radius = radius;
    this.velocity = velocity;
  }

  EntityType(final int radius) {
    this(radius, 0);
  }

  EntityType() {
    this(0);
  }

  /**
   * Is power up boolean.
   *
   * @return the boolean
   */
  public boolean isPowerUp() {
    return this.toString().contains("POWERUP");
  }
}

