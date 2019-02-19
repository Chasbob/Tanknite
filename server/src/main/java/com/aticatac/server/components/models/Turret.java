package com.aticatac.server.components.models;

import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.Position;
import com.aticatac.server.components.Transform;

public class Turret extends GameObject {

    public Turret (GameObject Parent, String name){

        super(Parent, name);
        this.addComponent(Transform.class);
        this.addComponent(Position.class);

        // does the shooting etc, linked to main tank
        // able to shoot 360 degrees
        // game object or component?
        // child of tank
        // has transform component for rotation
        // move same as tank?

        // shoot, take rotation of turret from transform component as an angle and send as param to physics

        double currentRotation = this.getComponent(Transform.class).getRotation();
    }

    public boolean shoot () {

        Bullet bullet = new Bullet(Transform.getX(), Transform.getY(), Transform.getRotation()); // NEED TO CHANGE PARAMS FOR BULLET
        bullet.moveForwards();


    }

}
