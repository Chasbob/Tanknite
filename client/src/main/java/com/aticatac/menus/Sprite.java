package com.aticatac.menus;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Sprite
{
    //TODO physics/intersection part of class

    private Image baseImage;
    private Image image;
    private double X =0;
    private double Y =0;
    private double rotation = 0;

    public void render(GraphicsContext gc)
    {
        gc.drawImage( image, X, Y);
    }

    public void Transform(double x, double y){
        SetTransform(X+x,Y+y);
    }

    public void SetTransform(double x, double y)
    {X = x;Y = y;}

    public void SetRotation(double r)
    {
        rotation = r;

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        ImageView iv = new ImageView(baseImage);
        iv.setRotate(rotation);

        image = iv.snapshot(params, null);
    }

    public void Rotate(double r){
        rotation = rotation + r;
        SetRotation(rotation);
    }

    public double GetRotation(){return rotation;}

    void SetImage(Image i){baseImage = i;image = baseImage;}

    public Image getImage(){return image;}
}