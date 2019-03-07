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
      int collisionType = (Integer) physicsData[0];
      //0 nothing, 1 is a wall, 2-5 are ppwerups, oterwise a tank id
      // TODO: Check whether not ammo, not health, not wall etc to work out whether tank, then store tank id in bidimap so can call isShoot on that
      if (collisionType != 0 && collisionType != 2 && collisionType != 3 && collisionType != 4 && collisionType != 5) {
        if (collisionType == 1) {
          GameObject.destroy(getGameObject());
        }
        else {
          // collided with tank
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
  }




}
