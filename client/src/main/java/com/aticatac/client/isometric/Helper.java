package com.aticatac.client.isometric;


public class Helper {
    public static int tileToScreenX(int x, int y) {
        x = 1920 - x;
        y = 1920 - y;
        return ((x + y)) / 2;
    }

    public static int tileToScreenY(int x, int y) {
        x = 1920 - x;
        y = 1920 - y;
        return (x - y) / 4;
    }

    public static int screenXtoTile(int x, int y){
        return 2*x-y;
    }

    public static int screenYtoTile(int x, int y){
        return -(4*y-x);
    }
}
