package com.aticatac.common.objectsystem;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The type GameObject.
 */
public class GameObject extends AbstractObject {
    private HashMap<Class<?>, Component> components;
    public AbstractObject parent;
    public List<GameObject> children;

    /**
     * Instantiates a new GameObject.
     *
     * @param name   the name
     * @param parent the parent
     */
    public GameObject(String name, RootObject parent) throws ComponentExistsException, InvalidClassInstance {
        super(name);
        this.parent = parent;
        this.children = new ArrayList<>();
        this.components = new HashMap<>();
        ((RootObject) this.parent).setChild(this);
        addComponent(Transform.class);
    }

    public GameObject(String name, GameObject parent) throws ComponentExistsException, InvalidClassInstance {
        super(name);
        this.parent = parent;
        this.children = new ArrayList<>();
        this.components = new HashMap<>();
        ((GameObject) this.parent).addChild(this);
        addComponent(Transform.class);
    }

    /**
     * Remove child.
     *
     * @param child the child
     */
    public void removeChild(GameObject child) {
        children.remove(child);
    }

    /**
     * Add child.
     *
     * @param child the child
     */
    void addChild(GameObject child) {
        this.children.add(child);
    }

    /**
     * Add components.
     *
     * @param components the components
     */
    public void addComponents(List<Class<? extends Component>> components) throws ComponentExistsException, InvalidClassInstance {
        for (Class<? extends Component> c : components) {
            if (children.contains(c)) {
                throw new ComponentExistsException();
            }
            addComponent(c);
        }
    }

    /**
     * Add component t.
     *
     * @param <T>  the type parameter
     * @param type the type
     *
     * @return the t
     */
    public <T extends Component> T addComponent(Class<T> type) throws ComponentExistsException, InvalidClassInstance {
        if (componentExists(type)) {
            throw new ComponentExistsException(type.getName() + "exists");
        }
        try {
            T t = type.getConstructor(GameObject.class).newInstance(this);
            t.start();
            components.put(type, t);
            return t;
        } catch (Exception e) {
            throw new InvalidClassInstance();
        }
    }

    /**
     * Remove component.
     *
     * @param <T>  the type parameter
     * @param type the type
     */
    public <T extends Component> void removeComponent(Class<T> type) {
        components.get(type).interrupt();
        components.remove(type);
    }

    /**
     * Gets component.
     *
     * @param <T>  the type parameter
     * @param type the type
     *
     * @return the component
     */
    public <T extends Component> T getComponent(Class<T> type) {
        return type.cast(components.get(type));
    }

    private <T extends Component> boolean componentExists(Class<T> type) {
        return components.containsKey(type);
    }
}
