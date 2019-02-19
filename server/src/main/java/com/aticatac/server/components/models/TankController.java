package com.aticatac.server.components.models;



import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.server.components.AI;
import com.aticatac.server.components.Ammo;
import com.aticatac.server.components.Health;
import com.aticatac.server.components.PhysicsManager;
import com.aticatac.server.components.model.Map;
import com.aticatac.common.objectsystem.GameObject;

import java.sql.Time;
// components for server side make in server or import from common?
// needs component of PhysicsManager
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


    // change these variables to components when updated


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
    public boolean moveUp() {
//        Position oldPosition = this.gameObject.getComponent(Transform.class).getPosition();
//        Position newPosition = this.gameObject.getComponent(PhysicsManager.class).moveUp();
//        if (oldPosition.equals(newPosition)) return false;
//        else{
//            this.getComponent(Transform.class).SetTransform(newPosition.getX(), newPosition.getY());
//            this.getComponent(Transform.class).SetRotation(0);
//            return true;
//        }
//        // set occupied co ordinates on server data whenever tank moves
        return true;
    }

    /**
     * Move right boolean.
     *
     * @return the boolean
     */
// when right arrow/d pressed
    public boolean moveRight (){
//        Position oldPosition = this.getComponent(Transform.class).getPosition();
//        Position newPosition = this.getComponent(PhysicsManager.class).moveRight();
//        if (oldPosition.equals(newPosition)) return false;
//        else{
//            this.getComponent(Transform.class).SetTransform(newPosition.getX(), newPosition.getY());
//            this.getComponent(Transform.class).SetRotation(90);
//            return true;
//        }
//        //physics tells what type of collision, if bullet + tank then call isShot, if bullet and anything else
//        //bullet disappears, other collisions have no effect, just stop the current move from happening
        return true;
    }

    /**
     * Move left boolean.
     *
     * @return the boolean
     */
// when left arrow/a pressed
    public boolean moveLeft (){
//        Position oldPosition = this.getComponent(Transform.class).getPosition();
//        Position newPosition = this.getComponent(PhysicsManager.class).moveLeft();
//        if (oldPosition.equals(newPosition)) return false;
//        else{
//            this.getComponent(Transform.class).SetTransform(newPosition.getX(), newPosition.getY());
//            this.getComponent(Transform.class).SetRotation(270);
//            return true;
//        }
        return true;
    }

    /**
     * Move backwards boolean.
     *
     * @return the boolean
     */
// when down arrow/s pressed
    public boolean moveDown () {
//        Position oldPosition = this.getComponent(Transform.class).getPosition();
//        Position newPosition = this.getComponent(PhysicsManager.class).moveBackwards();
//        if (oldPosition.equals(newPosition)) return false;
//        else {
//            this.getComponent(Transform.class).SetTransform(newPosition.getX(), newPosition.getY());
//            this.getComponent(Transform.class).SetRotation(180);
//            return true;
//        }
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
//        return this.findObject(Turret,) // get turret for this particular tank, and call shoot method in it
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

    public void dying(){
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
    public void die () {
        //new AmmoPickUp(this.getComponent(Transform.class).getX(), this.getComponent(Transform.class).getY());
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
    public void pickUpHealth () {
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
    public void pickUpAmmo () {
//        int currentAmmo = this.getComponent(Ammo.class).getAmmo();
//        this.getComponent(Ammo.class).setAmmo(currentAmmo + 10);
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
}


/* Method takes command (check branch) and returns boolean of whether possible
 */