package com.aticatac.client.server.bus.event;

import com.aticatac.client.server.objectsystem.Entity;

/**
 * The type Tank collision event.
 */
public class TankCollisionEvent {
  private final Entity entity;
  private final Entity hit;

  /**
   * Instantiates a new Tank collision event.
   *
   * @param entity the entity
   * @param hit    the hit
   */
  public TankCollisionEvent(final Entity entity, final Entity hit) {
    this.entity = entity;
    this.hit = hit;
  }

  /**
   * Gets entity.
   *
   * @return the entity
   */
  public Entity getEntity() {
    return entity;
  }

  /**
   * Gets hit.
   *
   * @return the hit
   */
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
