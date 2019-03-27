package com.aticatac.client.server.bus.event;

import com.aticatac.common.model.CommandModel;

public class PlayerInputEvent {
  public final CommandModel commandModel;

  public PlayerInputEvent(final CommandModel commandModel) {
    this.commandModel = commandModel;
  }
}
