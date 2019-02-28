package com.aticatac.server.prefabs;

import com.aticatac.common.components.Texture;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TankObject extends GameObject {
    //add in a parameter boolean which is ai true or false
    //TODO add in the parameter changes everywhere
    public TankObject(GameObject parent, String name, Position p, int health, int ammo)
        throws InvalidClassInstance, ComponentExistsException {
        super(name, parent, ObjectType.TANK);
        this.getComponent(Transform.class).setPosition(p.getX(), p.getY());
        new GameObject("TankBottom", this);
        new GameObject("TankTop", this);
        this.getChildren().get(0).getComponent(Transform.class).setPosition(p.getX(), p.getY());
        this.getChildren().get(1).getComponent(Transform.class).setPosition(p.getX() + 10, p.getY() + 10);
        this.getChildren().get(1).addComponent(Texture.class).setTexture("img/TankTop.png");
        this.getChildren().get(0).addComponent(Texture.class).setTexture("img/TankBottom.png");
        // determine whether ai, put behavioural trees from ai if so
        this.addComponent(Health.class).setHealth(health);
        this.addComponent(Ammo.class).setAmmo(ammo);
        this.addComponent(Physics.class);
        this.addComponent(Time.class);
        this.addComponent(TankController.class);
    }
}
