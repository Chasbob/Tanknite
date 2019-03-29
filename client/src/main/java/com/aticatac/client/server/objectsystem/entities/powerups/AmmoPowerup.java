package com.aticatac.client.server.objectsystem.entities.powerups;

import com.aticatac.client.server.Position;
import com.aticatac.client.server.objectsystem.Entity;
import com.aticatac.client.server.objectsystem.interfaces.Collidable;
import com.aticatac.common.objectsystem.EntityType;

/**
 * The type Ammo powerup.
 */
public class AmmoPowerup extends Entity implements Collidable {
  /**
   * Instantiates a new Ammo powerup.
   *
   * @param name     the name
   * @param position the position
   */
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
