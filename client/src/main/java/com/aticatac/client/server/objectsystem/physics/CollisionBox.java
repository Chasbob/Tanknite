package com.aticatac.client.server.objectsystem.physics;

import com.aticatac.client.server.Position;
import com.aticatac.common.objectsystem.EntityType;
import java.util.HashSet;
import java.util.Objects;
import org.apache.log4j.Logger;

/**
 * The type Collision box.
 */
public class CollisionBox {

  /***/
  private final int radius;
  /***/
  private final Logger logger;
  /***/
  private Position position;
  /***/
  private HashSet<Position> box;

  /**
   * Instantiates a new Collision box.
   *
   * @param position the position
   * @param radius   the radius
   */
  public CollisionBox(final Position position, final int radius) {
    this.position = position;
    this.radius = radius;
    this.box = calcCollisionBox(this.position);
    this.logger = Logger.getLogger(getClass());
  }

  /**
   * Instantiates a new Collision box.
   *
   * @param position the position
   * @param type     the type
   */
  public CollisionBox(final Position position, final EntityType type) {
    this(position, type.radius);
  }


  /**
   *
   * @param p
   * @return
   */
  private HashSet<Position> calcCollisionBox(final Position p) {
    HashSet<Position> out = new HashSet<>();
    int lowerY = p.getY() - radius;
    int lowerX = p.getX() - radius;
    //positions for bottom of out
    final int limit = radius * 2 + 1;
    calcXoffset(out, lowerY, lowerX, limit);
    //positions for left of out
    calcYoffset(out, lowerY, lowerX, limit);
    int higherY = p.getY() + radius;
    int higherX = p.getX() + radius;
    //positions for top of out
    calcXoffset(out, higherY, lowerX, limit);
    //positions for right of out
    calcYoffset(out, lowerY, higherX, limit);
    return out;
  }

  /**
   *
   * @param out
   * @param lowerY
   * @param lowerX
   * @param limit
   */
  private void calcYoffset(final HashSet<Position> out, final int lowerY, final int lowerX, final int limit) {
    for (int y = 0; y < limit; y++) {
      Position position = new Position(lowerX, lowerY + y);
      out.add(position);
    }
  }

  /**
   *
   * @param out
   * @param lowerY
   * @param lowerX
   * @param limit
   */
  private void calcXoffset(final HashSet<Position> out, final int lowerY, final int lowerX, final int limit) {
    for (int x = 0; x < limit; x++) {
      Position position = new Position(lowerX + x, lowerY);
      out.add(position);
    }
  }

  /**
   * Gets box.
   *
   * @return the box
   */
  public HashSet<Position> getBox() {
    return box;
  }

  /**
   *
   * @return
   */
  @Override
  public int hashCode() {
    return Objects.hash(radius, position, getBox());
  }

  /**
   *
   * @param o
   * @return
   */
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final CollisionBox that = (CollisionBox) o;
    return radius == that.radius &&
        position.equals(that.position) &&
        getBox().equals(that.getBox());
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
   * Sets position.
   *
   * @param p the p
   */
  public void setPosition(Position p) {
    this.position = p;
    box = calcCollisionBox(position);
  }
}
