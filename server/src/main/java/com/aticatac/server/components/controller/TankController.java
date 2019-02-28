package com.aticatac.server.components.controller;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.objectsystem.GameObject;
// components for server side make in server or import from common?
// needs component of Physics

/**
 * The type TankController.
 */
public class TankController extends Component {
    /**
     * Instantiates a new Component.
     *
     * @param gameObject the component parent
     */
    public TankController(GameObject gameObject) {
        super(gameObject);
    }
    //when sound plays check all tanks in that area and play noise to all those players

    /**
     * call method when up arrow/w pressed
     *
     * @return the boolean @
     */
    //physics manager will return a value. 0 = no collision, any other will mean collision.
    //else set position to new transform
    public boolean moveUp() {
//        Position oldPosition = this.getGameObject().getComponent(applyTransform.class).getPosition();
//        Object[] physicsData = this.getGameObject().getComponent(Physics.class).moveUp();
//        Position newPosition = (Position) physicsData[1];
//        if (oldPosition.equals(newPosition)) return false;
//        else {
//            this.getGameObject().getComponent(applyTransform.class).setPosition(newPosition.getX(), newPosition.getY());
//            this.getGameObject().getComponent(applyTransform.class).setRotation(0);
//            DataServer.INSTANCE.setCoordinates(newPosition, "tank", oldPosition);
//        }

        this.getGameObject().getComponent(Transform.class).applyTransform(0,-3);
            this.getGameObject().getComponent(Transform.class).setRotation(0);
        return true;
    }

    /**
     * Move right boolean.
     *
     * @return the boolean
     */
    // when right arrow/d pressed
    public boolean moveRight() {
//        Position oldPosition = this.getGameObject().getComponent(applyTransform.class).getPosition();
//        Object[] physicsData = this.getGameObject().getComponent(Physics.class).moveRight();
//        Position newPosition = (Position)physicsData[1];
//
//        if (oldPosition.equals(newPosition)) return false;
//
//        else{
//            this.getGameObject().getComponent(applyTransform.class).setPosition(newPosition.getX(), newPosition.getY());
//            this.getGameObject().getComponent(applyTransform.class).setRotation(90);
//            DataServer.INSTANCE.setCoordinates(newPosition, "tank", oldPosition);
//
//        }
        this.getGameObject().getComponent(Transform.class).applyTransform(-3,0);
            this.getGameObject().getComponent(Transform.class).setRotation(270);
//            this.getGameObject().getComponent(applyTransform.class).setRotation(90);
        //physics tells what type of collision, if bullet + tank then call isShot, if bullet and anything else
        //bullet disappears, other collisions have no effect, just stop the current move from happening
        return true;
    }

    /**
     * Move left boolean.
     *
     * @return the boolean
     */
    // when left arrow/a pressed
    public boolean moveLeft() {
//        Position oldPosition = this.getGameObject().getComponent(applyTransform.class).getPosition();
//        Object[] physicsData = this.getGameObject().getComponent(Physics.class).moveLeft();
//        Position newPosition = (Position) physicsData[1];
//        if (oldPosition.equals(newPosition)) return false;
//        else {
//            this.getGameObject().getComponent(applyTransform.class).setPosition(newPosition.getX(), newPosition.getY());
//            DataServer.INSTANCE.setCoordinates(newPosition, "tank", oldPosition);
//            //set occupied co ordinates on server data whenever tank moves
//        }

        this.getGameObject().getComponent(Transform.class).applyTransform(3,0);
            this.getGameObject().getComponent(Transform.class).setRotation(90);
        return true;
    }

    /**
     * Move backwards boolean.
     *
     * @return the boolean
     */
// when down arrow/s pressed
    public boolean moveDown() {
//        Position oldPosition = this.getGameObject().getComponent(applyTransform.class).getPosition();
//        Object[] physicsData = this.getGameObject().getComponent(Physics.class).moveDown();
//        Position newPosition = (Position) physicsData[1];
//        if (oldPosition.equals(newPosition)) return false;
//        else {
//            this.getGameObject().getComponent(applyTransform.class).setPosition(newPosition.getX(), newPosition.getY());
//            this.getGameObject().getComponent(applyTransform.class).setRotation(180);
//            DataServer.INSTANCE.setCoordinates(newPosition, "tank", oldPosition);
//            //set occupied co ordinates on server data whenever tank moves
//        }

        this.getGameObject().getComponent(Transform.class).applyTransform(0,3);
            this.getGameObject().getComponent(Transform.class).setRotation(180);

        return true;
    }

    /**
     * Shoot boolean.
     *
     * @return the boolean
     */
// call method when space bar pressed
    public boolean shoot() {
//        // call new shoot method from turretx
//        // talk to physics?
//
//
//        int currentAmmo = this.getComponent(Ammo.class).getAmmo();
//
//        if (currentAmmo == 0) return false;
//        return this.findObject(TurretController,) // get turret for this particular tank, and call shoot method in it
//        this.getComponent(Ammo.class).setAmmo(currentAmmo - 1);
        return true;
// create bullet object current co ordinatesn where shooting tank is, and move
        //
    }

    /**
     * Is shot.
     */
    public void isShot() {
//        int currentHealth = this.getComponent(Health.class).getHealth();
//        int newHealth = currentHealth - 10;
//        this.getComponent(Health.class).setHealth(newHealth);
//        if (newHealth > 0 && newHealth <=10){
//            dying();
//        }
//        else if (newHealth <= 0){
//            die();
//        }
// part of colliding? Physics calls this method?
    }

    public void dying() {
//        // tell physics/renderer etc
//        // can no longer move and will die in 20 seconds
//        //delay
//        die();
    }

    /**
     * Die.
     */
    // wheree to have collision with pick up, physics?
    // change from logic interface calling methods to map calling?
    public void die() {
        //new AmmoPickUp(this.getComponent(applyTransform.class).getX(), this.getComponent(applyTransform.class).getY());
        // set ammo pick up transform to where tank died
        //Destroy(this);
        /*
        if (map.getNumberOfAliveTanks() == 1){
            map.gameFinish();
           }
        else map.setNumberOfAliveTanks(map.getNumberOfAliveTanks - 1);



//            */
//        if (Map.getNumberOfAliveTanks() == 1){ // in other logic class?
//            // Map or parent
//            Map.gameFinish();
//            // start numberoftanks at 10 and subtract one for each death
//        }
    }

    /**
     * Pick up health.
     */
    public void pickUpHealth() {
//        int currentHealth = this.getComponent(Health.class).getHealth();
//        int newHealth = currentHealth + 10;
//        if (newHealth > maxHealth){
//            this.getComponent(Health.class).setHealth(maxHealth);
//        }
//        else this.getComponent(Health.class).setHealth(newHealth);
//        // only gain health up to maximum
    }

    /**
     * Pick up ammo.
     */
    public void pickUpAmmo() {
//        int currentAmmo = this.getComponent(Ammo.class).getAmmo();
//        this.getComponent(Ammo.class).setAmmo(currentAmmo + 10);
    }

    /**
     * Pick up speed.
     */
    public void pickUpSpeed() {
    }

    /**
     * Pick up damage.
     */
    public void pickUpDamage() {
    }
    /**
     * Gets health.
     */
}


/* Method takes command (check branch) and returns boolean of whether possible
 */