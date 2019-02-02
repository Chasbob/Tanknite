package com.aticatac.common.components;

import com.aticatac.common.objectsystem.GameObject;

public class SpeedPowerUp extends Component {

    private int frictionCoefficient = 0;

    /**
     * Constructor for component: speedPowerUp.
     * @param parent
     */
    public SpeedPowerUp (GameObject parent) {
        super(parent);
    }

    /**
     * gets acceleration.
     * @return
     */
    public int getFrictionCoefficient(){
        return frictionCoefficient;
    }

}