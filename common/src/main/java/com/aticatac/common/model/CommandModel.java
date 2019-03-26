package com.aticatac.common.model;

/**
 * The type Command model.
 */
public class CommandModel extends Model {
  private Command command;
  private int bearing;
  private Vector vector;

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

  public void setCommand(final Command command) {
    this.command = command;
  }

  public CommandModel(final String id) {
    super(id);
  }

  public Vector getVector() {
    return vector;
  }

  public void setVector(final Vector vector) {
    this.vector = vector;
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

  public void reset() {
    this.command = Command.DEFAULT;
    bearing = 0;
//    vector = Vector.Zero.cpy();
  }

  @Override
  public String toString() {
    return "CommandModel{" +
        "command=" + command +
        '}';
  }
}
