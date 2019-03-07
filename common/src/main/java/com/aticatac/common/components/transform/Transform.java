package com.aticatac.common.components.transform;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;

/**
 * The type Transform.
 */
public class Transform extends Component {
  private Position position;
  private double rotation = 0;

  /**
   * Instantiates a new Component.
   *
   * @param gameObject the component parent
   */
  public Transform(GameObject gameObject) {
    super(gameObject);
    this.position = new Position(0, 0);
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
   * @param transform the transform
   */
  public void setPosition(Transform transform) {
    this.setPosition(transform.getRelativeX(), transform.getRelativeY());
  }
//    public void setPosition(double x, double y) {
//        position = new Position(x, y);
//    }

  /**
   * Gets x.
   *
   * @return the x
   */
  public double getX() {
    if (this.getGameObject().hasParent()) {
      GameObject parent = this.getGameObject().getParent();
      if (parent.componentExists(Transform.class)) {
        Transform transform = parent.getTransform();
        double x = transform.getX();
        return x + this.position.getX();
      }
    }
//        else {
//            return getRelativeX();
//        }
    return getRelativeX();
  }

  /**
   * Gets relative x.
   *
   * @return the relative x
   */
  public double getRelativeX() {
    return this.position.getX();
  }

  /**
   * Gets relative y.
   *
   * @return the relative y
   */
  public double getRelativeY() {
    return this.position.getY();
  }

  /**
   * Gets y.
   *
   * @return the y
   */
  public double getY() {
    if (this.getGameObject().hasParent()) {
      return this.getGameObject().getParent().getTransform().getY() + this.position.getY();
    } else {
      return getRelativeY();
    }
  }

  /**
   * Transform.
   *
   * @param x the x
   * @param y the y
   */
  public void transform(double x, double y) {
    this.setPosition(this.getRelativeX() + x, this.getRelativeY() + y);
  }

  /**
   * Sets position.
   *
   * @param x the x
   * @param y the y
   */
  public void setPosition(double x, double y) {
    position.setX(x);
    position.setY(y);
  }

  /**
   * Gets rotation.
   *
   * @return the rotation
   */
  public double getRotation() {
    return rotation;
  }

  /**
   * Sets rotation.d
   *
   * @param r the r
   */
  public void setRotation(double r) {
    rotation = r;
  }

  @Override
  public String toString() {
    return "setPosition{"
    +
    "position=" + position
    +
    ", rotation=" + rotation
    +
    '}'
    ;
  }
//
//    public Position getScreenPosition() {
//        return screenPosition;
//    }
}
