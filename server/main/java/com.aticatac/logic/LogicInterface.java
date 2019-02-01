package com.aticatac.logic;

import com.aticatac.tank.Tank;

import java.util.HashMap;

/**
 * The type Logic interface.
 */
public class LogicInterface {
// change to server side, have key presses on client side
// This will probably be in the Map class, takes command from networking and runs method in Tank, returning boolean of
// whether that action ius possible
    // multiplayer
private static HashMap<String, Tank> tanks = new HashMap<>();

    /**
     * Create new tank.
     *
     * @param name the name
     */
// where to have round starting and ending? upon entering game, check if name appears anywhere currently online?
    // how to get name from user
    public static void createNewTank(String name) {
        tanks.put(name, new Tank(physicsManager.getStartingXCoordinate, physicaManager.getStartingYCoordinate));
    }

    /**
     * Run command boolean.
     *
     * @param command the command
     * @param name    the name
     * @return the boolean
     */
    public static boolean runCommand (Command command, String name){
        switch(command){
            case UP : return tanks.get(name).moveForwards();
                break;
            case LEFT : return tanks.get(name).moveLeft();
                break;
            case RIGHT : return tanks.get(name).moveRight();
                break;
            case DOWN : return tanks.get(name).moveBackwards();
                break;
            case SHOOT : return tanks.get(name).shoot();
        }
    }

    /**
     * Run command boolean.
     *
     * @param command the command
     * @return the boolean
     */
// single player
    public static boolean runCommand (Command command){
        switch(command){
            case UP : moveForwards();
                break;
            case LEFT : moveLeft();
                break;
            case RIGHT : moveRight();
                break;
            case DOWN : moveDown();
                break;
            case SHOOT : shoot();
        }
    }

    /**
     * Game finish.
     */
// Method so that if all tanks out of ammo, ammo spawn more likely
    public static void gameFinish () {
// only one tank left
    }

    /**
     * Number of alive tanks int.
     *
     * @return the int
     */
    public static int numberOfAliveTanks () {
        int numberOfTanks = 0;
        return numberOfTanks;
    }


}
