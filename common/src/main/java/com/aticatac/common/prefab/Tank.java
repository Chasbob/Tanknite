package com.aticatac.common.prefab;

import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.objectsystem.GameObject;

public class Tank extends GameObject {
    //public GameObject top,base;

    /**
     * Instantiates a new GameObject.
     *
     * @param name   the name
     * @param parent the parent
     */
    public Tank(String name, GameObject parent, Position p) throws ComponentExistsException, InvalidClassInstance {
        super(name, parent);
        this.getComponent(Transform.class).SetTransform(p.x,p.y);

        new GameObject("TankBottom",this);
        new GameObject("TankTop",this);
        new GameObject("HealthBar", this);

        this.children.get(0).getComponent(Transform.class).SetTransform(p.x,p.y);
        this.children.get(1).getComponent(Transform.class).SetTransform(p.x+10,p.y+10);
        //this.children.get(2).getComponent(Transform.class).SetTransform(p.x, p.y+30);
    }
}
