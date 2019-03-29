package com.aticatac.client.server.bus.event;

import com.aticatac.common.objectsystem.containers.Container;

/**
 * The type Bullets changed event.
 */
public class BulletsChangedEvent {
  private final Action action;
  private final Container bullet;

  /**
   * Instantiates a new Bullets changed event.
   *
   * @param action the action
   * @param bullet the bullet
   */
  public BulletsChangedEvent(final Action action, final Container bullet) {
    this.action = action;
    this.bullet = bullet;
  }

  /**
   * Gets action.
   *
   * @return the action
   */
  public Action getAction() {
    return action;
  }

  /**
   * Gets bullet.
   *
   * @return the bullet
   */
  public Container getBullet() {
    return bullet;
  }

  /**
   * The enum Action.
   */
  public enum Action {
    /**
     * Add action.
     */
    ADD,
    /**
     * Remove action.
     */
    REMOVE,
    /**
     * Update action.
     */
    UPDATE
  }
}
