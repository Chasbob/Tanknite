package com.aticatac.tank;

public class Tank {
    private String nickname;
    private int currentXCoord;
    private int currentYCoord;
    private char currentDirection;

    public Tank (String name){
        nickname = name;
    }

    public void setCurrentXCoord (int xCoord){
        currentXCoord = xCoord;
    }

    public void setCurrentYCoord (int yCoord){
        currentYCoord = yCoord;
    }

    public void setCurrentXCoord (char direction){
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
