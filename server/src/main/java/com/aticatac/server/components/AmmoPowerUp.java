package com.aticatac.server.components;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;

/**
 *
 */

public class AmmoPowerUp extends Component {

    /***/
    private int ammo = 10;

    /**
     * Creates a new ammo power up with a parent
     * @param gameObject
     */
    public AmmoPowerUp(GameObject gameObject) {
        super(gameObject);
    }

    /**
     *
     * @return
     */
    public int getAmmoIncrease(){

        return ammo;

    }


}