package com.aticatac.client.server.bus.event;

import com.aticatac.common.objectsystem.containers.PlayerContainer;

public class PlayersChangedEvent {
  public final Action action;
  private final PlayerContainer container;

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

  public PlayerContainer getPlayerContainer() {
    return container;
  }

  public enum Action {
    ADD, REMOVE, UPDATE
  }
}
