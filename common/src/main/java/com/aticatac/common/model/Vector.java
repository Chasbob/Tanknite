package com.aticatac.common.model;

import com.badlogic.gdx.math.MathUtils;

public class Vector {
  public final static Vector X = new Vector(1, 0);
  public final static Vector Y = new Vector(0, 1);
  public final static Vector Zero = new Vector(0, 0);
  private static final long serialVersionUID = 9222902788239530931L;
  /**
   * the x-component of this vector
   **/
  public int x;
  /**
   * the y-component of this vector
   **/
  public int y;

  /**
   * Constructs a new vector at (0,0)
   */
  public Vector() {
    set(0, 0);
  }

  /**
   * Constructs a vector with the given components
   *
   * @param x The x-component
   * @param y The y-component
   */
  public Vector(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Constructs a vector from the given vector
   *
   * @param v The vector
   */
  public Vector(Vector v) {
    set(v);
  }

  public static int len(int x, int y) {
    return (int) Math.sqrt(x * x + y * y);
  }

  public static int len2(int x, int y) {
    return x * x + y * y;
  }

  public static int dot(int x1, int y1, int x2, int y2) {
    return x1 * x2 + y1 * y2;
  }

  public static int dst(int x1, int y1, int x2, int y2) {
    final int x_d = x2 - x1;
    final int y_d = y2 - y1;
    return (int) Math.sqrt(x_d * x_d + y_d * y_d);
  }

  public static int dst2(int x1, int y1, int x2, int y2) {
    final int x_d = x2 - x1;
    final int y_d = y2 - y1;
    return x_d * x_d + y_d * y_d;
  }

  public Vector cpy() {
    return new Vector(this);
  }

  public int len() {
    return (int) Math.sqrt(x * x + y * y);
  }

  public int len2() {
    return x * x + y * y;
  }

  public Vector set(Vector v) {
    x = v.x;
    y = v.y;
    return this;
  }

  /**
   * Sets the components of this vector
   *
   * @param x The x-component
   * @param y The y-component
   *
   * @return This vector for chaining
   */
  public Vector set(int x, int y) {
    this.x = x;
    this.y = y;
    return this;
  }

  public Vector sub(Vector v) {
    x -= v.x;
    y -= v.y;
    return this;
  }

  /**
   * Substracts the other vector from this vector.
   *
   * @param x The x-component of the other vector
   * @param y The y-component of the other vector
   *
   * @return This vector for chaining
   */
  public Vector sub(int x, int y) {
    this.x -= x;
    this.y -= y;
    return this;
  }

  public Vector nor() {
    int len = len();
    if (len != 0) {
      x /= len;
      y /= len;
    }
    return this;
  }

  public Vector add(Vector v) {
    x += v.x;
    y += v.y;
    return this;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  /**
   * Adds the given components to this vector
   *
   * @param x The x-component
   * @param y The y-component
   *
   * @return This vector for chaining
   */
  public Vector add(int x, int y) {
    this.x += x;
    this.y += y;
    return this;
  }

  public int dot(Vector v) {
    return x * v.x + y * v.y;
  }

  public int dot(int ox, int oy) {
    return x * ox + y * oy;
  }

  public Vector scl(int scalar) {
    x *= scalar;
    y *= scalar;
    return this;
  }

  /**
   * @return the angle in degrees of this vector (point) relative to the x-axis. Angles are towards the positive y-axis
   * (typically counter-clockwise) and between 0 and 360.
   */
  public int angle() {
    int angle = (int) ((float) Math.atan2(y, x) * MathUtils.radiansToDegrees);
    if (angle < 0) {
      angle += 360;
    }
    return angle;
  }

  /**
   * @return the angle in radians of this vector (point) relative to the x-axis. Angles are towards the positive y-axis.
   * (typically counter-clockwise)
   */
  public int angleRad() {
    return (int) Math.atan2(y, x);
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Vector other = (Vector) obj;
    if (x != other.x) {
      return false;
    }
    return y == other.y;
  }

  /**
   * Converts this {@code Vector} to a string in the format {@code (x,y)}.
   *
   * @return a string representation of this object.
   */
  public String toString() {
    return "(" + x + "," + y + ")";
  }
}
