package com.aticatac.common.components.transform;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;

public class Transform extends Component {

    private Position position;

    private double rotation = 0;

    public Transform(GameObject parent) {
        super(parent);
    }

    public Position GetPosition(){return position;}

    public void SetPosition(Position pos){position = pos;}

    public void Transform(double x, double y){
        SetTransform(position.x+x,position.y+y);
    }

    public void SetTransform(double x, double y) {
        position.x = x;
        position.y = y;
    }

    public void Forward(double v) {
        double xFactor = Math.sin(Math.toRadians(GetRotation()));
        double yFactor = -Math.cos(Math.toRadians(GetRotation()));
        Transform(v*xFactor,v*yFactor);
    }

    public void SetRotation(double r){
        rotation = r;
    }

    public void Rotate(double r){
        rotation = rotation + r;
        SetRotation(rotation);
    }

    public double GetRotation(){
        return rotation;
    }
}
