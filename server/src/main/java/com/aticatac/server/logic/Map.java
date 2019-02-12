package com.aticatac.server.logic;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;

public class Map extends Component {

    public Map(GameObject componentParent){
        super(componentParent);
    }

    // where to have round starting and ending? upon entering game, check if name appears anywhere currently online?
    // how to get name from user
    public void createNewTank(String name){
        //new Tank(componentParent);
        // determine whether ai, put behavioural trees from ai if so
    }

    /**
     * call method when up arrow pressed
     * @
     */
    public static void moveForward(String name){
        /*currentDirection = tanks.get(name).getCurrentDirection();
        currentXCoord = tanks.get(name).getCurrentXCoord();
        currentYCoord = tanks.get(name).getCurrentYCoord();*/
        // Talk to physics, it will return whether can move, if can then tell renderer it moves and where

    }

    //get name of tank somehoooooow
    // call method when left arrow pressed
    //Map.turnLeft(name);

    /**
     *
     * @param name the
     */


    // call method when space bar pressed
    public static void shoot(String name, int damage) {
//        currentDirection = tanks.get(name).getCurrentDirection();
//        currentXCoord = tanks.get(name).getCurrentXCoord();
//        currentYCoord = tanks.get(name).getCurrentYCoord();
//        Bullet bullet = new Bullet(damage, currentXCoord, currentYCoord, currentDirection);


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
