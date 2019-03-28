package com.aticatac.common.objectsystem.containers;

import com.aticatac.common.objectsystem.EntityType;
import java.util.Objects;

/**
 * The type Container.
 */
public class Container {
  /**
   * The X.
   */
  protected int x;
  /**
   * The Y.
   */
  protected int y;
  /**
   * The R.
   */
  protected int r;
  /**
   * The Id.
   */
  protected final String id;

  public void setR(int r) {
    this.r = r;
  }

  /**
   * The Object type.
   */
  protected final EntityType objectType;

  /**
   * Instantiates a new Container.
   *
   * @param x          the x
   * @param y          the y
   * @param r          the r
   * @param id         the id
   * @param objectType the object type
   */
  public Container(int x, int y, int r, String id, EntityType objectType) {
    this.x = x;
    this.y = y;
    this.r = r;
    this.id = id;
    this.objectType = objectType;
  }

  /**
   * Instantiates a new Container.
   */
  public Container() {
    this.x = 1000;
    this.y = 1000;
    this.r = 0;
    this.id = "";
    this.objectType = EntityType.NONE;
  }

  /**
   * Instantiates a new Container.
   *
   * @param container the container
   */
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

  /**
   * Gets x.
   *
   * @return the x
   */
  public int getX() {
    return x;
  }

  /**
   * Gets y.
   *
   * @return the y
   */
  public int getY() {
    return y;
  }

  /**
   * Gets r.
   *
   * @return the r
   */
  public int getR() {
    return r;
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * Gets object type.
   *
   * @return the object type
   */
  public EntityType getObjectType() {
    return objectType;
  }
}
