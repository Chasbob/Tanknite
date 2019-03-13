package com.aticatac.server.components.controller;

import com.aticatac.common.components.Component;
import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.BulletDamage;
import com.aticatac.server.components.Damage;
import com.aticatac.server.prefabs.BulletObject;

public class TurretController extends Component {
  /**
   * Instantiates a new Component.
   *
   * @param gameObject the component parent
   */
  public TurretController(GameObject gameObject) {
    super(gameObject);
  }

  public boolean shoot() {

    //create a bullet object
    BulletObject bullet = null; // get name of bullet
    try {
      bullet = new BulletObject("bullet", this.getGameObject());
    } catch (InvalidClassInstance | ComponentExistsException invalidClassInstance) {
      invalidClassInstance.printStackTrace();
    }

    if (this.getGameObject().getComponent(BulletDamage.class).getPowerUpExists()) {
      bullet.getComponent(Damage.class).setDamage(20);
    }

    bullet.getComponent(BulletController.class).moveForwards(); // check if tank has bullet power up component and increase damage if does
    //moves forwards until collision happens then it will be destroyed.
    //constantly check if collided() method is returning true


    return true;
  }

}
