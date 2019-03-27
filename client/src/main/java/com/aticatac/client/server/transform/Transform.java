//package com.aticatac.client.server.transform;
//
//import com.aticatac.server.components.Component;
//import com.aticatac.client.server.objectsystem.GameObject;
//
///**
// * The type Transform.
// */
//public class Transform extends Component {
//  private final Position position;
//  private int rotation = 0;
//
//  /**
//   * Instantiates a new Component.
//   *
//   * @param gameObject the component parent
//   */
//  public Transform(GameObject gameObject) {
//    super(gameObject);
//    this.position = new Position(0, 0);
//  }
//
//  public Transform(Position position) {
//    this.position = position;
//  }
//
//  /**
//   * Gets position.
//   *
//   * @return the position
//   */
//  public Position getPosition() {
//    return new Position(getX(), getY());
//  }
//
//  /**
//   * Sets position.
//   *
//   * @param transform the transform
//   */
//  public void setPosition(Transform transform) {
//    this.position.set(transform.position);
//  }
////    public void setPosition(double x, double y) {
////        position = new Position(x, y);
////    }
//
//  public void setPosition(final Position position) {
//    this.position.set(position);
//  }
//
//  /**
//   * Gets x.
//   *
//   * @return the x
//   */
//  public int getX() {
//    if (this.getGameObject().hasParent()) {
//      GameObject parent = this.getGameObject().getParent();
//      if (parent.componentExists(Transform.class)) {
//        Transform transform = parent.getTransform();
//        int x = transform.getX();
//        return x + this.position.getX();
//      }
//    }
////        else {
////            return getRelativeX();
////        }
//    return getRelativeX();
//  }
//
//  private int getParentX() {
//    if (this.getGameObject().hasParent()) {
//      GameObject parent = this.getGameObject().getParent();
//      if (parent.componentExists(Transform.class)) {
//        Transform transform = parent.getTransform();
//        return transform.getX();
//      }
//    }
//    return getX();
//  }
//
//  private int getParentY() {
//    if (this.getGameObject().hasParent()) {
//      GameObject parent = this.getGameObject().getParent();
//      if (parent.componentExists(Transform.class)) {
//        Transform transform = parent.getTransform();
//        return transform.getY();
//      }
//    }
//    return getX();
//  }
//
//  /**
//   * Gets relative x.
//   *
//   * @return the relative x
//   */
//  public int getRelativeX() {
//    return this.position.getX();
//  }
//
//  /**
//   * Gets relative y.
//   *
//   * @return the relative y
//   */
//  public int getRelativeY() {
//    return this.position.getY();
//  }
//
//  /**
//   * Gets y.
//   *
//   * @return the y
//   */
//  public int getY() {
//    if (this.getGameObject().hasParent()) {
//      return this.getGameObject().getParent().getTransform().getY() + this.position.getY();
//    } else {
//      return getRelativeY();
//    }
//  }
//
//  /**
//   * Transform.
//   *
//   * @param x the x
//   * @param y the y
//   */
//  public void transform(int x, int y) {
//    this.setPosition(this.getRelativeX() + x, this.getRelativeY() + y);
//  }
//
//  /**
//   * Sets position.
//   *
//   * @param x the x
//   * @param y the y
//   */
//  public void setPosition(int x, int y) {
//    position.setX(x);
//    position.setY(y);
//  }
//
//  /**
//   * Gets rotation.
//   *
//   * @return the rotation
//   */
//  public int getRotation() {
//    return rotation;
//  }
//
//  /**
//   * Sets rotation.d
//   *
//   * @param r the r
//   */
//  public void setRotation(int r) {
//    rotation = r;
//  }
//
//  @Override
//  public String toString() {
//    return "setPosition{"
//        +
//        "position=" + position
//        +
//        ", rotation=" + rotation
//        +
//        '}'
//        ;
//  }
////
////    public Position getScreenPosition() {
////        return screenPosition;
////    }
//}
