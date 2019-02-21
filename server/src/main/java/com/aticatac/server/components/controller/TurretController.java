package com.aticatac.server.components.controller;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;
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

    public boolean shoot () {
        //TODO shooting
        BulletObject bullet = new BulletObject(this.getGameObject()); // get name of bullet
        bullet.getComponent(BulletController.class).moveForwards();
        //moves forwards until collision happens then it will be destroyed.
        //constantly check if collided() method is returning true
        return true;
    }

}
