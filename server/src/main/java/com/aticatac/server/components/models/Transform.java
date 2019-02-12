package com.aticatac.server.components.models;

public class Transform {

    private double X =0;
    private double Y =0;

    private double rotation = 0;

    public double GetX(){return X;}
    public double GetY(){return Y;}

    public void Transform(double x, double y){
        SetTransform(X+x,Y+y);
    }

    public void SetTransform(double x, double y) {
        X = x;
        Y = y;
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
