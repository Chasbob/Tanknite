package com.aticatac.server.components;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;

/**
 *
 */
public class BulletPowerUp extends Component {

    /**New damage this power up provides*/
    private int damage = 20;

    /**
     * Creates a new bullet power up with a parent
     * @param gameObject
     */
    public BulletPowerUp(GameObject gameObject) {
        super(gameObject);
    }


    /**
     *
     * @return
     */
    public int getBulletDamage(){

        return damage;
    }


}