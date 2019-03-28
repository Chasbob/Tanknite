package com.aticatac.client.isometric;


import com.aticatac.client.server.Position;

public class Helper {
    public static int tileToScreenX(int x, int y) {
        x = 1920 - x;
        y = 1920 - y;
        return (x + y) / 2;
    }

    public static int tileToScreenY(int x, int y) {
        x = 1920 - x;
        y = 1920 - y;
        return (x - y) / 4;
    }

    public static float tileToScreenX(float x, float y) {
        x = 1920 - x;
        y = 1920 - y;
        return (x + y) / 2;
    }

    public static float tileToScreenY(float x, float y) {
        x = 1920 - x;
        y = 1920 - y;
        return (x - y) / 4;
    }

    public static Position tileToScreen(Position p){
        int x = 1920 - p.getX();
        int y = 1920 - p.getY();
        return new Position((x + y) / 2, (x - y) / 4);
    }

    public static int screenXtoTile(int x, int y){
        return 1920 - ((2*x)-y);
    }

    public static int screenYtoTile(int x, int y){
        return 1920 - (-(4*y-x));
    }

    public static Position screenToTile(Position p){
        var p2 = new Position();

        p2.setX((p.getX() * 2 + p.getY() * 4  ) /2);
        p2.setY((p.getY() * 4 -(p.getX() * 2) ) /2);


        p2.setX(1920-p2.getX());
        p2.setY(p2.getY()+1920);
        return p2;
    }
}
