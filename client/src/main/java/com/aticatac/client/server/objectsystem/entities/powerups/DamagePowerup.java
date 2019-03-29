package com.aticatac.client.server.objectsystem.entities.powerups;

import com.aticatac.client.server.Position;
import com.aticatac.client.server.objectsystem.Entity;
import com.aticatac.client.server.objectsystem.interfaces.Collidable;
import com.aticatac.common.objectsystem.EntityType;

/**
 * The type Damage powerup.
 */
public class DamagePowerup extends Entity implements Collidable {
  /**
   * Instantiates a new Damage powerup.
   *
   * @param name     the name
   * @param position the position
   */
  public DamagePowerup(final String name, Position position) {
    super(name,EntityType.DAMAGE_POWERUP,position);
  }


  @Override
  public boolean intersects(final Collidable collidable) {
    return false;
  }

  @Override
  public void addToData() {
  }
}