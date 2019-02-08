package com.aticatac.server.components.models;

import com.aticatac.server.components.models.Bullet;
import com.aticatac.server.components.model.LogicInterface;

/**
 * The type Tank.
 */
public class Tank {
    private int currentXCoord;
    private int currentYCoord;
    private int currentAmmo;
    private char currentDirection;
    private int maxHealth = 100;
    private int currentHealth;

    /**
     * Instantiates a new Tank.
     *
     * @param xCoord the x coord
     * @param yCoord the y coord
     */
    public Tank (int xCoord, int yCoord){
        currentXCoord = xCoord;
        currentYCoord = yCoord;
        currentAmmo = 30;
        currentHealth = 100;
    }
// have ammo as component and get health component, and transform and position etc
// determine whether ai, put behavioural trees from ai if so



    //when sound plays check all tanks in that area and play noise to all those players

    /**
     * call method when up arrow/w pressed
     *
     * @return the boolean @
     */
//update to account for move to Tank class
    public boolean moveForwards() {
        int [] movement = physicsManager.forwards(int currentXCoord, int currentYCoord);
        currentXCoord = movement[1];
        currentYCoord = movement[2];
        currentDirection = 'N';
        if (movement[0] == 0) return false;
        else return true;
        // physics returns an array of the boolean, and then 2 co ordinates it moves to
        // Talk to physics, it will return whether can move, if can then tell renderer it moves and where
        // co ordinates can be changed in physics
    }

    /**
     * Move right boolean.
     *
     * @return the boolean
     */
// when right arrow/d pressed
    public boolean moveRight (){
        int [] movement = physicsManager.right(int currentXCoord, int currentYCoord);
        currentXCoord = movement[1];
        currentYCoord = movement[2];
        currentDirection = 'E';
        if (movement[0] == 0) return false;
        else return true;
    }

    /**
     * Move left boolean.
     *
     * @return the boolean
     */
// when left arrow/a pressed
    public boolean moveLeft (){
        int [] movement = physicsManager.left(int currentXCoord, int currentYCoord);
        currentXCoord = movement[1];
        currentYCoord = movement[2];
        currentDirection = 'W';
        if (movement[0] == 0) return false;
        else return true;
    }

    /**
     * Move backwards boolean.
     *
     * @return the boolean
     */
// when down arrow/s pressed
    public boolean moveBackwards (){
        int [] movement = physicsManager.backwards(int currentXCoord, int currentYCoord);
        currentXCoord = movement[1];
        currentYCoord = movement[2];
        currentDirection = 'S';
        if (movement[0] == 0) return false;
        else return true;
    }

    /**
     * Shoot boolean.
     *
     * @return the boolean
     */
// call method when space bar pressed
    public boolean shoot() {
        // talk to physics?
        if (currentAmmo == 0) return false;
        Bullet bullet = new Bullet(currentXCoord, currentYCoord, currentDirection);
        currentAmmo--;
        return true;

// create bullet object current co ordinatesn where shooting tank is, and move
        //
    }

    /**
     * Is shot.
     */
    public static void isShot() {
// part of colliding? Physics calls this method?
        //if new health = 0 then die()
    }

    /**
     * Die.
     */
    public void die () {
        // drops ammo box
        if (LogicInterface.numberOfAliveTanks() == 1){ // in other logic class?
            LogicInterface.gameFinish();
            // start numberoftanks at 10 and subtract one for each death
        }
    }


    /**
     * Pick up health.
     */
    public void pickUpHealth () {
        currentHealth += 10;
        if (currentHealth > maxHealth){
            currentHealth = maxHealth;
        }
        // only gain health up to maximum
    }

    /**
     * Pick up ammo.
     */
    public void pickUpAmmo () {
        currentAmmo += 10;
    }

    /**
     * Pick up speed.
     */
    public void pickUpSpeed () {

    }

    /**
     * Pick up damage.
     */
    public void pickUpDamage () {

    }

    /**
     * Gets health.
     */
    public static void getHealth () {


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
}


/* Method takes command (check branch) and returns boolean of whether possible
 */