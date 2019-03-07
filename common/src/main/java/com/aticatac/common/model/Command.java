package com.aticatac.common.model;

public enum Command {
  UP, DOWN, LEFT, RIGHT, SHOOT, QUIT, START;

  public int commandToInt(Command command) {
    switch (command) {
      case UP:
        return 1;
      case DOWN:
        return 2;
      case LEFT:
        return 3;
      case RIGHT:
        return 4;
      case SHOOT:
        return 5;
    }
    return 0;
  }
}
