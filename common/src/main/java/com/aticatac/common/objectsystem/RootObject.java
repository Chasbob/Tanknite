package com.aticatac.common.objectsystem;

/**
 * The type Root object.
 */
public class RootObject extends AbstractObject {
    private AbstractObject child;

    /**
     * Instantiates a new Root object.
     *
     * @param name the name
     */
    public RootObject(String name) {
        super(name);
    }

    /**
     * Gets child.
     *
     * @return the child
     */
    public AbstractObject getChild() {
        return child;
    }

    /**
     * Sets child.
     *
     * @param child the child
     */
    public void setChild(AbstractObject child) {
        this.child = child;
    }
}
