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

    public TankObject (GameObject Parent, String name, Position p,int health,int ammo) throws InvalidClassInstance, ComponentExistsException {
        super(name,Parent);

        this.getComponent(Transform.class).SetTransform(p.x,p.y);

        new GameObject("TankBottom",this);
        new GameObject("TankTop",this);

        Texture t = new Texture(Gdx.files.internal("img/tank.png"));

        this.getChildren().get(0).getComponent(Transform.class).SetTransform(p.x,p.y);
        this.getChildren().get(1).getComponent(Transform.class).SetTransform(p.x +t.getWidth()/2f,p.y);

        // determine whether ai, put behavioural trees from ai if so
        this.addComponent(Health.class).setHealth(health);
        this.addComponent(Ammo.class).setAmmo(ammo);
        this.addComponent(Physics.class);
        this.addComponent(Time.class);
        this.addComponent(TankController.class);
    }
}
