package com.aticatac.client.server;
//import com.aticatac.server.components.DataServer;

/**
 * The type Position.
 */
public class Position {
  /**
   * The constant zero.
   */
  public static final Position zero = new Position(0, 0);
  /**
   * The x and y coordinates for the position
   */
  private int x, y;

  /**
   * construct for a position where the values are not yet set
   */
  public Position() {
    this(0, 0);
  }

  /**
   * construct for a position where the values are known
   *
   * @param xs the X value
   * @param ys the Y value
   */
  public Position(int xs, int ys) {
    x = xs;
    y = ys;
  }

  /**
   * Set.
   *
   * @param p the p
   */
  public void set(Position p) {
    x = p.x;
    y = p.y;
  }

  /**
   * Set.
   *
   * @param x the x
   * @param y the y
   */
  public void set(final int x, final int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Gets the x value for the position
   *
   * @return the X value
   */
  public int getX() {
    return x;
  }

  /**
   * Sets x.
   *
   * @param x the x
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * Gets the Y value for the position
   *
   * @return the Y value
   */
  public int getY() {
    return y;
  }

  /**
   * Sets y.
   *
   * @param y the y
   */
  public void setY(int y) {
    this.y = y;
  }

  /**
   * Copy position.
   *
   * @return the position
   */
  public Position copy() {
    return new Position(x, y);
  }

  @Override
  public int hashCode() {
    int result = 0;
    result = x + y;
    return result;
  }

  @Override
  public boolean equals(Object o) {
    // null check
    if (o == null) {
      return false;
    }
    // this instance check
    if (this == o) {
      return true;
    }
    // instanceof Check and actual value check
    return (o instanceof Position) && (((Position) o).getX() == this.x) && (((Position) o).getY() == this.y);
  }

  @Override
  public String toString() {
    return "Position{" +
        "x=" + x +
        ", y=" + y +
        '}';
  }
}


