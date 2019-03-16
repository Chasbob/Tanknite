package com.aticatac.server.objectsystem.interfaces;

import com.aticatac.server.objectsystem.physics.CollisionBox;

public interface Collidable {
//  CollisionBox calcCollisionBox(Position p);

  CollisionBox getCollisionBox();

  boolean intersects(Collidable collidable);

  void addToData();
}
