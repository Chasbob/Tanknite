package com.aticatac.server.objectsystem.entities.powerups;

import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.components.transform.Position;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.interfaces.Collidable;
import com.aticatac.server.objectsystem.physics.CollisionBox;

public class HealthPowerup extends Entity implements Collidable {
  private final int health;

  public HealthPowerup(final String name, Position position) {
    super(EntityType.HEALTH_POWERUP);
    health = 100;
  }

  @Override
  public String toString() {
    return "HealthPowerup{" +
        "health=" + health +
        ", name='" + name + '\'' +
        ", type=" + type +
        '}';
  }

  @Override
  public CollisionBox getCollisionBox() {
    return null;
  }

  @Override
  public boolean intersects(final Collidable collidable) {
    return false;
  }

  @Override
  public void addToData() {
  }
}
