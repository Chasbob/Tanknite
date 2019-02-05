package com.aticatac.common.objectsystem;

/**
 * The type Game object.
 */
public abstract class AbstractObject {
    private String name;

    /**
     * Instantiates a new Game object.
     *
     * @param name the name
     */
    public AbstractObject(String name) {
        this.name = name;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }
}
