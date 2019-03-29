package com.aticatac.client.server.ai;

import com.aticatac.common.model.Command;

/**
 * A decision made by an AI tank.
 */
public class Decision {
  private final Command command;
  private final int angle;
  private final ShootType shoot;

  /**
   * Creates a new decision: A command and angle change pair.
   *
   * @param command The command to execute
   * @param angle   The aim angle
   * @param shoot   Shooting?
   */
  public Decision(Command command, int angle, ShootType shoot) {
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

  /**
   * Gets shoot.
   *
   * @return the shoot
   */
  public ShootType getShoot() {
    return shoot;
  }

  /**
   * The enum Shoot type.
   */
  public enum ShootType {
    /**
     * None shoot type.
     */
    NONE,
    /**
     * Normal shoot type.
     */
    NORMAL,
    /**
     * Freeze shoot type.
     */
    FREEZE,
    /**
     * Spray shoot type.
     */
    SPRAY
  }
}
