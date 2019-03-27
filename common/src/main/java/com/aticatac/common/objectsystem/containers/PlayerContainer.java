package com.aticatac.common.objectsystem.containers;

import com.aticatac.common.objectsystem.EntityType;

public class PlayerContainer extends Container {
  protected final int health;
  protected final int ammo;

  public PlayerContainer(final int x, final int y, final int r, final String id, final EntityType objectType, final int health, final int ammo) {
    super(x, y, r, id, objectType);
    this.health = health;
    this.ammo = ammo;
  }

  public PlayerContainer(Container container) {
    super(container);
    this.health = 100;
    this.ammo = 30;
  }

  public PlayerContainer() {
    this.health = 100;
    this.ammo = 30;
  }

  public int getHealth() {
    return this.health;
  }

  public int getAmmo() {
    return this.ammo;
  }
}
