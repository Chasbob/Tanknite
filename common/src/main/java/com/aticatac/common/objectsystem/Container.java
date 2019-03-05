package com.aticatac.common.objectsystem;

import com.aticatac.common.components.Texture;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The type Container.
 */
public class Container {
    private String texture;
    private double x;
    private double y;
    private double r;
    private final HashMap<String,Container> children;
    private String id;
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
        this.children = new HashMap<>();
        for (GameObject child : g.getChildren()) {
            this.children.put(id,new Container(child));
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
        return new ArrayList<>(children.values());
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

    public void update(GameObject g){
        this.id = g.getName();
        if (g.componentExists(Texture.class)) {
            this.texture = g.getComponent(Texture.class).getTexture();
        } else {
            this.texture = "";
        }
        this.x = g.getTransform().getX();
        this.y = g.getTransform().getY();
        this.r = g.getTransform().getRotation();

        var childrenLocal = new HashMap<>(children);
        for (GameObject child : g.getChildren()) {
            if(children.containsKey(child.getName())) {
                //Child exists
                childrenLocal.remove(child.getName());
                children.get(child.getName()).update(child);
            }else{
                //Child doesnt exist
                this.children.put(id,new Container(child));
            }
        }

        if(!childrenLocal.isEmpty()){
            for (var childName: childrenLocal.keySet()){
                children.remove(childName);
            }
        }
    }
}
