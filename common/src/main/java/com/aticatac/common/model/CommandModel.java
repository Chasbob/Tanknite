package com.aticatac.common.model;

/**
 * The type Command model.
 */
public class CommandModel extends Model {
  private final Command command;

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
   * Gets command.
   *
   * @return the command
   */
  public Command getCommand() {
    return command;
  }
}
