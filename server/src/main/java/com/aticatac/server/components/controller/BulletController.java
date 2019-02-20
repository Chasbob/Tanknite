package com.aticatac.server.components.controller;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;

/**
 * The type BulletController.
 */
public class BulletController extends Component {

    private int damage; // or just have special case when shooting with powerup?
    private boolean collided = false;

    /**
     * Instantiates a new Component.
     *
     * @param gameObject the component parent
     */
    public BulletController(GameObject gameObject) {
        super(gameObject);
    }
    //physics in charge of moving and collisions for bullet and tank

    /**
     * Move forwards.
     */

    // Need different methods to tank movement in Physics as simpler movement?
//    public void moveForwards(double rotation) {
//        while (true) {
//            Position oldPosition = this.getComponent(Transform.class).GetPosition();
//            Object physicsData[] = this.getComponent(Physics.class).bulletMove(rotation);
//            Position newPosition = (Position)physicsData[1];
//            int collisionType = (Integer)physicsData[0];
//            //0 nothing, 1 is a wall, 2 is a tank
//            if (collisionType != 0){
//                if (collisionType == 1){
//                    Destroy(this);
//                }
//                else {
//
//                    TankController collidedTank = ;// get tank using new position from serverData
//                    collidedTank.isShot();
//                    Destroy(this);
//
//                    collided(); // work out whether collided with wall or tank etc
//                }
//            }   // set occupied co ordinates for server data
//            else {
//                this.getComponent(Transform.class).SetTransform(newPosition.getX(), newPosition.getY());
//            }
//
//            /*
//            switch (currentDirection) {
//                case 'N':
//                    if (this.getComponent(Physics.class).up()) {
//                        this.getComponent(Transform.class).setTransform(currentXCoord, currentYCoord + 1);
//                    } else collided();
//                case 'S':
//                    if (this.getComponent(Physics.class).down()) {
//                        this.getComponent(Transform.class).setTransform(currentXCoord, currentYCoord - 1);
//                    } else collided();
//                case 'E':
//                    if (this.getComponent(Physics.class).right()) {
//                        this.getComponent(Transform.class).setTransform(currentXCoord + 1, currentYCoord);
//                    } else collided();
//                case 'W':
//                    if (this.getComponent(Physics.class).left()) {
//                        this.getComponent(Transform.class).setTransform(currentXCoord - 1, currentYCoord);
//                    } else collided();
//            }
//            */
//        }
//    }
    // physics handles bullet collision

    // make a method for bullet to disappear

    public void collided () {
        collided = true;
    }

    public boolean getCurrentCollided () {
        return collided;
    }

}
