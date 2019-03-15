package com.aticatac.server.prefabs;

import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.server.components.BulletCollisionBox;
import com.aticatac.server.components.Damage;
import com.aticatac.server.components.Time;
import com.aticatac.server.components.controller.BulletController;
import com.aticatac.server.components.physics.Physics;
import com.aticatac.server.objectsystem.GameObject;

public class BulletObject extends GameObject {
    /**
     * Instantiates a new GameObject.
     *
     * @param name   the name
     * @param parent the parent
     * @throws InvalidClassInstance     the invalid class instance
     * @throws ComponentExistsException the component exists exception
     */
    public BulletObject(String name, GameObject parent) throws InvalidClassInstance, ComponentExistsException {
        super(name, (parent));
        this.addComponent(Physics.class);
        this.addComponent(Time.class);
        this.addComponent(BulletController.class);
        this.addComponent(Damage.class);
        this.addComponent(BulletCollisionBox.class);
    }
}
