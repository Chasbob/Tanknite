package com.aticatac.server.components.controller;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.objectsystem.GameObject;
<<<<<<< HEAD
import com.aticatac.server.components.DataServer;
import com.aticatac.server.components.Physics;

import static com.aticatac.common.objectsystem.GameObject.Destroy;
=======
import com.aticatac.server.components.Physics;
import com.aticatac.server.components.ServerData;
>>>>>>> dev

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
            Position newPosition = (Position)physicsData[1];
            int collisionType = (Integer)physicsData[0];
            //0 nothing, 1 is a wall, 2 is a tank
            if (collisionType != 0){
                if (collisionType == 1){
<<<<<<< HEAD
                    Destroy(this);
                }
                else {
                    String collidedTankName = DataServer.INSTANCE.getOccupiedCoordinates().getKey(newPosition); //
                    // TODO: use secind bidimap which stores tanks names and tanks positions

                    // get tank using new position from serverData
                    collidedTankName.getComponent(TankController.class).isShot();
                    // TODO: Get correct tank object from tank name

                    Destroy(this);

                    // dont need collided(); // work out whether collided with wall or tank etc
=======
                    //Destroy(this);
                }
                else {

                    //collidedTankName = this.getGameObject().getComponent(ServerData.class).getOccupiedCoordinatesTank().getKey(newPosition);// get tank using new position from serverData
                    //collidedTank.isShot();
                    //Destroy(this);

                    collided(); // work out whether collided with wall or tank etc
>>>>>>> dev
                }
            }   // set occupied co ordinates for server data
            else {
                this.getGameObject().getComponent(Transform.class).SetTransform(newPosition.getX(), newPosition.getY());
            }
    // physics handles bullet collision

    // make a method for bullet to disappear

    public void collided () {
        collided = true;
    }

    public boolean getCurrentCollided () {
        return collided;
    }

}
