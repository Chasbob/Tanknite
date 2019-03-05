package com.aticatac.server.components.controller;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Position;
import com.aticatac.server.components.*;
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
    // ammo, health, damage, speed
    public boolean moveUp() {
        Position oldPosition = this.getGameObject().getComponent(Transform.class).getPosition();
        Object[] physicsData = this.getGameObject().getComponent(Physics.class).moveUp();
        Position newPosition = (Position) physicsData[1];
        if (oldPosition.equals(newPosition)) return false;
        else {
            System.out.println("new coordinates:" + newPosition.getX() +" "+ newPosition.getY());
            this.getGameObject().getComponent(Transform.class).setPosition(newPosition.getX(), newPosition.getY());
            this.getGameObject().getComponent(Transform.class).setRotation(0);
            DataServer.INSTANCE.setCoordinates(newPosition, "tank", oldPosition);
            if (physicsData[0] == "ammo"){
              pickUpAmmo();
              // destroy powerup object too
            }
            if (physicsData[0] == "health"){
              pickUpHealth();

            }
            if (physicsData[0] == "speed"){
              pickUpSpeed();

            }
            if (physicsData[0] == "damage"){
              pickUpDamage();

            }


        }
        return true;
    }

    /**
     * Move right boolean.
     *
     * @return the boolean
     */
    // when right arrow/d pressed
    public boolean moveRight (){
        Position oldPosition = this.getGameObject().getComponent(Transform.class).getPosition();
        Object[] physicsData = this.getGameObject().getComponent(Physics.class).moveRight();
        Position newPosition = (Position)physicsData[1];

        if (oldPosition.equals(newPosition)) return false;

        else{
            this.getGameObject().getComponent(Transform.class).setPosition(newPosition.getX(), newPosition.getY());
            this.getGameObject().getComponent(Transform.class).setRotation(90);
            DataServer.INSTANCE.setCoordinates(newPosition, "tank", oldPosition);

        }
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
    public boolean moveLeft (){
        Position oldPosition = this.getGameObject().getComponent(Transform.class).getPosition();
        Object[] physicsData = this.getGameObject().getComponent(Physics.class).moveLeft();
        Position newPosition = (Position) physicsData[1];
        if (oldPosition.equals(newPosition)) return false;

        else{
            this.getGameObject().getComponent(Transform.class).setPosition(newPosition.getX(), newPosition.getY());
            this.getGameObject().getComponent(Transform.class).setRotation(270);
            DataServer.INSTANCE.setCoordinates(newPosition, "tank", oldPosition);
          //set occupied co ordinates on server data whenever tank moves
        }


        return true;
    }

    /**
     * Move backwards boolean.
     *
     * @return the boolean
     */
// when down arrow/s pressed
    public boolean moveDown () {
        Position oldPosition = this.getGameObject().getComponent(Transform.class).getPosition();
        Object[] physicsData = this.getGameObject().getComponent(Physics.class).moveDown();
        Position newPosition = (Position)physicsData[1];

        if (oldPosition.equals(newPosition)) return false;

        else {
            this.getGameObject().getComponent(Transform.class).setPosition(newPosition.getX(), newPosition.getY());
            this.getGameObject().getComponent(Transform.class).setRotation(180);
            DataServer.INSTANCE.setCoordinates(newPosition, "tank", oldPosition);
          //set occupied co ordinates on server data whenever tank moves
        }
        return true;
    }

    public boolean shoot() {
      int currentAmmo = this.getGameObject().getComponent(Ammo.class).getAmmo();
      if (currentAmmo == 0) return false;
      this.getGameObject().getComponent(Ammo.class).getAmmo();
      return this.getGameObject().getComponent(TurretController.class).shoot();
    }

    /**
     * Is shot.
     */
    public void isShot(int damage) {
        int currentHealth = this.getGameObject().getComponent(Health.class).getHealth();
        int newHealth = currentHealth - damage;
        this.getGameObject().getComponent(Health.class).setHealth(newHealth);
        if (newHealth > 0 && newHealth <=10){
            dying();
        }
        else if (newHealth <= 0){
            die();
        }
    }

    public void dying() {
//        // tell physics/renderer etc
//        // can no longer move and will die in 20 seconds
        //Physics will check the health and prevent it moving if less than 10
//        //delay
//        die();
    }


    /**
     * Die.
     */
    public void die () {

        //remove it from data coordinates
        DataServer.INSTANCE.deleteCoordinates(this.getGameObject().getComponent(Transform.class).getPosition());

        //add a powerup into the data that says that this is a power up with location.
        //DataServer.INITIALISE.setOccupiedCoordinates("ammopowerup", this.getGameObject.getComponent(Transform.class).GetPosition());
        GameObject.Destroy(getGameObject());

        //TODO potentially not relevant in here
        //Check if the number of tanks alive is 1, if so end the game
        //if not then remove this tank from number of tanks on the map

    }


    /**
     * Pick up health.
     */
    public void pickUpHealth () {

        int currentHealth = this.getGameObject().getComponent(Health.class).getHealth();
        int newHealth = currentHealth + 10;
        if (newHealth > this.getGameObject().getComponent(Health.class).getMaxHealth()){
            this.getGameObject().getComponent(Health.class).setHealth(this.getGameObject().getComponent(Health.class).getMaxHealth());
        }
        else this.getGameObject().getComponent(Health.class).setHealth(newHealth);
        // only gain health up to maximum
    }

    /**
     * Pick up ammo.
     */

    public void pickUpAmmo() {

        int currentAmmo = this.getGameObject().getComponent(Ammo.class).getAmmo();
        this.getGameObject().getComponent(Ammo.class).setAmmo(currentAmmo + 10);


    }

    /**
     * Pick up speed.
     */
    public void pickUpSpeed() {

       this.getGameObject().getComponent(Acceleration.class).setPowerUpExists(true);
       //make this a thread which waits for certain time then
        // this.getGameObject().getComponent(Acceleration.class).setPowerUpExists(false);


    }


    /**
     * Pick up damage.
     */
    public void pickUpDamage () {

        //this.getGameObject().getComponent(BulletDamage.class).setPowerUpExists(true);
        //make this a thread which waits for certain time then
        // this.getGameObject().getComponent(BulletDamage.class).setPowerUpExists(false);

    }


}