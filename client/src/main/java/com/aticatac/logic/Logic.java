package com.aticatac.logic;
public class Logic {


    // where to have round starting and ending? upon entering game, check if name appears anywhere currently online?
    public static void createNewTank(){
// determine whether ai, put behavioural trees from ai if so
    }

    public static void moveForward(char currentDirection, int currentXCoord, int currentYCoord){
        // Talk to physics, it will return whether can move, if can then tell renderer it moves and where

    }

    public static void turnLeft(char currentDirection) {

    }

    public static void turnRight(char currentDirection) {

    }

    public static void shoot() {
// create bullet object
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
Reload
getAmmo()
 */
}
