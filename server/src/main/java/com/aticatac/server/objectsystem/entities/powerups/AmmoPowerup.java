package com.aticatac.server.objectsystem.entities.powerups;

import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.interfaces.Collidable;
import com.aticatac.server.objectsystem.physics.CollisionBox;

public class AmmoPowerup extends Entity implements Collidable {
  protected AmmoPowerup(final String name) {
//    super(name);
  }

  // update like changes to wall
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
