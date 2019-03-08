package com.aticatac.server.components.ai;

import com.aticatac.common.model.Command;

/**
 * A decision made by an AI tank.
 */
public class Decision {
  private final Command command;
  private final int angleChange;

  /**
   * Creates a new decision: A command and angle change pair.
   *
   * @param command     Command to execute
   * @param angleChange Angle change to apply
   */
  public Decision(Command command, int angleChange) {
    this.command = command;
    this.angleChange = angleChange;
  }

  /**
   * Gets the command to execute.
   *
   * @return Command to execute
   */
  public Command getCommand() {
    return command;
  }

  /**
   * Gets the angle change to apply.
   *
   * @return Angle change to apply
   */
  public int getAngleChange() {
    return angleChange;
  }
}
