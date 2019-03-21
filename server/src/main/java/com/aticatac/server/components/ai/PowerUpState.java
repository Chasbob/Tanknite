package com.aticatac.server.components.ai;

import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.components.transform.Position;
import com.aticatac.server.objectsystem.Entity;

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

  public Entity.EntityType getType() {
    return type;
  }

  public int getX() {
    return position.getX();
  }

  public int getY() {
    return position.getY();
  }
}
