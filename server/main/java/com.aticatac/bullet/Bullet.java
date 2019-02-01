package com.aticatac.bullet;

public class Bullet {
    private int currentXCoord;
    private int currentYCoord;
    private char currentDirection;
    private int damage;

    public Bullet (int xCoord, int yCoord, char direction){
        currentXCoord = xCoord;
        currentYCoord = yCoord;
        currentDirection = direction;
    }

    public static void  moveForwards (){

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
