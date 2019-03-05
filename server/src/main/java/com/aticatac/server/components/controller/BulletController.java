package com.aticatac.server.components.controller;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.Damage;
import com.aticatac.server.components.DataServer;
import com.aticatac.server.components.Physics;

import static com.aticatac.common.objectsystem.GameObject.destroy;
import com.aticatac.server.components.Physics;


/**
 * The type BulletController.
 */
public class BulletController extends Component {

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
      Object physicsData[] = this.getGameObject().getComponent(Physics.class).bulletMove(this.getGameObject().getComponent(Transform.class).getRotation());
      Position newPosition = (Position) physicsData[1];
      int collisionType = (Integer) physicsData[0];
      //0 nothing, 1 is a wall, 2 is a tank
      if (collisionType != 0) {
        if (collisionType == 1) {
          GameObject.destroy(getGameObject());
        } else {

        }
      }

    }

  }

}
