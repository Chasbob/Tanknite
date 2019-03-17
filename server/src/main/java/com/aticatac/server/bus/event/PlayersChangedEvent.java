package com.aticatac.server.bus.event;

import com.aticatac.common.objectsystem.Container;

public class PlayersChangedEvent {
  public final Action action;
  private final Container container;
  public PlayersChangedEvent(final Action action, final Container container) {
    this.action = action;
    this.container = container;
  }



  public Container getContainer() {
    return container;
  }

  public enum Action {
    ADD, REMOVE, UPDATE;
  }
}
