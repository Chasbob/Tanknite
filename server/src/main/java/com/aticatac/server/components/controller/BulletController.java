package com.aticatac.server.components.controller;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.DataServer;
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
      String collisionType = (String) physicsData[0];
      // TODO: Check whether not ammo, not health, not wall etc to work out whether tank, then store tank id in bidimap so can call isShoot on that
      if (!(collisionType.equals("none")) && !(collisionType.equals("health")) && !(collisionType.equals("speed")) && !(collisionType.equals("ammo")) && !(collisionType.equals("damage"))){
        if (collisionType.equals("wall")) {
          GameObject.destroy(getGameObject());
        } else {
          // collided with tank
          // might need to disallow usernames 0-5?
          // TODO: check when tanks created, if the objects are named after username, need a way to access
          // String collidedTankName = collisionType;
          //GameObject collidedTankName = DataServer.INSTANCE.getOccupiedCoordinates();

          //GameObject collidedTankName = this.getGameObject().getComponent(DataServer.class).getOccupiedCoordinatesTank().getKey(newPosition);// get tank using new position from serverData
          //collidedTankName.getComponent(TankController.class).isShot(this.getGameObject().getComponent(Damage.class).getDamage());
          GameObject.destroy(getGameObject());
          // copy bullet controller from physics


        }
      }   // set occupied co ordinates for server data
      else {
        this.getGameObject().getComponent(Transform.class).setPosition(newPosition.getX(), newPosition.getY()); // Hasnt collided
      }
    }
    // physics handles bullet collision

    // make a method for bullet to disappear
  }

}




