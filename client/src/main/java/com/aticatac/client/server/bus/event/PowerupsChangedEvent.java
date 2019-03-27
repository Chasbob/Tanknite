package com.aticatac.client.server.bus.event;

import com.aticatac.common.objectsystem.Container;

public class PowerupsChangedEvent {
  private final Action action;
  private final Container container;

  @Override
  public String toString() {
    return "PowerupsChangedEvent{" +
        "action=" + action +
        ", container=" + container +
        '}';
  }

  public PowerupsChangedEvent(final Action action, final Container container) {
    this.action = action;
    this.container = container;
  }

  public Action getAction() {
    return action;
  }

  public Container getContainer() {
    return container;
  }

  public enum Action {
    ADD, REMOVE, UPDATE
  }
}
