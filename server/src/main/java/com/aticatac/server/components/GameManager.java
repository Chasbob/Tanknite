package com.aticatac.server.components;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.model.Command;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.common.objectsystem.ObjectType;
import com.aticatac.server.components.controller.TankController;
import com.aticatac.server.gamemanager.Manager;
import com.aticatac.server.prefabs.TankObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The type Game manager.
 */
public class GameManager extends Component {
    private HashMap<String, GameObject> playerMap = new HashMap<>();
    private ArrayList<Float> playerXS;
    private ArrayList<Float> playerYS;

    /**
     * Instantiates a new Component.
     *
     * @param gameObject the component parent
     */
    public GameManager(GameObject gameObject) {
        super(gameObject);
        try {
            new GameObject("Player Container", this.getGameObject(), ObjectType.PLAYER_CONTAINER);
            this.playerXS = new ArrayList<>();
            this.playerYS = new ArrayList<>();
        } catch (Exception e) {
            this.logger.error(e);
        }
    }

    /**
     * Gets player map.
     *
     * @return the player map
     */
    public HashMap<String, GameObject> getPlayerMap() {
        return playerMap;
    }

    /**
     * Add player.
     *
     * @param player the player
     */
    public void addPlayer(String player) {
        if (!playerMap.containsKey(player)) {
            playerMap.put(player, createTank(player));
        }
    }
    //TODO addAI which passes in that AI is true.
    //IN here it could check the count to know how many times it needs to create an AI
    //count how many times the addPLayer is called.
    //This needs to be called upon the start of the game as all clients shld then be added.

    /**
     * Remove client.
     *
     * @param username the username
     */
    public void removeClient(String username) {
        playerMap.remove(username);
    }
    //TODO get this to call the correct commands for the correct tank
    //get the children for the game object which connects to all the tanks etc.
    //then look through that list and get the name? then should be able to call the gameobject?
    //when have correct game object then can do the below

    /**
     * Player input.
     *
     * @param player the player
     * @param cmd    the cmd
     */
    public void playerInput(String player, Command cmd) {
        //Gets the tank that the command came from
        var tank = playerMap.get(player);
        switch (cmd) {
            //TODO set name of tank game object to player id and pass that in to logic.
            case UP:
                tank.getComponent(TankController.class).moveUp();
                this.logger.trace("Player: " + player + " sent command: " + cmd);
                break;
            case DOWN:
                tank.getComponent(TankController.class).moveDown();
                this.logger.trace("Player: " + player + " sent command: " + cmd);
                break;
            case LEFT:
                tank.getComponent(TankController.class).moveLeft();
                this.logger.trace("Player: " + player + " sent command: " + cmd);
                break;
            case RIGHT:
                tank.getComponent(TankController.class).moveRight();
                this.logger.trace("Player: " + player + " sent command: " + cmd);
                break;
            case SHOOT:
                tank.getComponent(TankController.class).shoot();
                this.logger.trace("Player: " + player + " sent command: " + cmd);
                break;
            default:
                this.logger.warn("switched to default case.");
        }
    }

    private TankObject createTank(String player) {
        try {
            Position position;
            final Manager instance = Manager.INSTANCE;
            boolean taken = true;
            float xs = 0;
            float ys = 0;
            //TODO add a method to ensure tanks dont spawn in to close
            while (taken) {
                xs = ThreadLocalRandom.current().nextInt(instance.getMin(), instance.getMax() + 1);
                ys = ThreadLocalRandom.current().nextInt(instance.getMin(), instance.getMax() + 1);
                if (!(playerXS.contains(xs) && playerYS.contains(ys))) {
                    taken = false;
                    playerXS.add(xs);
                    playerYS.add(ys);
                    System.out.println("x" + xs);
                }
            }
            TankObject tank = new TankObject(getGameObject().getChildren().get(0),
            player, position = new Position(xs, ys), 100, 30);
            DataServer.INSTANCE.setCoordinates(position, "Tank");
            return tank;
            //TODO should these exceptions be caught?
            //TODO would it not make more sense to not add a null to the player map?
        } catch (InvalidClassInstance | ComponentExistsException e) {
            this.logger.error(e);
        }
        return null;
    }
}
