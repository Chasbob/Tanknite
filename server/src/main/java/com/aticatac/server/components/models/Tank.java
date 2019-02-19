package com.aticatac.server.components.models;

import com.aticatac.server.components.*;
import com.aticatac.server.components.model.Map;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.models.powerups.AmmoPickUp;
// components for server side make in server or import from common?
// needs component of PhysicsManager
/**
 * The type Tank.
 */
public class Tank extends GameObject {
    // change these variables to components when updated
    private int currentXCoord;
    private int currentYCoord;
    private char currentDirection;
    private int maxHealth = 100;

    /**
     * Instantiates a new Tank.
     *
     * @param xCoord the x coord
     * @param yCoord the y coord
     */
    public Tank (GameObject Parent, String name){
        // work out where to give starting co ordinates for tank when gets created, change params to work for this when
        // tank is created
        // need to link turret to tank
        super(Parent, name);
        GameObject Turret = new GameObject(Tank, name); // will turret move when tank does if turret is child of tank?
        this.addComponent(Health.class);
        this.addComponent(AI.class);
        this.addComponent(Ammo.class);
        this.addComponent(PhysicsManager.class);
        this.addComponent(Transform.class);
        this.addComponent(Position.class);
        this.addComponent(Time.class);
        this.getComponent(Health.class).setHealth(100);
        this.getComponent(Ammo.class).setAmmo(30);
        // where to set starting coords? this.getComponent(Transform.class).setTransform(xCoord, yCoord);
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
        double oldX = this.getComponent(Transform.class).getX();
        double oldY = this.getComponent(Transform.class).getY();
        Position oldPosition = this.getComponent(Position.class).Position(oldX, oldY);
        Position newPosition = this.getComponent(PhysicsManager.class).moveForwards();
        currentDirection = 'N';
        this.getComponent(Transform.class).setTransform(newPosition.getX(), newPosition.getY());
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
        double oldX = this.getComponent(Transform.class).getX();
        double oldY = this.getComponent(Transform.class).getY();
        Position oldPosition = this.getComponent(Position.class).Position(oldX, oldY);
        Position newPosition = this.getComponent(PhysicsManager.class).moveRight();
        currentDirection = 'E';
        this.getComponent(Transform.class).setTransform(newPosition.getX(), newPosition.getY());
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
        double oldX = this.getComponent(Transform.class).getX();
        double oldY = this.getComponent(Transform.class).getY();
        Position oldPosition = this.getComponent(Position.class).Position(oldX, oldY);
        Position newPosition = this.getComponent(PhysicsManager.class).moveLeft();
        currentDirection = 'W';
        this.getComponent(Transform.class).setTransform(newPosition.getX(), newPosition.getY());
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
        double oldX = this.getComponent(Transform.class).getX();
        double oldY = this.getComponent(Transform.class).getY();
        Position oldPosition = this.getComponent(Position.class).Position(oldX, oldY);
        Position newPosition = this.getComponent(PhysicsManager.class).moveBackwards();
        currentDirection = 'S';
        this.getComponent(Transform.class).setTransform(newPosition.getX(), newPosition.getY());
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
        // call new shoot method from turretx
        // talk to physics?


        int currentAmmo = this.getComponent(Ammo.class).getAmmo();

        if (currentAmmo == 0) return false;
        return this.findObject()
        /*Bullet bullet = new Bullet(Transform.getX(), Transform.getY(), currentDirection); // NEED TO CHANGE PARAMS FOR BULLET
        bullet.moveForwards(); */
        this.getComponent(Ammo.class).setAmmo(currentAmmo - 1);
        return true;


// create bullet object current co ordinatesn where shooting tank is, and move
        //
    }

    /**
     * Is shot.
     */
    public void isShot() {
        int currentHealth = this.getComponent(Health.class).getHealth();
        int newHealth = currentHealth - 10;
        this.getComponent(Health.class).setHealth(newHealth);
        if (newHealth > 0 && newHealth <=10){
            dying();
        }
        else if (newHealth <= 0){
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
        new AmmoPickUp(this.getComponent(Transform.class).getX(), this.getComponent(Transform.class).getY());
        /*
        if (map.getNumberOfAliveTanks() == 1){
            map.gameFinish();
           }
        else map.setNumberOfAliveTanks(map.getNumberOfAliveTanks - 1);



            */
        if (Map.getNumberOfAliveTanks() == 1){ // in other logic class?
            // Map or parent
            Map.gameFinish();
            // start numberoftanks at 10 and subtract one for each death
        }
    }


    /**
     * Pick up health.
     */
    public void pickUpHealth () {
        int currentHealth = this.getComponent(Health.class).getHealth();
        int newHealth = currentHealth + 10;
        if (newHealth > maxHealth){
            this.getComponent(Health.class).setHealth(maxHealth);
        }
        else this.getComponent(Health.class).setHealth(newHealth);
        // only gain health up to maximum
    }

    /**
     * Pick up ammo.
     */
    public void pickUpAmmo () {
        int currentAmmo = this.getComponent(Ammo.class).getAmmo();
        this.getComponent(Ammo.class).setAmmo(currentAmmo + 10);
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