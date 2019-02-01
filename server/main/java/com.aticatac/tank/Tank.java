package com.aticatac.tank;

import com.aticatac.bullet.Bullet;
import com.aticatac.logic.LogicInterface;

import java.util.HashMap;

public class Tank {
    private int currentXCoord;
    private int currentYCoord;
    private char currentDirection;

    public Tank (){
    }

// determine whether ai, put behavioural trees from ai if so



    //when sound plays check all tanks in that area and play noise to all those players
    /**
     * call method when up arrow/w pressed
     * @
     */
    //update to account for move to Tank class
    public static boolean moveForwards() {
        int [] movement = physicsManager.forwards(int currentXCoord, int currentYCoord);
        currentXCoord = movement[1];
        currentYCoord = movement[2];
        if (movement[0] == 0) return false;
        else return true;
        // physics returns an array of the boolean, and then 2 co ordinates it moves to
        // Talk to physics, it will return whether can move, if can then tell renderer it moves and where
        // co ordinates can be changed in physics
    }

    // when right arrow/d pressed
    public static boolean moveRight (){
        int [] movement = physicsManager.right(int currentXCoord, int currentYCoord);
        currentXCoord = movement[1];
        currentYCoord = movement[2];
        if (movement[0] == 0) return false;
        else return true;
    }

    // when left arrow/a pressed
    public static boolean moveLeft (){
        int [] movement = physicsManager.left(int currentXCoord, int currentYCoord);
        currentXCoord = movement[1];
        currentYCoord = movement[2];
        if (movement[0] == 0) return false;
        else return true;
    }

    // when down arrow/s pressed
    public static boolean moveBackwards (){
        int [] movement = physicsManager.backwards(int currentXCoord, int currentYCoord);
        currentXCoord = movement[1];
        currentYCoord = movement[2];
        if (movement[0] == 0) return false;
        else return true;
    }

    /*
    public static void turnLeft(String name) {
        switch(tanks.get(name).getCurrentDirection()){
            case 'N' : tanks.get(name).setCurrentDirection('W');
                break;
            case 'E' : tanks.get(name).setCurrentDirection('N');
                break;
            case 'S' : tanks.get(name).setCurrentDirection('E');
                break;
            case 'W' : tanks.get(name).setCurrentDirection('S');
        }
    }
    */

    /*
    // call method when right arrow pressed
    public static void turnRight(String name) {
        switch(tanks.get(name).getCurrentDirection()){
            case 'N' : tanks.get(name).setCurrentDirection('E');
                break;
            case 'E' : tanks.get(name).setCurrentDirection('S');
                break;
            case 'S' : tanks.get(name).setCurrentDirection('W');
                break;
            case 'W' : tanks.get(name).setCurrentDirection('N');
        }
    }
    */

    // call method when space bar pressed
    public static void shoot(String name, int damage) {
        char currentDirection = tanks.get(name).getCurrentDirection();
        int currentXCoord = tanks.get(name).getCurrentXCoord();
        int currentYCoord = tanks.get(name).getCurrentYCoord();
        Bullet bullet = new Bullet(damage, currentXCoord, currentYCoord, currentDirection);

// create bullet object current co ordinatesn where shooting tank is, and move
        //
    }

    public static void isShot() {
// part of colliding? Physics calls this method?
        //if new health = 0 then die()
    }

    public static void die () {
        if (LogicInterface.numberOfAliveTanks() == 1){ // in other logic class?
            LogicInterface.gameFinish();
        }
    }
    /*Potential:
PickUpItem
getAmmo()
 */

    public static void getHealth () {

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


/* Method takes command (check branch) and returns boolean of whether possible
 */