package com.aticatac.client.server.ai;

import com.aticatac.client.server.Position;
import java.util.Objects;

/**
 * The type Player state.
 */
public class PlayerState {
  /**
   * The constant none.
   */
  public static final PlayerState none = new PlayerState(new Position(0, 0), 0);
  private final int health;
  private final Position position;

  /**
   * Instantiates a new Player state.
   *
   * @param position the position
   * @param health   the health
   */
  public PlayerState(final Position position, final int health) {
    this.position = position;
    this.health = health;
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

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final PlayerState that = (PlayerState) o;
    return getHealth() == that.getHealth() &&
        getPosition().equals(that.getPosition());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getHealth(), getPosition());
  }

  /**
   * Gets health.
   *
   * @return the health
   */
  public int getHealth() {
    return health;
  }

  /**
   * Gets position.
   *
   * @return the position
   */
  public Position getPosition() {
    return position;
  }
}
