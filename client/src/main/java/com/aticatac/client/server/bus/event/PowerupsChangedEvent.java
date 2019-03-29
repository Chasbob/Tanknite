package com.aticatac.client.server.bus.event;

import com.aticatac.common.objectsystem.containers.Container;

public class PowerupsChangedEvent {
  private final Action action;
  private final Container container;
  private final Container consumer;

  public PowerupsChangedEvent(final Action action, final Container container) {
    this.action = action;
    this.container = container;
    consumer = new Container();
  }

  public PowerupsChangedEvent(final Action action, final Container container, final Container consumer) {
    this.action = action;
    this.container = container;
    this.consumer = consumer;
  }

  @Override
  public String toString() {
    return "PowerupsChangedEvent{" +
        "action=" + action +
        ", container=" + container +
        '}';
  }

  public Action getAction() {
    return action;
  }

  public Container getConsumer() {
    return consumer;
  }

  public Container getContainer() {
    return container;
  }

  public enum Action {
    ADD, REMOVE, UPDATE
  }
}
