package com.aticatac.server.components.controller;

import com.aticatac.server.components.Component;
import com.aticatac.server.components.physics.Entity;
import com.aticatac.server.components.physics.Physics;
import com.aticatac.server.components.physics.PhysicsResponse;
import com.aticatac.server.components.transform.Position;
import com.aticatac.server.components.transform.Transform;
import com.aticatac.server.objectsystem.GameObject;

/**
 * The type BulletController.
 */
public class BulletController extends Component {
  private int damage = 10; // or just have special case when shooting with powerup?
  private boolean collided = false;

  /**
   * Instantiates a new Component.
   *
   * @param gameObject the component parent
   */
  public BulletController(GameObject gameObject) {
    super(gameObject);
  }

  /**
   * Move forwards.
   */

  public void moveForwards() {
    while (true) {
      //Position oldPosition = this.getGameObject().getComponent(Transform.class).getPosition();
//      PhysicsResponse physicsData = this.getGameObject().getComponent(Physics.class).move(this.getGameObject().getComponent(Transform.class).getRotation());
//      Position newPosition = (Position) physicsData[1];
//      int collisionType = (Integer) physicsData[0];
//      //0 nothing, 1 is a wall, 2 is a tank
//      if (collisionType != 0) {
//        if (collisionType == 1) {
//          GameObject.destroy(getGameObject());
//        } else {
//
//        }
//      }

    }

  }

}
