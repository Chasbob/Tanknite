package com.aticatac.common.model;

public enum Command {
  DEFAULT(new Vector(0, 0)),
  UP(new Vector(0, 1)),
  DOWN(new Vector(0, -1)),
  LEFT(new Vector(-1, 0)),
  RIGHT(new Vector(1, 0)),
  MOVE(new Vector(0, 0)),
  SHOOT(new Vector(0, 0)),
  QUIT(new Vector(0, 0)),
  START(new Vector(0, 0)),
  FILL_AI(new Vector(0, 0)),
  FREEZE_BULLET(new Vector(0, 0)),
  BULLET_SPRAY(new Vector(0, 0)),
  PAUSE(new Vector(0, 0));
  public final Vector vector;

  Command(final Vector v) {
    vector = v;
  }

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


  public boolean isShoot() {
    switch (this) {
      case DEFAULT:
      case UP:
      case DOWN:
      case LEFT:
      case RIGHT:
      case MOVE:
      case QUIT:
      case START:
      case FILL_AI:
      case PAUSE:
        return false;
      case SHOOT:
      case FREEZE_BULLET:
      case BULLET_SPRAY:
        return true;
      default:
        return false;
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

