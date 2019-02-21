package com.aticatac.server.components;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.model.Command;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.controller.TankController;
import com.aticatac.server.gameManager.Manager;
import com.aticatac.server.prefabs.TankObject;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class GameManager extends Component {
    private HashMap<String, GameObject> playerMap = new HashMap<>();

    /**
     * Instantiates a new Component.
     *
     * @param gameObject the component parent
     */
    public GameManager(GameObject gameObject) {
        super(gameObject);
        try {
            new GameObject("Player Container", this.getGameObject());
        } catch (Exception ignored) {
        }
    }

    public HashMap<String, GameObject> getPlayerMap() {
        return playerMap;
    }

    public void addPlayer(String player) {
        if (!playerMap.containsKey(player)) {
            playerMap.put(player, createTank());
        }
    }

    public void removeClient(String username) {
        if (playerMap.containsKey(username)) {
            playerMap.remove(username);
        }
    }

    //TODO get this to call the correct commands for the correct tank
    //get the children for the game object which connects to all the tanks etc.
    //then look through that list and get the name? then should be able to call the gameobject?
    //when have correct game object then can do the below
    public void playerInput(String player, Command cmd) {
        var tank = playerMap.get(player);
        switch (cmd) {
            case UP:
                //tank.getComponent(TankController.class).moveUp();
                logger.info("Player: " + player + " sent command: " + cmd);
                break;
            case DOWN:
                //tank.getComponent(TankController.class).moveDown();
                logger.info("Player: " + player + " sent command: " + cmd);
                break;
            case LEFT:
                //tank.getComponent(TankController.class).moveLeft();
                logger.info("Player: " + player + " sent command: " + cmd);
                break;
            case RIGHT:
                //tank.getComponent(TankController.class).moveRight();
                logger.info("Player: " + player + " sent command: " + cmd);
                break;
            case SHOOT:
                //tank.getComponent(TankController.class).shoot();
                logger.info("Player: " + player + " sent command: " + cmd);
        }
    }

    public TankObject createTank() {
        try {
            return new TankObject(getGameObject().getChildren().get(0),
                    //TODO make the name here unique so it can be found
                    "Tank",
                    new Position(ThreadLocalRandom.current().nextInt(Manager.INSTANCE.getMin(), Manager.INSTANCE.getMax() + 1),
                            ThreadLocalRandom.current().nextInt(Manager.INSTANCE.getMin(), Manager.INSTANCE.getMax() + 1)),
                    100,
                    30);
        } catch (InvalidClassInstance invalidClassInstance) {
            invalidClassInstance.printStackTrace();
        } catch (ComponentExistsException e) {
            e.printStackTrace();
        }
        return null;
    }
}
