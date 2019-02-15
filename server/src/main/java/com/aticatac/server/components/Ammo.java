package com.aticatac.server.components;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;
public class Ammo extends Component{
    private int ammo = 0;

    public Ammo(GameObject parent) {
        super(parent);
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

}
