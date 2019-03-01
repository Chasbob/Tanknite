package com.aticatac.server.components.controller;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.Damage;
import com.aticatac.server.components.Physics;

import static com.aticatac.common.objectsystem.GameObject.Destroy;
import com.aticatac.server.components.Physics;
import com.aticatac.server.components.ServerData;


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
            Object physicsData[] = this.getGameObject().getComponent(Physics.class).bulletMove(this.getGameObject().getComponent(Transform.class).GetRotation());
            Position newPosition = (Position) physicsData[1];
            int collisionType = (Integer) physicsData[0];
            //0 nothing, 1 is a wall, 2 is a tank
            if (collisionType != 0) {
                if (collisionType == 1) {
                    Destroy(this);
                } else {
                    String collidedTankName = DataServer.INSTANCE.getOccupiedCoordinates().getKey(newPosition); //
                    // TODO: use secind bidimap which stores tanks names and tanks positions

          String collidedTankName = this.getGameObject().getComponent(ServerData.class).getOccupiedCoordinatesTank().getKey(newPosition);// get tank using new position from serverData
          collidedTankName.getGameObject().getComponent(TankController.class).isShot(this.getGameObject().getComponent(Damage.class).getDamage());
          Destroy(this);

                    Destroy(this);

                }
            }   // set occupied co ordinates for server data
            else {
                this.getGameObject().getComponent(Transform.class).SetTransform(newPosition.getX(), newPosition.getY());
            }
        }
    }
    // physics handles bullet collision

    // make a method for bullet to disappear

}




