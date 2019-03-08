package com.aticatac.server.components.controller;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.objectsystem.GameObject;
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

  /**
   * call method when up arrow/w pressed
   *
   * @return the boolean @
   */
  //physics manager will return a value. 0 = no collision, any other will mean collision.
  //else set position to new transform
  public boolean moveUp() {
    this.getGameObject().getComponent(Time.class).startMoving();
    Position oldPosition = this.getGameObject().getComponent(Transform.class).getPosition();
    Object[] physicsData = this.getGameObject().getComponent(Physics.class).moveUp();
    Position newPosition = (Position) physicsData[1];
    if (oldPosition.equals(newPosition)) {
      return false;
    } else {
      this.getGameObject().getComponent(Transform.class).setPosition(newPosition.getX(), newPosition.getY());
      this.getGameObject().getComponent(Transform.class).setRotation(0);
      DataServer.INSTANCE.setCoordinates(newPosition, "tank", oldPosition);
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
  public boolean moveDown() {
    this.getGameObject().getComponent(Time.class).startMoving();
    Position oldPosition = this.getGameObject().getComponent(Transform.class).getPosition();
    Object[] physicsData = this.getGameObject().getComponent(Physics.class).moveDown();
    Position newPosition = (Position) physicsData[1];
    if (oldPosition.equals(newPosition)) {
      return false;
    } else {
      this.getGameObject().getComponent(Transform.class).setPosition(newPosition.getX(), newPosition.getY());
      this.getGameObject().getComponent(Transform.class).setRotation(180);
      DataServer.INSTANCE.setCoordinates(newPosition, "tank", oldPosition);
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

  /**
   * Die.
   */
  public void die() {
    //add a powerup into the data that says that this is a power up with location.
    //DataServer.INITIALISE.setOccupiedCoordinates("ammopowerup", this.getGameObject.getComponent(Transform.class).GetPosition());
    //Then when this is collided with by a tank add to tank that component
    GameObject.destroy(getGameObject());
    //TODO potentially not relevant in here
        /*if (map.getNumberOfAliveTanks() == 1){
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