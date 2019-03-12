package com.aticatac.common.components;

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

    /**Power up exists check*/
    private boolean powerUpExists = false;

    /**
     * Creates a new Acceleration component with a parent.
     * @param componentParent The parent of the Acceleration component
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
     * Gets it the power up exists
     * @return boolean whether power up exists or not
     */
    public boolean getPowerUpExists(){

        return powerUpExists;

    }


    /**
     *Sets whether the power up exists or not
     * @param exists boolean true if the power up exists, false if not.
     */
    public void setPowerUpExists(boolean exists){

        powerUpExists = exists;

    }


}