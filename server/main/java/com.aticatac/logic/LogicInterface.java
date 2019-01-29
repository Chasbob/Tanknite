package com.aticatac.logic;

import com.aticatac.bullet.Bullet;
import com.aticatac.tank.Tank;

import java.util.HashMap;

public class LogicInterface {
// change to server side, have key presses on client side
    private static HashMap<String, Tank> tanks = new HashMap<>();

    // where to have round starting and ending? upon entering game, check if name appears anywhere currently online?
    // how to get name from user
    public static void createNewTank(String name){
        tanks.put(name, new Tank());
// determine whether ai, put behavioural trees from ai if so
    }


    //when sound plays check all tanks in that area and play noise to all those players
    /**
     * call method when up arrow/w pressed
     * @
     */
    public static void moveForwards(String name){
        int currentXCoord = tanks.get(name).getCurrentXCoord();
        int currentYCoord = tanks.get(name).getCurrentYCoord();
        // Talk to physics, it will return whether can move, if can then tell renderer it moves and where

    }

    // call method when left arrow pressed
    //Logic.turnLeft(name);

    // when right arrow/d pressed
    public static void moveRight (String name){
        int currentXCoord = tanks.get(name).getCurrentXCoord();
        int currentYCoord = tanks.get(name).getCurrentYCoord();

    }

    // when left arrow/a pressed
    public static void moveLeft (String name){
        int currentXCoord = tanks.get(name).getCurrentXCoord();
        int currentYCoord = tanks.get(name).getCurrentYCoord();

    }

    // when down arrow/s pressed
    public static void moveBackwards (String name){
        int currentXCoord = tanks.get(name).getCurrentXCoord();
        int currentYCoord = tanks.get(name).getCurrentYCoord();

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
        if (numberOfAliveTanks() == 1){
            gameFinish();
        }
    }

    public static void getHealth () {

    }

    public static void gameFinish () {
// only one tank left
    }

    public static int numberOfAliveTanks () {
        int numberOfTanks = 0;
        return numberOfTanks;
    }

/*Potential:
PickUpItem
getAmmo()
 */
}
