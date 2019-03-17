package com.aticatac.common.objectsystem;

import java.util.Objects;

public class Container {
  private final String texture;
  private final int x;
  private final int y;
  private final int r;
  private final int health;
  private final int ammo;
  private final String id;
  private final ObjectType objectType;

  public Container(int x, int y, int r, int health, int ammo, String id, ObjectType objectType) {
    this.texture = "";
    this.x = x;
    this.y = y;
    this.r = r;
    this.health = health;
    this.ammo = ammo;
    this.id = id;
    this.objectType = objectType;
  }

  public Container() {
    this.texture = "";
    this.x = 1000;
    this.y = 1000;
    this.r = 0;
    this.health = 100;
    this.ammo = 30;
    this.id = "";
    this.objectType = ObjectType.OTHER;
  }

  public int getHealth() {
    return health;
  }

  public int getAmmo() {
    return ammo;
  }

  public String getTexture() {
    return texture;
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
    return getX() == container.getX() &&
        getY() == container.getY() &&
        getR() == container.getR() &&
        getHealth() == container.getHealth() &&
        getAmmo() == container.getAmmo() &&
        getTexture().equals(container.getTexture()) &&
        getId().equals(container.getId()) &&
        getObjectType() == container.getObjectType();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getTexture(), getX(), getY(), getR(), getHealth(), getAmmo(), getId(), getObjectType());
  }

  @Override
  public String toString() {
    return "Container{" +
        "texture='" + texture + '\'' +
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

  public ObjectType getObjectType() {
    return objectType;
  }
}
