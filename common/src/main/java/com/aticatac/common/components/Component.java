package com.aticatac.common.components;

import com.aticatac.common.objectsystem.GameObject;

import java.io.Serializable;

/**
 * The type Component.
 */
public abstract class Component extends Thread implements Serializable {
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
    }

    /**
     * Gets game object.
     *
     * @return the game object
     */
    public GameObject getGameObject() {
        return gameObject;
    }
}
