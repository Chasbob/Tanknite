package com.aticatac.server.components.models;

import com.aticatac.server.components.*;
import com.aticatac.server.components.models.Bullet;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.model.LogicInterface;
import com.aticatac.server.components.models.powerups.AmmoPickUp;
// components for server side make in server or import from common?
// needs component of PhysicsManager
/**
 * The type Tank.
 */
public class Tank /*extends Component*/ {
    // change these variables to components when updated
    private int currentXCoord;
    private int currentYCoord;
    private int currentAmmo;
    private char currentDirection   ;
    private int maxHealth = 100;
    private int currentHealth;

    /**
     * Instantiates a new Tank.
     *
     * @param xCoord the x coord
     * @param yCoord the y coord
     */
    public Tank (int xCoord, int yCoord){
    /*(GameObject componentParent){
        super(componentParent); */
        /*Tank.addComponent(Health.class);
        GameObject.addComponent(AI.class);
        GameObject.addComponent(Ammo.class);
        GameObject.addComponent(PhysicsManager.class);
        GameObject.addComponent(Transform.class);
        GameObject.addComponent(Position.class);
        GameObject.getComponent(Health.class).setHealth(100);
        GameObject.getComponent(Ammo.class).setAmmo(30);*/
        currentXCoord = xCoord;
        currentYCoord = yCoord;
        currentAmmo = 30;
        currentHealth = 100;
    }
// have ammo as component and get health component, and transform and position etc (and physicsmanager)
// determine whether ai, put behavioural trees from ai if so


    //when sound plays check all tanks in that area and play noise to all those players

    /**
     * call method when up arrow/w pressed
     *
     * @return the boolean @
     */
    //physics manager now returns transform, so if new transform = old transform collision has happened,
    //else set position to new transform
    public boolean moveForwards() {
        double oldX = Transform.getX();
        double oldY = Transform.getY();
        Position oldPosition = Position(oldX, oldY);
        Position newPosition = PhysicsManager.moveForwards();
        currentDirection = 'N';
        Transform.setTransform(newPosition.getX(), newPosition.getY());
        if (oldPosition.equals(newPosition)) return false;
        else return true;

    }

    /**
     * Move right boolean.
     *
     * @return the boolean
     */
// when right arrow/d pressed
    public boolean moveRight (){
        double oldX = Transform.getX();
        double oldY = Transform.getY();
        Position oldPosition = Position(oldX, oldY);
        Position newPosition = PhysicsManager.moveRight();
        currentDirection = 'E';
        Transform.setTransform(newPosition.getX(), newPosition.getY());
        if (oldPosition.equals(newPosition)) return false;
        else return true;
        //physics tells what type of collision, if bullet + tank then call isShot, if bullet and anything else
        //bullet disappears, other collisions have no effect, just stop the current move from happening
    }

    /**
     * Move left boolean.
     *
     * @return the boolean
     */
// when left arrow/a pressed
    public boolean moveLeft (){
        double oldX = Transform.getX();
        double oldY = Transform.getY();
        Position oldPosition = Position(oldX, oldY);
        Position newPosition = PhysicsManager.moveLeft();
        currentDirection = 'W';
        Transform.setTransform(newPosition.getX(), newPosition.getY());
        if (oldPosition.equals(newPosition)) return false;
        else return true;
    }

    /**
     * Move backwards boolean.
     *
     * @return the boolean
     */
// when down arrow/s pressed
    public boolean moveBackwards (){
        double oldX = Transform.getX();
        double oldY = Transform.getY();
        Position oldPosition = Position(oldX, oldY);
        Position newPosition = PhysicsManager.moveBackwards();
        currentDirection = 'S';
        Transform.setTransform(newPosition.getX(), newPosition.getY());
        if (oldPosition.equals(newPosition)) return false;
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
        bullet.moveForwards();
        currentAmmo--;
        return true;

// create bullet object current co ordinatesn where shooting tank is, and move
        //
    }

    /**
     * Is shot.
     */
    public void isShot() {
        currentHealth = currentHealth - 10;
        if (currentHealth > 0 && currentHealth <=10){
            dying();
        }
        else if (currentHealth <= 0){
            die();
        }
// part of colliding? Physics calls this method?
    }

    public void dying(){
        // tell physics/renderer etc
        // can no longer move and will die in 20 seconds
        //delay
        die();

    }


    /**
     * Die.
     */

    // wheree to have collision with pick up, physics?
    // change from logic interface calling methods to map calling?
    public void die () {
        new AmmoPickUp(currentXCoord, currentYCoord);
        /*
        if (map.getNumberOfAliveTanks() == 1){
            map.gameFinish();
           }
        else map.setNumberOfAliveTanks(map.getNumberOfAliveTanks - 1);



            */
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