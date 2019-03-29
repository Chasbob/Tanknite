package com.aticatac.client.server.bus.event;

import com.aticatac.common.objectsystem.containers.PlayerContainer;

/**
 * The type Players changed event.
 */
public class PlayersChangedEvent {
  /**
   * The Action.
   */
  public final Action action;
  private final PlayerContainer container;

  /**
   * Instantiates a new Players changed event.
   *
   * @param action    the action
   * @param container the container
   */
  public PlayersChangedEvent(final Action action, final PlayerContainer container) {
    this.action = action;
    this.container = container;
  }

  @Override
  public String toString() {
    return "PlayersChangedEvent{"
        + "action="
        + action
        + ", container="
        + container
        + '}';
  }

  /**
   * Gets player container.
   *
   * @return the player container
   */
  public PlayerContainer getPlayerContainer() {
    return container;
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
