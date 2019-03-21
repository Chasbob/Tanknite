package com.aticatac.server.objectsystem.entities.powerups;

import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.components.transform.Position;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.interfaces.Collidable;

public class AmmoPowerup extends Entity implements Collidable {
  public AmmoPowerup(final String name, Position position) {
    super(name, EntityType.AMMO_POWERUP, position);
  }
  // update like changes to wall

  @Override
  public boolean intersects(final Collidable collidable) {
    return false;
  }

  @Override
  public void addToData() {
  }
}
