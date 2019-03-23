package com.aticatac.server.objectsystem;

import com.aticatac.common.objectsystem.Container;
import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.Position;
import com.aticatac.server.objectsystem.physics.CollisionBox;
import java.security.SecureRandom;
import java.util.Objects;
import org.apache.log4j.Logger;

/**
 * The type Entity.
 */
public class Entity {
  /**
   * The constant empty.
   */
  public static final Entity empty = new Entity(EntityType.NONE);
  /**
   * The constant bullet.
   */
  public static final Entity bullet = new Entity(EntityType.BULLET);
  /**
   * The constant outOfBounds.
   */
  public static final Entity outOfBounds = new Entity(EntityType.OUTOFBOUNDS);
  protected final Logger logger;
  private final String name;
  private final EntityType type;
  private Position position;
  private CollisionBox collisionBox;
  private int rotation;

  /**
   * Instantiates a new Entity.
   *
   * @param name     the name
   * @param type     the type
   * @param position the position
   */
  public Entity(final String name, final EntityType type, final Position position) {
    logger = Logger.getLogger(getClass());
    this.name = name;
    this.type = type;
    this.setPosition(position);
    this.setCollisionBox(new CollisionBox(this.getPosition(), type.radius));
  }

  /**
   * Instantiates a new Entity.
   *
   * @param name the name
   * @param type the type
   */
  public Entity(final String name, final EntityType type) {
    this(name, type, Position.zero);
  }

  /**
   * Instantiates a new Entity.
   *
   * @param type the type
   */
  public Entity(final EntityType type) {
    this("", type);
  }

  /**
   * Instantiates a new Entity.
   *
   * @param type the type
   * @param p    the p
   */
  public Entity(final EntityType type, final Position p) {
    this("", type, p);
  }

  /**
   * Instantiates a new Entity.
   */
  public Entity() {
    this(EntityType.NONE);
  }

  /**
   * Random power up entity . entity type.
   *
   * @return the entity . entity type
   */
  public static EntityType randomPowerUP() {
    SecureRandom random = new SecureRandom();
    EntityType entityType = EntityType.NONE;
    while (!entityType.isPowerUp()) {
      int x = random.nextInt((EntityType.class).getEnumConstants().length);
      entityType = (EntityType.class).getEnumConstants()[x];
    }
    return entityType;
  }

  /**
   * Gets rotation.
   *
   * @return the rotation
   */
  public int getRotation() {
    return rotation;
  }

  /**
   * Sets rotation.
   *
   * @param rotation the rotation
   */
  public void setRotation(final int rotation) {
    this.rotation = rotation;
  }

  /**
   * Gets collision box.
   *
   * @return the collision box
   */
  public CollisionBox getCollisionBox() {
    return collisionBox;
  }

  private void setCollisionBox(CollisionBox collisionBox) {
    this.collisionBox = collisionBox;
  }

  /**
   * Gets container.
   *
   * @return the container
   */
  public Container getContainer() {
    return new Container(getPosition().getX(), getPosition().getY(), 0, 0, 0, getName(), getType());
  }

  /**
   * Gets x.
   *
   * @return the x
   */
  public int getX() {
    return getPosition().getX();
  }

  /**
   * Gets y.
   *
   * @return the y
   */
  public int getY() {
    return getPosition().getY();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getType());
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Entity entity = (Entity) o;
    return getName().equals(entity.getName()) &&
        getType() == entity.getType() &&
        getPosition().equals(entity.getPosition());
  }

  /**
   * The Name.
   */
  @Override
  public String toString() {
    return "Entity{" +
        "name='" + getName() + '\'' +
        ", type=" + getType() +
        '}';
  }
  /**
   * The Type.
   */
  /**
   * Gets name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets type.
   *
   * @return the type
   */
  public EntityType getType() {
    return type;
  }

  /**
   * Gets position.
   *
   * @return the position
   */
  public Position getPosition() {
    return position;
  }

  /**
   * Sets position.
   *
   * @param position the position
   */
  public void setPosition(Position position) {
    this.position = position;
  }

  /**
   * Sets position.
   *
   * @param newPosition the new position
   * @param collidable  the collidable
   */
  protected void setPosition(Position newPosition, boolean collidable) {
    if (collidable) {
      DataServer.INSTANCE.removeBoxFromData(getCollisionBox());
      this.getPosition().set(newPosition);
      this.getCollisionBox().setPosition(this.getPosition());
      DataServer.INSTANCE.addBoxToData(getCollisionBox(), this);
    } else {
      this.getPosition().set(newPosition);
      this.getCollisionBox().setPosition(this.getPosition());
    }
  }
  /**
   * The enum Entity type.
   */
}
