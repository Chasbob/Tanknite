package com.aticatac.client.server.objectsystem.entities.powerups;

import com.aticatac.client.server.Position;
import com.aticatac.client.server.objectsystem.Entity;
import com.aticatac.client.server.objectsystem.interfaces.Collidable;
import com.aticatac.common.objectsystem.EntityType;

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
