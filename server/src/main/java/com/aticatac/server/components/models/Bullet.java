package com.aticatac.server.components.models;

import com.aticatac.server.components.PhysicsManager;

/**
 * The type Bullet.
 */
public class Bullet {
    private int currentXCoord;
    private int currentYCoord;
    private char currentDirection;
    private int damage; // or just have special case when shooting with powerup?
    private boolean collided = false;

    /**
     * Instantiates a new Bullet.
     *
     * @param xCoord    the x coord
     * @param yCoord    the y coord
     * @param direction the direction
     */
    public Bullet (int xCoord, int yCoord, char direction){
        currentXCoord = xCoord;
        currentYCoord = yCoord;
        currentDirection = direction;
        // add components for physicamanager and transform
    }

    //physics in charge of moving and collisions for bullet and tank

    /**
     * Move forwards.
     */

    // Need different methods to tank movement in PhysicsManager as simpler movement?
    public void moveForwards() {
        while (!collided) {
            switch (currentDirection) {
                case 'N':
                    if (PhysicsManager.up()) {
                        setCurrentYCoord(currentYCoord + 1);
                    } else collided();
                case 'S':
                    if (PhysicsManager.down()) {
                        setCurrentYCoord(currentYCoord - 1);
                    } else collided();
                case 'E':
                    if (PhysicsManager.right()) {
                        setCurrentXCoord(currentXCoord + 1);
                    } else collided();
                case 'W':
                    if (PhysicsManager.left()) {
                        setCurrentXCoord(currentXCoord - 1);
                    } else collided();
            }
        }
    }
    // physics handles bullet collision
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

    public void collided () {
        collided = true;
    }

}
