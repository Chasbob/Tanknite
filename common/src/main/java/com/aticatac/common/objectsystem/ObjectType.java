package com.aticatac.common.objectsystem;

public enum ObjectType {
  TANK(14), BULLET(2), OTHER, PLAYER_CONTAINER, ROOT;
  public final int radius;

  ObjectType() {
    radius = 0;
  }

  ObjectType(final int i) {
    radius = i;
  }

  public boolean isProjectile() {
    switch (this) {
      case BULLET:
        return true;
      default:
        return false;
    }
  }
}
