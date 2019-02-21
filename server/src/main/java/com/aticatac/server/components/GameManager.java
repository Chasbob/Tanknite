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
            playerMap.put(player, createTank(player, false));
        }
    }

    //TODO addAI which passes in that AI is true.
    //This needs to be called upon the start of the game as all clients shld then be added.
    public void addAI(String name){

        //IN here it could check the count to know how many times it needs to create an AI
        //count how many times the addPLayer is called.

        if (!playerMap.containsKey(name)){
            playerMap.put(name, createTank(name, true));
        }

    }

    public void removeClient(String username) {
        if (playerMap.containsKey(username)) {
            playerMap.remove(username);
        }
    }

    public void playerInput(String player, Command cmd) {
        //Gets the tank that the command came from
        var tank = playerMap.get(player);
        switch (cmd) {
            case UP:
                tank.getComponent(TankController.class).moveUp();
                logger.info("Player: " + player + " sent command: " + cmd);
                break;
            case DOWN:
                tank.getComponent(TankController.class).moveDown();
                logger.info("Player: " + player + " sent command: " + cmd);
                break;
            case LEFT:
                tank.getComponent(TankController.class).moveLeft();
                logger.info("Player: " + player + " sent command: " + cmd);
                break;
            case RIGHT:
                tank.getComponent(TankController.class).moveRight();
                logger.info("Player: " + player + " sent command: " + cmd);
                break;
            case SHOOT:
                tank.getComponent(TankController.class).shoot();
                logger.info("Player: " + player + " sent command: " + cmd);
        }
    }

    public TankObject createTank(String player, boolean isAI) {
        try {

            Position position;

            TankObject tank = new TankObject(getGameObject().getChildren().get(0),
                    player,
                    position = new Position(ThreadLocalRandom.current().nextInt(Manager.INSTANCE.getMin(), Manager.INSTANCE.getMax() + 1),
                            ThreadLocalRandom.current().nextInt(Manager.INSTANCE.getMin(), Manager.INSTANCE.getMax() + 1)),
                    100,
                    30,
                    isAI);

            DataServer.INSTANCE.setCoordinates(position, "Tank");

            return tank;

        } catch (InvalidClassInstance invalidClassInstance) {
            invalidClassInstance.printStackTrace();
        } catch (ComponentExistsException e) {
            e.printStackTrace();
        }
        return null;
    }
}
