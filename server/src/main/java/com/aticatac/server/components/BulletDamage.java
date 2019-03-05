package com.aticatac.server.components;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;

public class BulletDamage extends Component{

    /**Power up exists */
    private boolean powerUpExists = false;

    /**
     *
     * @param gameObject
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
