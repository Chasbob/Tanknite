package com.aticatac.common.prefab;

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
    public Tank(String name, GameObject parent) throws ComponentExistsException, InvalidClassInstance {
        super(name, parent);

        new GameObject("Tank Turret",this);
        new GameObject("Tank Base",this);
    }
}
