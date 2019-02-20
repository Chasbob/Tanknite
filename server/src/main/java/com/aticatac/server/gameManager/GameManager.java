package com.aticatac.server.gameManager;

import com.aticatac.common.components.transform.Position;
import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.model.Command;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.controller.TankController;
import com.aticatac.server.prefabs.TankObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class GameManager {
    public HashMap<String, GameObject> playerMap = new HashMap<>();
    private GameObject root;
    private static final int min = 0, max = 640;

    public GameManager() {
        try {
            root = new GameObject("Root");

            new GameObject("Tank Container", root);

        } catch (Exception unchecked) {
        }
    }

    public void addClient(String username){
        if(!playerMap.containsKey(username)) {
            playerMap.put(username,createTank());
        }
    }

    public void removeClient(String username){
        if(playerMap.containsKey(username)) {
            playerMap.remove(username);
        }
    }

    public TankObject createTank(){
        try {
            return new TankObject(root.getChildren().get(0),
                    "Tank",
                    new Position(ThreadLocalRandom.current().nextInt(min, max + 1),
                            ThreadLocalRandom.current().nextInt(min, max + 1)),
                    100,
                    30);
        } catch (InvalidClassInstance invalidClassInstance) {
            invalidClassInstance.printStackTrace();
        } catch (ComponentExistsException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void playerInput(String player, Command cmd) {
        var tank = playerMap.get(player);
        switch (cmd) {
            case UP:
                tank.getComponent(TankController.class).moveUp();
                break;
            case DOWN:
                tank.getComponent(TankController.class).moveDown();
                break;
            case LEFT:
                tank.getComponent(TankController.class).moveLeft();
                break;
            case RIGHT:
                tank.getComponent(TankController.class).moveRight();
                break;
            case SHOOT:
                tank.getComponent(TankController.class).shoot();
        }
        System.out.println("printing input" + player);
    }

    public void EndOfGame() {
        Iterator it = playerMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            removeClient((String)pair.getKey());
            it.remove();
        }
    }
}
