package com.aticatac.common.components;

import com.aticatac.common.objectsystem.GameObject;

public class Health extends Component {
    private int health = 0;

    public Health(GameObject parent) {
        super(parent);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}
