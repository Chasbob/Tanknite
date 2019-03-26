package com.aticatac.client.server.objectsystem.physics;

import com.aticatac.client.server.Position;
import com.aticatac.client.server.objectsystem.Entity;
import java.util.HashSet;

public class PhysicsResponse {
  private final HashSet<Entity> collisions;
  private final Position position;

  public HashSet<Entity> getCollisions() {
    return collisions;
  }

  public Position getPosition() {
    return position;
  }

  public PhysicsResponse(final HashSet<Entity> collisions, final Position position) {
    this.collisions = collisions;
    this.position = position;
  }
}
