package com.aticatac.server.objectsystem.entities.powerups;

import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.components.transform.Position;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.interfaces.Collidable;
import com.aticatac.server.objectsystem.physics.CollisionBox;

public class SpeedPowerup extends Entity implements Collidable {
  public SpeedPowerup(final String name, Position position) {
    super(EntityType.SPEED_POWERUP);
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
