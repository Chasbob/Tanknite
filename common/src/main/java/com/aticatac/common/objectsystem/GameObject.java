package com.aticatac.common.objectsystem;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * The type GameObject.
 */
public class GameObject extends AbstractObject {
    private HashMap<Class<?>, Component> components;
    private Optional<GameObject> parent;
    private List<GameObject> children;

    public Transform transform;

    /**
     * Instantiates a new GameObject.
     *
     * @param name   the name
     * @param parent the parent
     * @throws InvalidClassInstance     the invalid class instance
     * @throws ComponentExistsException the component exists exception
     */
    public GameObject(String name, GameObject parent) throws InvalidClassInstance, ComponentExistsException {
        super(name);
        this.parent = Optional.of(parent);
        this.children = new ArrayList<>();
        this.components = new HashMap<>();
        this.parent.get().addChild(this);
        this.addComponent(Transform.class);
        this.transform = getComponent(Transform.class);
    }

    /**
     * Instantiates a new Game object.
     *
     * @param name the name
     * @throws InvalidClassInstance     the invalid class instance
     * @throws ComponentExistsException the component exists exception
     */
    public GameObject(String name) throws InvalidClassInstance, ComponentExistsException {
        super(name);
        this.parent = Optional.empty();
        this.children = new ArrayList<>();
        this.components = new HashMap<>();
        this.addComponent(Transform.class);
        this.transform = getComponent(Transform.class);
    }

    /**
     * Instantiates a new Game object.
     *
     * @throws InvalidClassInstance     the invalid class instance
     * @throws ComponentExistsException the component exists exception
     */
    public GameObject() throws InvalidClassInstance, ComponentExistsException {
        super("root");
        this.parent = Optional.empty();
        this.children = new ArrayList<>();
        this.components = new HashMap<>();
        this.addComponent(Transform.class);
        this.transform = getComponent(Transform.class);
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
     * Add component t.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the t
     * @throws ComponentExistsException the component exists exception
     * @throws InvalidClassInstance     the invalid class instance
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
     * Gets component.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the component
     */
    public <T extends Component> T getComponent(Class<T> type) {
        return type.cast(components.get(type));
    }

    /**
     * Component exists boolean.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the boolean
     */
    public <T extends Component> boolean componentExists(Class<T> type) {
        return components.containsKey(type);
    }

    /**
     * Remove child.
     *
     * @param child the child
     */
    public void removeChild(GameObject child) {
        children.remove(child);
    }

    public Collection<Component> fetchAllComponents(){
        return components.values();
    }

    public static void Destroy(GameObject g){

    }

    //Getters and Setters
    public AbstractObject getParent() {
        return parent.get();
    }

    public void setParent(GameObject parent) {
        this.parent = Optional.of(parent);
    }

    public List<GameObject> getChildren() {
        return children;
    }

    public void setChildren(List<GameObject> children) {
        this.children = children;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }
}
