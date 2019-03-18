package com.aticatac.server.objectsystem.entities.powerups;

import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.interfaces.Collidable;
import com.aticatac.server.objectsystem.physics.CollisionBox;

public class HealthPowerup extends Entity implements Collidable {
  protected HealthPowerup(final String name) {
//    super(name);
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
