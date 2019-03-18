package com.aticatac.server.components.ai;

import com.aticatac.server.components.transform.Position;
import java.util.Objects;

public class PlayerState {
  public static final PlayerState none = new PlayerState(new Position(0, 0), 0);
  public final int health;
  private final Position position;

  public PlayerState(final Position position, final int health) {
    this.position = position;
    this.health = health;
  }

  public int getX() {
    return position.getX();
  }

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

  public int getHealth() {
    return health;
  }

  public Position getPosition() {
    return position;
  }
}
