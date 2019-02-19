package com.aticatac.server.components.models;

import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.*;

public class TankObject extends GameObject {

    public TankObject (GameObject Parent, String name) throws InvalidClassInstance, ComponentExistsException {
        // work out where to give starting co ordinates for tank when gets created, change params to work for this when
        // tank is created
        // need to link turret to tank
        super(name,Parent);

        GameObject Turret = new GameObject("Turret",this); // will turret move when tank does if turret is child of tank?
        this.addComponent(Health.class);
        this.addComponent(AI.class);
        this.addComponent(Ammo.class);
        this.addComponent(PhysicsManager.class);
        this.addComponent(Time.class);
        this.getComponent(Health.class).setHealth(100);
        this.getComponent(Ammo.class).setAmmo(30);

        //this.getComponent(Transform.class).SetTransform(this.getComponent(PhysicsManager.class).initialisePosition(name));
        // where to set starting coords? this.getComponent(Transform.class).setTransform(xCoord, yCoord);
    }
}
