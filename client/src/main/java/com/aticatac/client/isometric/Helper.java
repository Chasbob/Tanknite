package com.aticatac.client.isometric;


import com.aticatac.server.transform.Position;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Helper {
    public static Position CartesianToIsometric(int x, int y){
        var pos = new Position();
        var rotation = Math.toRadians(-45);
        pos.setX((int)(x*Math.cos(rotation)-y*Math.sin(rotation)));
        pos.setY((int)(x*Math.sin(rotation)+y*Math.cos(rotation)));
        pos.setY(pos.getY()/2);
        return pos;
    }

    public static Position IsometricToCartesian(int x, int y){
        var pos = new Position();
        var rotation = Math.toRadians(45);
        pos.setX((int)
                (    (y*Math.sin(rotation))
                    /(2*(Math.cos(rotation)-1)) ));

        pos.setY((int)
                (    (x*Math.sin(rotation))
                        /(1-0.5f*Math.cos(rotation))) );

        return pos;
    }

    public static int tileToScreenX(int x, int y) {
        return ((x + y)) /2;
    }

    public static int tileToScreenY(int x, int y) {
        return ((x) - (y)) / 4;
    }

    public int screenToMapX(int x, int y) {
        return ((x / 16) + (y / 16));
    }
//  public int screenToMapY(int x,int y){
//    return
//  }
}
