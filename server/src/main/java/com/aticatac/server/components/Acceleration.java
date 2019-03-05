package com.aticatac.server.components;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;

/**
 * Speed power up is a power up which, when picked up, will allow the tank to move faster.
 * Therefore, if the speed power up component is attached to a tank then it will provide a lower friction coefficient,
 *
 * @author Claire Fletcher
 */

public class Acceleration extends Component {

    /**The Friction Coefficient that this power up provides*/
    private int frictionCoefficient = 0;

    private boolean powerUpExists = false;

    /**
     * Creates a new SpeedPowerUp with a parent.
     * @param componentParent The parent of the SpeedPowerUp.
     */
    public Acceleration (GameObject componentParent) {
        super(componentParent);
    }


    /**
     * gets acceleration.
     * @return The frictionCoefficient.
     */
    public int getFrictionCoefficient(){
        return frictionCoefficient;
    }


    /**
     *
     * @return
     */
    public boolean getPowerUpExists(){

        return powerUpExists;

    }


    /**
     *
     * @param exists
     */
    public void setPowerUpExists(boolean exists){

        powerUpExists = exists;

    }


}