package com.aticatac.server.components.models;

import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.PhysicsManager;
import com.aticatac.common.components.transform.Position;

/**
 * The type Bullet.
 */
public class Bullet extends GameObject{
    private int currentXCoord;
    private int currentYCoord;
    private char currentDirection;
    private int damage; // or just have special case when shooting with powerup?
    private boolean collided = false;

    public Bullet (GameObject Parent, String name){
        super (Parent, name);
        this.addComponent(PhysicsManager.class);
        this.addComponent(Time.class);


    /*(int xCoord, int yCoord, char direction){
        currentXCoord = xCoord;
        currentYCoord = yCoord;
        currentDirection = direction;*/
    // work out how to parse in these params!
        // add components for physicamanager, transform and position
    }

    //physics in charge of moving and collisions for bullet and tank

    /**
     * Move forwards.
     */

    // Need different methods to tank movement in PhysicsManager as simpler movement?
    public void moveForwards(double rotation) {
        while (!collided) {
            Position oldPosition = this.getComponent(Transform.class).GetPosition();
            Position newPosition = this.getComponent(PhysicsManager.class).bulletMove(rotation);
            if (oldPosition.equals(newPosition)){
                collided(); // work out whether collided with wall or tank etc
            }
            else {
                this.getComponent(Transform.class).SetTransform(newPosition.getX(), newPosition.getY());
            }

            /*
            switch (currentDirection) {
                case 'N':
                    if (this.getComponent(PhysicsManager.class).up()) {
                        this.getComponent(Transform.class).setTransform(currentXCoord, currentYCoord + 1);
                    } else collided();
                case 'S':
                    if (this.getComponent(PhysicsManager.class).down()) {
                        this.getComponent(Transform.class).setTransform(currentXCoord, currentYCoord - 1);
                    } else collided();
                case 'E':
                    if (this.getComponent(PhysicsManager.class).right()) {
                        this.getComponent(Transform.class).setTransform(currentXCoord + 1, currentYCoord);
                    } else collided();
                case 'W':
                    if (this.getComponent(PhysicsManager.class).left()) {
                        this.getComponent(Transform.class).setTransform(currentXCoord - 1, currentYCoord);
                    } else collided();
            }
            */
        }
    }
    // physics handles bullet collision

    public void collided () {
        collided = true;
        // destroy bullet, checkm if collided with tank or wall etc
        // if collided with tank, tank.isShot();
    }
    /**
     * Set current x coord.
     *
     * @param xCoord the x coord
     */
    public void setCurrentXCoord (int xCoord){

        currentXCoord = xCoord;
    }

    /**
     * Set current y coord.
     *
     * @param yCoord the y coord
     */
    public void setCurrentYCoord (int yCoord){

        currentYCoord = yCoord;
    }

    /**
     * Set current direction.
     *
     * @param direction the direction
     */
    public void setCurrentDirection (char direction){

        currentDirection = direction;
    }

    /**
     * Get current x coord int.
     *
     * @return the int
     */
    public int getCurrentXCoord (){

        return currentXCoord;
    }

    /**
     * Gets current y coord.
     *
     * @return the current y coord
     */
    public int getCurrentYCoord () {

        return currentYCoord;
    }

    /**
     * Gets current direction.
     *
     * @return the current direction
     */
    public char getCurrentDirection () {

        return currentDirection;
    }

    public boolean getCurrentCollided () {
        return collided;
    }

}
