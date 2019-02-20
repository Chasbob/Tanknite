package com.aticatac.server.gameManager;

import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.model.Command;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.GameManager;
import org.apache.log4j.Logger;

import java.util.HashMap;

public enum Manager {
    INSTANCE;
    private final int min = 0, max = 640;
    private final Logger logger;
    private final HashMap<String, GameObject> playerMap;
    private GameObject root;

    public GameObject getRoot() {
        return root;
    }

    Manager() {
        this.logger = Logger.getLogger(getClass());
        playerMap = new HashMap<>();
        try {
            this.root = new GameObject("Root");
            root.addComponent(GameManager.class);
        } catch (InvalidClassInstance invalidClassInstance) {
            throw new ExceptionInInitializerError(invalidClassInstance);
        } catch (ComponentExistsException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public void addClient(String username) {
        this.root.getComponent(GameManager.class).addPlayer(username);
        this.logger.info("added client: " + username);
        this.logger.info("root: " + this.root.getComponent(GameManager.class).getPlayerMap().size());
    }

    public void removeClient(String username) {
        root.getComponent(GameManager.class).removeClient(username);
    }

    public void playerInput(String player, Command cmd) {
        root.getComponent(GameManager.class).playerInput(player, cmd);
    }

    public void endOfGame() {
        //TODO
    }
}
