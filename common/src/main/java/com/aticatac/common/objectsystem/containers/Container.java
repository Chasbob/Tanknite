package com.aticatac.common.objectsystem.containers;

import com.aticatac.common.objectsystem.EntityType;
import java.util.Objects;

public class Container {
  protected final int x;
  protected final int y;
  protected final int r;
  protected final String id;
  protected final EntityType objectType;

  public Container(int x, int y, int r, String id, EntityType objectType) {
    this.x = x;
    this.y = y;
    this.r = r;
    this.id = id;
    this.objectType = objectType;
  }

  public Container() {
    this.x = 1000;
    this.y = 1000;
    this.r = 0;
    this.id = "";
    this.objectType = EntityType.NONE;
  }

  public Container(Container container) {
    this.x = container.x;
    this.y = container.y;
    this.r = container.r;
    this.id = container.id;
    this.objectType = container.objectType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getX(), getY(), getR(), getId(), getObjectType());
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
        && getId().equals(container.getId())
        && getObjectType() == container.getObjectType();
  }

  @Override
  public String toString() {
    return "Container{" +
        ", x=" + x +
        ", y=" + y +
        ", r=" + r +
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
