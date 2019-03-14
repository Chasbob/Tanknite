package com.aticatac.common.model;

/**
 * The type Command model.
 */
public class CommandModel extends Model {
  private final Command command;
  private int bearing;

  /**
   * Instantiates a new Model.
   *
   * @param id      the id
   * @param command the command
   */
  public CommandModel(String id, Command command) {
    super(id);
    this.command = command;
  }

  /**
   * Gets bearing.
   *
   * @return the bearing
   */
  public int getBearing() {
    return bearing;
  }

  /**
   * Sets bearing.
   *
   * @param bearing the bearing
   */
  public void setBearing(int bearing) {
    this.bearing = bearing;
  }

  /**
   * Gets command.
   *
   * @return the command
   */
  public Command getCommand() {
    return command;
  }

  @Override
  public String toString() {
    return "CommandModel{" +
        "command=" + command +
        '}';
  }
}
