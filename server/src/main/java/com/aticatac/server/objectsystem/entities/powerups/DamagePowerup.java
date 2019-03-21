package com.aticatac.server.objectsystem.entities.powerups;


import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.components.transform.Position;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.interfaces.Collidable;
import com.aticatac.server.objectsystem.physics.CollisionBox;

public class DamagePowerup extends Entity implements Collidable {
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