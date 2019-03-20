package com.aticatac.server.bus.event;

import com.aticatac.server.objectsystem.Entity;

public class TankCollisionEvent {
  private final Entity entity;
  private final Entity hit;

  public TankCollisionEvent(final Entity entity, final Entity hit) {
    this.entity = entity;
    this.hit = hit;
  }

  public Entity getEntity() {
    return entity;
  }

  public Entity getHit() {
    return hit;
  }

  @Override
  public String toString() {
    return "TankCollisionEvent{" +
        "entity=" + entity +
        ", hit=" + hit +
        '}';
  }
}
