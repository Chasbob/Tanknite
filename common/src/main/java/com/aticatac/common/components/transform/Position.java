package com.aticatac.common.components.transform;

/**
 * The type Position.
 */
public class Position {
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

  @Override
  public String toString() {
    return "Position{" +
    "x=" + x +
    ", y=" + y +
    '}';
  }
}
