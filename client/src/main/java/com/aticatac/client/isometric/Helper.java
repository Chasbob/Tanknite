package com.aticatac.client.isometric;

import com.aticatac.common.components.transform.Position;


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
}
