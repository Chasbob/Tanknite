package com.aticatac.common.components.transform;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;

/**
 * The type Transform.
 */
public class Transform extends Component {
  private Position position;
  private int rotation = 0;

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
  public int getX() {
    if (this.getGameObject().hasParent()) {
      GameObject parent = this.getGameObject().getParent();
      if (parent.componentExists(Transform.class)) {
        Transform transform = parent.getTransform();
        int x = transform.getX();
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
  public int getRelativeX() {
    return this.position.getX();
  }

  /**
   * Gets relative y.
   *
   * @return the relative y
   */
  public int getRelativeY() {
    return this.position.getY();
  }

  /**
   * Gets y.
   *
   * @return the y
   */
  public int getY() {
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
  public void transform(int x, int y) {
    this.setPosition(this.getRelativeX() + x, this.getRelativeY() + y);
  }

  /**
   * Sets position.
   *
   * @param x the x
   * @param y the y
   */
  public void setPosition(int x, int y) {
    position.setX(x);
    position.setY(y);
  }

  /**
   * Gets rotation.
   *
   * @return the rotation
   */
  public int getRotation() {
    return rotation;
  }

  /**
   * Sets rotation.d
   *
   * @param r the r
   */
  public void setRotation(int r) {
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
