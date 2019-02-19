package com.aticatac.server.components.controller;

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

    public boolean shoot () {
        //TODO shooting
        //BulletController bullet = new BulletController(Transform.getX(), Transform.getY(), Transform.getRotation()); // NEED TO CHANGE PARAMS FOR BULLET
        //bullet.moveForwards();
        return true;
    }

}
