package com.aticatac.client.server.objectsystem.interfaces;

import com.aticatac.client.server.objectsystem.physics.CollisionBox;

/**
 * The interface Collidable.
 */
public interface Collidable {
//  CollisionBox calcCollisionBox(Position p);

  /**
   * Gets collision box.
   *
   * @return the collision box
   */
  CollisionBox getCollisionBox();

  /**
   * Intersects boolean.
   *
   * @param collidable the collidable
   * @return the boolean
   */
  boolean intersects(Collidable collidable);

  /**
   * Add to data.
   */
  void addToData();
}
