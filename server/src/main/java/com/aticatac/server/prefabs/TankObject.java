package com.aticatac.server.prefabs;

import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.objectsystem.GameObject;
<<<<<<<<< Temporary merge branch 1
import com.aticatac.common.objectsystem.ObjectType;
=========
>>>>>>>>> Temporary merge branch 2
import com.aticatac.server.components.*;
import com.aticatac.server.components.ai.AI;
import com.aticatac.server.components.controller.TankController;

import java.util.HashMap;

public class TankObject extends GameObject {
    //add in a parameter boolean which is ai true or false
    //TODO add in the parameter changes everywhere
    public TankObject(GameObject parent, String name, Position p, int health, int ammo, boolean isAI) throws InvalidClassInstance, ComponentExistsException {
        super(name, parent, ObjectType.TANK);
        this.getComponent(Transform.class).setPosition(p.getX(), p.getY());
        new GameObject("TankBottom", this);
        new GameObject("TankTop", this);
        this.getChildren().get(0).getComponent(Transform.class).setPosition(p.getX(), p.getY());
        this.getChildren().get(1).getComponent(Transform.class).setPosition(p.getX() + 10, p.getY() + 10);
        this.getChildren().get(0).addComponent(Texture.class).Texture = "img/TankTop.png";
        this.getChildren().get(1).addComponent(Texture.class).Texture = "img/TankBottom.png";
        // determine whether ai, put behavioural trees from ai if so
        this.addComponent(Health.class).setHealth(health);
        this.addComponent(Ammo.class).setAmmo(ammo);
        this.addComponent(Physics.class);
        this.addComponent(Time.class);
        this.addComponent(TankController.class);
        this.addComponent(Acceleration.class);

        if(isAI){
            this.addComponent(AI.class);
        }



    }
}
