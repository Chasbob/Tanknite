package com.aticatac.client.server.ai;

import com.aticatac.client.server.Position;
import com.aticatac.common.objectsystem.EntityType;

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

  public EntityType getType() {
    return type;
  }

  public int getX() {
    return position.getX();
  }

  public int getY() {
    return position.getY();
  }
}
