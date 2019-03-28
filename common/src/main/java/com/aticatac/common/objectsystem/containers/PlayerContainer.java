package com.aticatac.common.objectsystem.containers;

import com.aticatac.common.objectsystem.EntityType;

/**
 * The type Player container.
 */
public class PlayerContainer extends Container {
  /**
   * The Health.
   */
  protected final int health;
  /**
   * The Ammo.
   */
  protected final int ammo;
  private boolean alive;

  /**
   * Instantiates a new Player container.
   *
   * @param x          the x
   * @param y          the y
   * @param r          the r
   * @param id         the id
   * @param objectType the object type
   * @param health     the health
   * @param ammo       the ammo
   */
  public PlayerContainer(final int x, final int y, final int r, final String id, final EntityType objectType, final int health, final int ammo, final boolean alive) {
    super(x, y, r, id, objectType);
    this.health = health;
    this.ammo = ammo;
    this.alive = alive;
  }

  /**
   * Instantiates a new Player container.
   *
   * @param container the container
   */
  public PlayerContainer(Container container) {
    super(container);
    this.health = 100;
    this.ammo = 30;
    this.alive = true;
  }

  /**
   * Instantiates a new Player container.
   */
  public PlayerContainer() {
    this.health = 100;
    this.ammo = 30;
    this.alive = true;
  }

  /**
   * Is alive boolean.
   *
   * @return the boolean
   */
  public boolean isAlive() {
    return alive;
  }

  /**
   * Gets health.
   *
   * @return the health
   */
  public int getHealth() {
    return this.health;
  }

  /**
   * Gets ammo.
   *
   * @return the ammo
   */
  public int getAmmo() {
    return this.ammo;
  }

  public void setAlive(final boolean alive) {
    this.alive = alive;
  }
}
