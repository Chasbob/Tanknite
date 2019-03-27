package com.aticatac.common.objectsystem;

import java.util.Objects;

public class Container {
  private final int x;
  private final int y;
  private final int r;
  private final int health;
  private final int ammo;
  private final String id;
  private final EntityType objectType;

  public Container(int x, int y, int r, int health, int ammo, String id, EntityType objectType) {
    this.x = x;
    this.y = y;
    this.r = r;
    this.health = health;
    this.ammo = ammo;
    this.id = id;
    this.objectType = objectType;
  }

  public Container() {
    this.x = 1000;
    this.y = 1000;
    this.r = 0;
    this.health = 100;
    this.ammo = 30;
    this.id = "";
    this.objectType = EntityType.NONE;
  }

  public int getHealth() {
    return health;
  }

  public int getAmmo() {
    return ammo;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getX(), getY(), getR(), getHealth(), getAmmo(), getId(), getObjectType());
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Container container = (Container) o;
    return getX() == container.getX()
        && getY() == container.getY()
        && getR() == container.getR()
        && getHealth() == container.getHealth()
        && getAmmo() == container.getAmmo()
        && getId().equals(container.getId())
        && getObjectType() == container.getObjectType();
  }

  @Override
  public String toString() {
    return "Container{" +
        ", x=" + x +
        ", y=" + y +
        ", r=" + r +
        ", health=" + health +
        ", ammo=" + ammo +
        ", id='" + id + '\'' +
        ", objectType=" + objectType +
        '}';
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getR() {
    return r;
  }

  public String getId() {
    return id;
  }

  public EntityType getObjectType() {
    return objectType;
  }
}
