package com.aticatac.server.components;

import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.Component;

public class Transform extends Component {
    public Transform (GameObject parent){
        super(parent);
    }

    private double X =0;
    private double Y =0;

    private double rotation = 0;

    public double getX(){return X;}
    public double getY(){return Y;}

    public void transform(double x, double y){
        setTransform(X+x,Y+y);
    }

    public void setTransform(double x, double y) {
        X = x;
        Y = y;
    }

    public void forward(double v) {
        double xFactor = Math.sin(Math.toRadians(getRotation()));
        double yFactor = -Math.cos(Math.toRadians(getRotation()));
        transform(v*xFactor,v*yFactor);
    }

    public void setRotation(double r){
        rotation = r;
    }

    public void rotate(double r){
        rotation = rotation + r;
        setRotation(rotation);
    }

    public double getRotation(){
        return rotation;
    }
}
