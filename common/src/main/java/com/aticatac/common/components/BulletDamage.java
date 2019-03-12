package com.aticatac.common.components;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;

public class BulletDamage extends Component{

    /**Power up exists */
    private boolean powerUpExists = false;

    /**
     * Creates a new bulletdamage constructor with a parent
     * @param gameObject the parent of the bullet damage component
     */
    public BulletDamage(GameObject gameObject) {

        super(gameObject);


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