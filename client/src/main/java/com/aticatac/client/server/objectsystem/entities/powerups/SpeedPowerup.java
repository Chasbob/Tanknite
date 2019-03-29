package com.aticatac.client.server.objectsystem.entities.powerups;

import com.aticatac.client.server.Position;
import com.aticatac.client.server.objectsystem.Entity;
import com.aticatac.client.server.objectsystem.interfaces.Collidable;
import com.aticatac.common.objectsystem.EntityType;

/**
 * The type Speed powerup.
 */
public class SpeedPowerup extends Entity implements Collidable {
  /**
   * Instantiates a new Speed powerup.
   *
   * @param name     the name
   * @param position the position
   */
  public SpeedPowerup(final String name, Position position) {
    super(name, EntityType.SPEED_POWERUP, position);
  }


  @Override
  public boolean intersects(final Collidable collidable) {
    return false;
  }

  @Override
  public void addToData() {
  }
}
