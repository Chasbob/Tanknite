package com.aticatac.server.bus.event;

import com.aticatac.common.objectsystem.Container;

public class BulletsChangedEvent {
  private final Action action;
  private final Container bullet;

  public BulletsChangedEvent(final Action action, final Container bullet) {
    this.action = action;
    this.bullet = bullet;
  }

  public Action getAction() {
    return action;
  }

  public Container getBullet() {
    return bullet;
  }

  public enum Action {
    ADD, REMOVE, UPDATE;
  }
}
