package com.aticatac.server.prefabs;

import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.*;
import com.aticatac.server.components.ai.AI;
import com.aticatac.server.components.controller.TankController;

import java.util.HashMap;

public class TankObject extends GameObject {

    //add in a parameter boolean which is ai true or false
    //TODO add in the parameter changes everywhere
    public TankObject (GameObject Parent, String name, Position p,int health,int ammo) throws InvalidClassInstance, ComponentExistsException {
        super(name, Parent);

        this.getComponent(Transform.class).SetTransform(p.x, p.y);

        new GameObject("TankBottom", this);
        new GameObject("TankTop", this);

        this.getChildren().get(0).getComponent(Transform.class).SetTransform(p.x, p.y);
        this.getChildren().get(1).getComponent(Transform.class).SetTransform(p.x + 10, p.y + 10);

        // determine whether ai, put behavioural trees from ai if so

        this.addComponent(Health.class).setHealth(health);
        this.addComponent(Ammo.class).setAmmo(ammo);
        this.addComponent(Physics.class);
        this.addComponent(Time.class);
        this.addComponent(TankController.class);

//        if(isAI){
//            this.addComponent(AI.class);
//        }



    }
}
