package com.aticatac.common.model;

public enum Command {
  UP, DOWN, LEFT, RIGHT, MOVE, SHOOT, QUIT, START, FILL_AI;

  public int getAngle() {
    switch (this) {
      case UP:
        return 90;
      case DOWN:
        return 270;
      case LEFT:
        return 180;
      case RIGHT:
        return 0;
      default:
        return 0;
    }
  }

  public boolean isMovement() {
    switch (this) {
      case UP:
        return true;
      case DOWN:
        return true;
      case LEFT:
        return true;
      case RIGHT:
        return true;
      case MOVE:
        return true;
      default:
        return false;
    }
  }
}
