package com.aticatac.server.components.model;

import com.aticatac.common.model.Command;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.PhysicsManager;
import com.aticatac.server.components.models.Tank;

import java.util.HashMap;

public class Map{
    private static HashMap<String, Tank> tanks = new HashMap<>();
    private int numberOfAliveTanks;
    public Map(){
        numberOfAliveTanks = 10;
    }

    // have methods for what happens with collisions here?

    // change to server side, have key presses on client side
// This will probably be in the Map class, takes command from networking and runs method in Tank, returning boolean of
// whether that action ius possible
    // multiplayer

    /**
     * Create new tank.
     *
     *
     * @param name the name
     */
// where to have round starting and ending? upon entering game, check if name appears anywhere currently online?
    // how to get name from user
    public static void createNewTank(String name) {
        tanks.put(name, new Tank(Map, name));
        GameObject Tank = new GameObject(Map, name);
        // fix params for tank and work out how to tell starting co ords from physics
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
            case LEFT : return tanks.get(name).moveLeft();
            case RIGHT : return tanks.get(name).moveRight();
            case DOWN : return tanks.get(name).moveBackwards();
            case SHOOT : return tanks.get(name).shoot();
        }
        return false;
    }

    /**
     * Run command boolean.
     *
     * @param command the command
     * @return the boolean
     */
// single player

    // Need to know name of tank in single player, or just have same as multiplauyer?
    /*public static boolean runCommand (Command command){
        switch(command){
            case UP : return moveForwards();
            case LEFT : return moveLeft();
            case RIGHT : return moveRight();
            case DOWN : return moveDown();
            case SHOOT : return shoot();
        }
        return false;
    }
    */
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
    public int getNumberOfAliveTanks () {
        return numberOfAliveTanks;
    }

    public void setNumberOfAliveTanks (int numberOfTanks) {
        numberOfAliveTanks = numberOfTanks;
    }


}
/*
package com.aticatac.server.logic;

/**
 * The type Round logic.
 */
//public class RoundLogic {
    // starts round and calls methods from Map.java

    // Have a game manager and a TopLevelManager
//}

