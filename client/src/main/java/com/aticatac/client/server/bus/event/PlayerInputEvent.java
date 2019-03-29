package com.aticatac.client.server.bus.event;

import com.aticatac.common.model.CommandModel;

/**
 * The type Player input event.
 */
public class PlayerInputEvent {
  /**
   * The Command model.
   */
  public final CommandModel commandModel;

  /**
   * Instantiates a new Player input event.
   *
   * @param commandModel the command model
   */
  public PlayerInputEvent(final CommandModel commandModel) {
    this.commandModel = commandModel;
  }
}
