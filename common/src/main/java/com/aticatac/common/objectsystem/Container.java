package com.aticatac.common.objectsystem;

import com.aticatac.common.components.Texture;
import java.util.ArrayList;

/**
 * The type Container.
 */
public class Container {
    private final String texture;
    private final double x;
    private final double y;
    private final double r;
    private final ArrayList<Container> children;
    private final String id;
    private final ObjectType objectType;

    /**
     * Instantiates a new Container.
     *
     * @param g the g
     */
    public Container(GameObject g) {
        this.id = g.getName();
        objectType = g.getObjectType();
        if (g.componentExists(Texture.class)) {
            this.texture = g.getComponent(Texture.class).getTexture();
        } else {
            this.texture = "";
        }
        this.x = g.getTransform().getX();
        this.y = g.getTransform().getY();
        this.r = g.getTransform().getRotation();
        this.children = new ArrayList<>();
        for (GameObject child : g.getChildren().values()) {
            this.children.add(new Container(child));
        }
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * Gets r.
     *
     * @return the r
     */
    public double getR() {
        return r;
    }

    /**
     * Gets texture.
     *
     * @return the texture
     */
    public String getTexture() {
        return texture;
    }

    @Override
    public String toString() {
        return "Container{" +
            "texture='" + texture + '\'' +
            ", x=" + x +
            ", y=" + y +
            ", r=" + r +
            ", children=" + children +
            ", id='" + id + '\'' +
            '}';
    }

    /**
     * Gets children.
     *
     * @return the children
     */
    public ArrayList<Container> getChildren() {
        return children;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets object type.
     *
     * @return the object type
     */
    public ObjectType getObjectType() {
        return objectType;
    }
}
