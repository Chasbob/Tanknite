package com.aticatac.server.components.controller;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.Damage;
import com.aticatac.server.components.Physics;
import com.aticatac.server.prefabs.TankObject;


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
      if (!(collisionType.equals("none")) && !(collisionType.equals("health")) && !(collisionType.equals("speed")) && !(collisionType.equals("ammo")) && !(collisionType.equals("damage"))){
        if (collisionType.equals("wall")) {
          GameObject.destroy(getGameObject());
          return;
        } else {
          // collided with tank
          // TODO: check when tanks created, if the objects are named after username, need a way to access
          String collidedTankName = collisionType;
          //GameObject collidedTankName = DataServer.INSTANCE.getOccupiedCoordinates();

          //GameObject collidedTankName = this.getGameObject().getComponent(DataServer.class).getOccupiedCoordinatesTank().getKey(newPosition);
          // TODO: Work out how to get tank gsme object from username string
          GameObject collidedTank = this.getGameObject().findObject("collidedTankName", TankObject);
          collidedTank.isShot(this.getGameObject().getComponent(Damage.class).getDamage());
          //collidedTankName.getGameObject().getComponent(TankController.class).isShot(this.getGameObject().getComponent(Damage.class).getDamage());
          GameObject.destroy(getGameObject());
          return;



        }
      }
      else {
        this.getGameObject().getComponent(Transform.class).setPosition(newPosition.getX(), newPosition.getY()); // Hasnt collided
      }
    }
  }

}




