package com.aticatac.server.components.models;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
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
        //Bullet bullet = new Bullet(Transform.getX(), Transform.getY(), Transform.getRotation()); // NEED TO CHANGE PARAMS FOR BULLET
        //bullet.moveForwards();
        return true;
    }

}
