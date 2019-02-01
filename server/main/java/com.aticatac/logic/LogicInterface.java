package com.aticatac.logic;

import com.aticatac.tank.Tank;

import java.util.HashMap;

public class LogicInterface {
// change to server side, have key presses on client side
// This will probably be in the Map class, takes command from networking and runs method in Tank, returning boolean of
// whether that action ius possible
    // multiplayer
private static HashMap<String, Tank> tanks = new HashMap<>();

    // where to have round starting and ending? upon entering game, check if name appears anywhere currently online?
    // how to get name from user
    public static void createNewTank(String name) {
        tanks.put(name, new Tank());
    }
    public static boolean runCommand (Command command, String name){
        switch(command){
            case UP : return tanks.get(name).moveForwards();
                break;
            case LEFT : return tanks.get(name).moveRight();
                break;
            case RIGHT : return tanks.get(name).moveBackwards();
                break;
            case DOWN : return tanks.get(name).moveLeft();
                break;
            case SHOOT : return tanks.get(name).shoot();
        }
    }
    // single player
    public static boolean runCommand (Command command){
        switch(command){
            case UP : tanks.get(name).setCurrentDirection('E');
                break;
            case LEFT : tanks.get(name).setCurrentDirection('S');
                break;
            case RIGHT : tanks.get(name).setCurrentDirection('W');
                break;
            case DOWN : tanks.get(name).setCurrentDirection('N');
                break;
            case SHOOT : tanks.get(name).shoot();
        }

    }


    public static void gameFinish () {
// only one tank left
    }

    public static int numberOfAliveTanks () {
        int numberOfTanks = 0;
        return numberOfTanks;
    }


}
