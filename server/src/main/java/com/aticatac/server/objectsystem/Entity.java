package com.aticatac.server.objectsystem;

import com.aticatac.common.objectsystem.Container;
import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.bus.EventBusFactory;
import com.aticatac.server.bus.event.PlayersChangedEvent;
import com.aticatac.server.transform.Position;
import com.aticatac.server.objectsystem.physics.CollisionBox;
import java.security.SecureRandom;
import java.util.Objects;

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
  /**
   * The Name.
   */
  public final String name;
  /**
   * The Type.
   */
  public final EntityType type;
  protected Position position;
  protected CollisionBox collisionBox;

  /**
   * Instantiates a new Entity.
   *
   * @param name     the name
   * @param type     the type
   * @param position the position
   */
  public Entity(final String name, final EntityType type, final Position position) {
    this.name = name;
    this.type = type;
    this.position = position;
    this.collisionBox = new CollisionBox(this.position, type.radius);
  }

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

  public CollisionBox getCollisionBox() {
    return collisionBox;
  }

  public Entity getBaseEntity() {
    return new Entity(name, type, position);
  }

  public Container getContainer() {
    return new Container(position.getX(), position.getY(), 0, 0, 0, name, type);
  }

  /**
   * Gets x.
   *
   * @return the x
   */
  public int getX() {
    return position.getX();
  }

  /**
   * Gets y.
   *
   * @return the y
   */
  public int getY() {
    return position.getY();
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

  @Override
  public String toString() {
    return "Entity{" +
        "name='" + name + '\'' +
        ", type=" + type +
        '}';
  }

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

  public Position getPosition() {
    return position;
  }

  protected void setPosition(Position newPosition) {
    DataServer.INSTANCE.removeBoxFromData(collisionBox);
    this.position.set(newPosition);
    this.collisionBox.setPosition(this.position);
    DataServer.INSTANCE.addBoxToData(collisionBox, getBaseEntity());
    EventBusFactory.getEventBus().post(new PlayersChangedEvent(PlayersChangedEvent.Action.UPDATE,getContainer()));
  }
  /**
   * The enum Entity type.
   */
}
