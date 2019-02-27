package com.aticatac.server.components.controller;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.ServerData;
import com.aticatac.server.components.DataServer;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.server.components.Ammo;
import com.aticatac.server.components.DataServer;
import com.aticatac.server.components.Health;
import com.aticatac.server.components.Physics;
import com.aticatac.server.components.Time;
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
        Position oldPosition = this.gameObject.getComponent(Transform.class).getPosition();
        Position newPosition = this.gameObject.getComponent(PhysicsManager.class).moveUp();
        if (oldPosition.equals(newPosition)) return false;
        else{
            parent.getComponent(Transform.class).SetTransform(newPosition.getX(), newPosition.getY());
            this.getComponent(Transform.class).SetRotation(0);
            DataServer.getOccupiedCoordinates(oldPosition, newPosition);
            // convert to enums, use code before to get key
            // server data component add to tank
            // where to set initial tanks occupied co ordinates
            return true;
        }
        // TODO, in movement have physics tell if a tank has collided with a power up
        return true;
    }
    return true;
  }

  /**
   * Move right boolean.
   *
   * @return the boolean
   */
  // when right arrow/d pressed
  public boolean moveRight() {
    this.getGameObject().getComponent(Time.class).startMoving();
    Position oldPosition = this.getGameObject().getComponent(Transform.class).getPosition();
    Object[] physicsData = this.getGameObject().getComponent(Physics.class).moveRight();
    Position newPosition = (Position) physicsData[1];
    if (oldPosition.equals(newPosition)) {
      return false;
    } else {
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
  public boolean moveLeft() {
    this.getGameObject().getComponent(Time.class).startMoving();
    Position oldPosition = this.getGameObject().getComponent(Transform.class).getPosition();
    Object[] physicsData = this.getGameObject().getComponent(Physics.class).moveLeft();
    Position newPosition = (Position) physicsData[1];
    if (oldPosition.equals(newPosition)) {
      return false;
    } else {
      this.getGameObject().getComponent(Transform.class).setPosition(newPosition.getX(), newPosition.getY());
      DataServer.INSTANCE.setCoordinates(newPosition, "tank", oldPosition);
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
            this.getGameObject().getComponent(Transform.class).SetTransform(newPosition.getX(), newPosition.getY());
            this.getGameObject().getComponent(Transform.class).SetRotation(180);
            DataServer.INSTANCE.setCoordinates(newPosition, "tank", oldPosition);
        }
        return true;
    }
    return true;
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


        int currentAmmo = this.getGameObject().getComponent(Ammo.class).getAmmo();

        if (currentAmmo == 0) return false;
        return this.findObject(TurretController,) // get turret for this particular tank, and call shoot method in it
        this.getGameObject().getComponent(Ammo.class).setAmmo(currentAmmo - 1);
        return true;


// create bullet object current co ordinatesn where shooting tank is, and move
    //
  }

  /**
   * Is shot.
   */
  public void isShot() {
    int currentHealth = this.getGameObject().getComponent(Health.class).getHealth();
    int newHealth = currentHealth - 10;
    this.getGameObject().getComponent(Health.class).setHealth(newHealth);
    if (newHealth > 0 && newHealth <= 10) {
      dying();
    } else if (newHealth <= 0) {
      die();
    }
  }

  public void dying() {
//        // tell physics/renderer etc
//        // can no longer move and will die in 20 seconds
//        //delay
//        die();
  }

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
        // TODO keep number of alive tanks somewhere (gamemanager? Parent of tanks)
        if (map.getNumberOfAliveTanks() == 1){
            map.gameFinish();
           }

        else map.setNumberOfAliveTanks(map.getNumberOfAliveTanks - 1);


        if (Map.getNumberOfAliveTanks() == 1){ // in other logic class?
           // Map or parent
            Map.gameFinish();
            // start numberoftanks at 10 and subtract one for each death
        }*/
  }

  /**
   * Pick up health.
   */
  public void pickUpHealth() {
    int currentHealth = this.getGameObject().getComponent(Health.class).getHealth();
    int newHealth = currentHealth + 10;
    if (newHealth > this.getGameObject().getComponent(Health.class).getMaxHealth()) {
      this.getGameObject().getComponent(Health.class).setHealth(this.getGameObject().getComponent(Health.class).getMaxHealth());
    } else {
      this.getGameObject().getComponent(Health.class).setHealth(newHealth);
    }
    // only gain health up to maximum
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

    }

    /**
     * Pick up damage.
     */
    public void pickUpDamage () {

    }

}

