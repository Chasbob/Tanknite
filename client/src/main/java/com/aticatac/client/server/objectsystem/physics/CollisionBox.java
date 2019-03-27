package com.aticatac.client.server.objectsystem.physics;

import com.aticatac.client.server.Position;
import com.aticatac.common.objectsystem.EntityType;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Objects;

/**
 * The type Collision box.
 */
public class CollisionBox {

  /**Radius of the entity*/
  private final int radius;
  /**logger*/
  private final Logger logger;
  /**Position of the entity*/
  private Position position;
  /**Set of positions making up the collision box of this entity*/
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
   * Calculates all the positions for a box around a given position
   * @param p the position that needs a box creating around it
   * @return a set of positions representing a box around a position
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
   * Calculates the offset for Y
   * @param out the set of positions that represent a box
   * @param lowerY the lowest Y value for the box
   * @param lowerX the lowest X value for the box
   * @param limit The limit of the box size
   */
  private void calcYoffset(final HashSet<Position> out, final int lowerY, final int lowerX, final int limit) {
    for (int y = 0; y < limit; y++) {
      Position position = new Position(lowerX, lowerY + y);
      out.add(position);
    }
  }

  /**
   * Calculates the offset for X
   * @param out the set of positions that represent a box
   * @param lowerY The lowest Y value for the box
   * @param lowerX The lowest X value for the box
   * @param limit The limit of the box size
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
   * Creates the hashcode for a collisionbox
   * @return the integer hashcode
   */
  @Override
  public int hashCode() {
    return Objects.hash(radius, position, getBox());
  }

  /**
   * Compares the X and Y values of the positions of a collision box to determine equality
   * @param o the object being compared
   * @return true if the objects are equal, else false.
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
    return radius == that.radius && position.equals(that.position) && getBox().equals(that.getBox());
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
