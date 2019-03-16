package com.aticatac.server.components.physics;

import com.aticatac.server.components.transform.Position;
import com.aticatac.server.objectsystem.Entity;

public class PhysicsResponse {
  public final Entity entity;
  public final Position position;

  public PhysicsResponse(final Entity entity, final Position position) {
    this.entity = entity;
    this.position = position;
  }

  public PhysicsResponse(final Position position) {
    this(new Entity(), position);
  }

  public Entity getEntity() {
    return entity;
  }

  public Position getPosition() {
    return position;
  }

  @Override
  public String toString() {
    return "PhysicsResponse{" +
        "entity=" + entity +
        ", position=" + position +
        '}';
  }
}
