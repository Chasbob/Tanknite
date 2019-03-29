package com.aticatac.client.server.ai;

import com.aticatac.client.server.Position;
import com.aticatac.common.objectsystem.EntityType;

/**
 * The type Power up state.
 */
public class PowerUpState {
  /**
   * The Type.
   */
  public final EntityType type;
  private final Position position;

  /**
   * Instantiates a new Power up state.
   *
   * @param type     the type
   * @param position the position
   */
  public PowerUpState(final EntityType type, final Position position) {
    this.type = type;
    this.position = position;
  }

  /**
   * Gets position.
   *
   * @return the position
   */
  public Position getPosition() {
    return position;
  }

  /**
   * Gets type.
   *
   * @return the type
   */
  public EntityType getType() {
    return type;
  }

  /**
   * Gets x.
   *
   * @return the x
   */
  public int getX() {
    return position.getX();
  }

  /**
   * Gets y.
   *
   * @return the y
   */
  public int getY() {
    return position.getY();
  }
}
