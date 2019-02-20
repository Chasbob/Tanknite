package com.aticatac.server.gameManager;

import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.model.Command;
import com.aticatac.common.objectsystem.GameObject;
import org.apache.log4j.Logger;

import java.util.HashMap;

public class GameManager implements Runnable {
    private static final int min = 0, max = 640;
    private final Logger logger;
    public HashMap<String, GameObject> playerMap = new HashMap<>();
    private GameObject root;

    public GameManager() {
        this.logger = Logger.getLogger(getClass());
        try {
            root = new GameObject("Root");
            root.addComponent(com.aticatac.server.components.GameManager.class);
        } catch (ComponentExistsException | InvalidClassInstance e) {
            this.logger.error("Ewan promises this will NEVER HAPPEN.\n" + e);
        }
    }

    public void addClient(String username) {
        root.getComponent(com.aticatac.server.components.GameManager.class).addPlayer(username);
    }

    public void removeClient(String username) {
        root.getComponent(com.aticatac.server.components.GameManager.class).removeClient(username);
    }

    public void playerInput(String player, Command cmd) {
        root.getComponent(com.aticatac.server.components.GameManager.class).playerInput(player, cmd);
    }

    public void endOfGame() {
        //TODO
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
    }

    public void stop() {
    }
}
