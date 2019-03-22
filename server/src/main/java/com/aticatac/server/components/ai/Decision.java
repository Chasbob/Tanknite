package com.aticatac.server.components.ai;

import com.aticatac.common.model.Command;

/**
 * A decision made by an AI tank.
 */
public class Decision {
  private final Command command;
  private final int angle;
  private final boolean shoot;

  /**
   * Creates a new decision: A command and angle change pair.
   *
   * @param command The command to execute
   * @param angle   The aim angle
   * @param shoot   Shooting?
   */
  public Decision(Command command, int angle, boolean shoot) {
    this.command = command;
    this.angle = angle;
    this.shoot = shoot;
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
   * Gets the aim angle.
   *
   * @return The aim angle
   */
  public int getAngle() {
    return angle;
  }

  public boolean getShoot() {
    return shoot;
  }
}
