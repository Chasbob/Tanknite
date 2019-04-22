package com.aticatac.common.model;

import com.badlogic.gdx.math.MathUtils;

public class VectorF {
  public final static VectorF X = new VectorF(1, 0);
  public final static VectorF Y = new VectorF(0, 1);
  public final static VectorF Zero = new VectorF(0, 0);
  private static final long serialVersionUID = 9222902788239530931L;
  /**
   * the x-component of this VectorF
   **/
  public float x;
  /**
   * the y-component of this VectorF
   **/
  public float y;

  /**
   * Constructs a new VectorF at (0,0)
   */
  public VectorF() {
    set(0, 0);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  /**
   * Constructs a VectorF with the given components
   *
   * @param x The x-component
   * @param y The y-component
   */
  public VectorF(float x, float y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Constructs a VectorF from the given VectorF
   *
   * @param v The VectorF
   */
  public VectorF(VectorF v) {
    set(v);
  }

  public static float len(float x, float y) {
    return (float) Math.sqrt(x * x + y * y);
  }

  public static float len2(float x, float y) {
    return x * x + y * y;
  }

  public static float dot(float x1, float y1, float x2, float y2) {
    return x1 * x2 + y1 * y2;
  }

  public static float dst(float x1, float y1, float x2, float y2) {
    final float x_d = x2 - x1;
    final float y_d = y2 - y1;
    return (float) Math.sqrt(x_d * x_d + y_d * y_d);
  }

  public static float dst2(float x1, float y1, float x2, float y2) {
    final float x_d = x2 - x1;
    final float y_d = y2 - y1;
    return x_d * x_d + y_d * y_d;
  }

  public VectorF cpy() {
    return new VectorF(this);
  }

  public float len() {
    return (float) Math.sqrt(x * x + y * y);
  }

  public float len2() {
    return x * x + y * y;
  }

  public VectorF set(VectorF v) {
    x = v.x;
    y = v.y;
    return this;
  }

  /**
   * Sets the components of this VectorF
   *
   * @param x The x-component
   * @param y The y-component
   *
   * @return This VectorF for chaining
   */
  public VectorF set(float x, float y) {
    this.x = x;
    this.y = y;
    return this;
  }

  public VectorF sub(VectorF v) {
    x -= v.x;
    y -= v.y;
    return this;
  }

  /**
   * Substracts the other VectorF from this VectorF.
   *
   * @param x The x-component of the other VectorF
   * @param y The y-component of the other VectorF
   *
   * @return This VectorF for chaining
   */
  public VectorF sub(float x, float y) {
    this.x -= x;
    this.y -= y;
    return this;
  }

  public VectorF nor() {
    float len = len();
    if (len != 0) {
      x /= len;
      y /= len;
    }
    return this;
  }

  public VectorF add(VectorF v) {
    x += v.x;
    y += v.y;
    return this;
  }

  /**
   * Adds the given components to this VectorF
   *
   * @param x The x-component
   * @param y The y-component
   *
   * @return This VectorF for chaining
   */
  public VectorF add(float x, float y) {
    this.x += x;
    this.y += y;
    return this;
  }

  public float dot(VectorF v) {
    return x * v.x + y * v.y;
  }

  public float dot(float ox, float oy) {
    return x * ox + y * oy;
  }

  public VectorF scl(float scalar) {
    x *= scalar;
    y *= scalar;
    return this;
  }

  /**
   * @return the angle in degrees of this VectorF (point) relative to the x-axis. Angles are towards the positive y-axis
   * (typically counter-clockwise) and between 0 and 360.
   */
  public float angle() {
    float angle = (float) ((float) Math.atan2(y, x) * MathUtils.radiansToDegrees);
    if (angle < 0) {
      angle += 360;
    }
    return angle;
  }

  /**
   * @return the angle in radians of this VectorF (point) relative to the x-axis. Angles are towards the positive y-axis.
   * (typically counter-clockwise)
   */
  public float angleRad() {
    return (float) Math.atan2(y, x);
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
    VectorF other = (VectorF) obj;
    if (x != other.x) {
      return false;
    }
    return y == other.y;
  }

  /**
   * Converts this {@code VectorF} to a string in the format {@code (x,y)}.
   *
   * @return a string representation of this object.
   */
  public String toString() {
    return "(" + x + "," + y + ")";
  }
}
