package com.aticatac.server.components.models;

public class Turret {

    // does the shooting etc, linked to main tank
    // able to shoot 360 degrees
    // game object or component?
    // child of tank
    // has transform component for rotation
    // move same as tank?

    // shoot, take rotation of turret from transform component as an angle and send as param to physics

    double currentRotation = this.getComponent(Transform.class).getRotation();

}
