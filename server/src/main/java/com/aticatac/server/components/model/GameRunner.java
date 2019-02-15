package com.aticatac.server.components.model;

public class GameRunner {


    private int roundNumber = 0;

    public void start() {

    while (roundNumber != 3){
        new Map();
        // get tanks usernames and fill in rest with computers
        for (int i = 0; i < 10; i++) {
            Map.createNewTank("" + i);
        }
        //hashmap and tanks

    }




        //clear map

    }
}
