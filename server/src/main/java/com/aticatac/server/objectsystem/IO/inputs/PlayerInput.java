package com.aticatac.server.objectsystem.IO.inputs;

import com.aticatac.common.model.Command;
import com.aticatac.common.model.CommandModel;
import com.aticatac.server.objectsystem.interfaces.inputs.Input;
import java.util.ArrayList;

public class PlayerInput implements Input<PlayerInput> {
  public final ArrayList<Command> moveCommands;
  public final int bearing;
  public final boolean shoot;

  public PlayerInput(final ArrayList<Command> moveCommands, final int bearing, final boolean shoot) {
    this.moveCommands = moveCommands;
    this.bearing = bearing;
    this.shoot = shoot;
  }

  public PlayerInput(final CommandModel model) {
    this.moveCommands = new ArrayList<>();
    if (model.getCommand() == Command.SHOOT) {
      this.bearing = model.getBearing();
      this.shoot = true;
    } else {
      this.bearing = 0;
      this.shoot = false;
      this.moveCommands.add(model.getCommand());
    }
  }

  public PlayerInput(final ArrayList<Command> moveCommands) {
    this(moveCommands, 0, false);
  }

  public PlayerInput() {
    this(Command.DEFAULT);
  }

  public PlayerInput(final Command command) {
    this.moveCommands = new ArrayList<>();
    this.moveCommands.add(command);
    this.bearing = command.vector.angle();
    this.shoot = (command == Command.SHOOT);
  }

  @Override
  public PlayerInput set(final PlayerInput playerInput) {
    return null;
  }
}
