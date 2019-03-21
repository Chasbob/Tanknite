package com.aticatac.server.ai;

import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.transform.Position;

public class PowerUpState {
  public final EntityType type;
  private final Position position;

  public PowerUpState(final EntityType type, final Position position) {
    this.type = type;
    this.position = position;
  }

  public Position getPosition() {
    return position;
  }

  public int getX() {
    return position.getX();
  }

  public int getY() {
    return position.getY();
  }
}
