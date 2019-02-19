package com.aticatac.server.gameManager;

import com.aticatac.common.components.transform.Position;
import com.aticatac.common.model.Command;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.common.objectsystem.RootObject;
import com.aticatac.common.prefab.Tank;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class GameManager {
    public HashMap<Integer, GameObject> playerMap = new HashMap<>();
    private RootObject rootAbstract;
    private GameObject root;

    public GameManager(int numberOfPlayers) {
        try {
            rootAbstract = new RootObject("Root");
            root = new GameObject("Root", rootAbstract);
            var tankContainer = new GameObject("Tank Container", root);
            int min = 0, max = 640;
            for (int i = 1; i <= numberOfPlayers; i++) {
                //Initalise Tank
                var tank = new Tank("Tank" + i,
                        tankContainer,
                        new Position(ThreadLocalRandom.current().nextInt(min, max + 1),
                                ThreadLocalRandom.current().nextInt(min, max + 1)));
                //Insert Tank into Hashmap
                playerMap.put(i, tank);
            }
        } catch (Exception unchecked) {
        }
    }

    public void playerInput(int player, Command cmd) {
        var tank = playerMap.get(player);
        switch (cmd) {
            case UP:
                tank.transform.SetRotation(0);
                tank.transform.Forward(-3);
                break;
            case DOWN:
                tank.transform.SetRotation(180);
                tank.transform.Forward(-3);
                break;
            case LEFT:
                tank.transform.SetRotation(-90);
                tank.transform.Forward(-3);
                break;
            case RIGHT:
                tank.transform.SetRotation(90);
                tank.transform.Forward(-3);
                break;
        }
    }

    public void EndOfGame() {
    }
}
