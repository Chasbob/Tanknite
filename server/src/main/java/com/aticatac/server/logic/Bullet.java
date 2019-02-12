package com.aticatac.server.logic;

public class Bullet {
    private int currentXCoord;
    private int currentYCoord;
    private char currentDirection;
    private int damage;
    // bullet speed?
    // or different bullets for each gun/powerup?
    public Bullet (int bulletDamage, int xCoord, int yCoord, char direction){
        damage = bulletDamage;
        currentXCoord = xCoord;
        currentYCoord = yCoord;
        currentDirection = direction;

    }

    public void setCurrentXCoord (int xCoord){
        currentXCoord = xCoord;
    }

    public void setCurrentYCoord (int yCoord){
        currentYCoord = yCoord;
    }

    public void setCurrentDirection (char direction){
        currentDirection = direction;
    }

    public int getCurrentXCoord (){
        return currentXCoord;
    }

    public int getCurrentYCoord () {
        return currentYCoord;
    }

    public char getCurrentDirection () {
        return currentDirection;
    }
}
