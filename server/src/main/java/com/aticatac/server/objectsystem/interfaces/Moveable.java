package com.aticatac.server.objectsystem.interfaces;

import com.aticatac.server.objectsystem.Entity;

/**
 * The interface Moveable.
 */
public interface Moveable {
  /**
   * Move entity.
   *
   * @return the entity collide d with
   */
  Entity move();
}
