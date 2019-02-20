package com.aticatac.common.components;

import com.aticatac.common.objectsystem.GameObject;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * The type Component.
 */
public abstract class Component extends Thread implements Serializable {
    private final Logger logger;
    /**
     * The Component parent.
     */
    private GameObject gameObject;

    /**
     * Instantiates a new Component.
     *
     * @param gameObject the component parent
     */
    public Component(GameObject gameObject) {
        this.gameObject = gameObject;
        this.logger = Logger.getLogger(getName());
    }

    public Logger getLogger() {
        return logger;
    }

    /**
     * Gets game object.
     *
     * @return the game object
     */
    public GameObject getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }
}
