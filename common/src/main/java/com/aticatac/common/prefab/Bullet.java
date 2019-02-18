package com.aticatac.common.prefab;

import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.common.objectsystem.RootObject;

public class Bullet extends GameObject {
    /**
     * Instantiates a new GameObject.
     *
     * @param name   the name
     * @param parent the parent
     */
    public Bullet(String name, GameObject parent) throws ComponentExistsException, InvalidClassInstance {
        super(name, parent);
    }
}
