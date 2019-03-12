package com.aticatac.server.components.controller;

import com.aticatac.common.components.Ammo;
import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;

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
    //TODO shooting
    BulletController bullet = new BulletController(this.getGameObject());
    bullet.moveForwards();
    //moves forwards until collision happens then it will be destroyed.
    //constantly check if collided() method is returning true
    int currentAmmo = this.getGameObject().getComponent(Ammo.class).getAmmo();
//
//        if (currentAmmo == 0) return false;
//        return this.findObject(TurretController,) // get turret for this particular tank, and call shoot method in it
//        this.getComponent(Ammo.class).setAmmo(currentAmmo - 1);
    return true;
  }
}
