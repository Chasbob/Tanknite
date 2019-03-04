package com.aticatac.server.components;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;

/**
 *
 */
public class HealthPowerUp extends Component {

    /**Health increase this power up provides*/
    private int health = 10;

    /**
     * Creates a new health power up with a parent
     * @param gameObject
     */
    public HealthPowerUp(GameObject gameObject) {
        super(gameObject);
    }


    /**
     *
     * @return
     */
    public int getHealthIncrease(){

        return health;

    }


}
