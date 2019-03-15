package com.aticatac.server.objectsystem;

import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.objectsystem.Container;
import com.aticatac.common.objectsystem.ObjectType;
import com.aticatac.server.components.Ammo;
import com.aticatac.server.components.Component;
import com.aticatac.server.components.Health;
import com.aticatac.server.components.Texture;
import com.aticatac.server.components.physics.Entity;
import com.aticatac.server.objectsystem.transform.Transform;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type GameObject.
 */
public class GameObject {
  //  private final Transform transform;
  private final ObjectType objectType;
  private final String name;
  private final ConcurrentHashMap<Class<?>, Component> components;
  private final ConcurrentHashMap<String, GameObject> children;
  private final Optional<GameObject> parent;
  private Entity entity;

  /**
   * Instantiates a new Game object.
   *
   * @param name the name
   *
   * @throws InvalidClassInstance     the invalid class instance
   * @throws ComponentExistsException the component exists exception
   */
  public GameObject(String name) throws InvalidClassInstance, ComponentExistsException {
    this(name, ObjectType.OTHER);
  }

  /**
   * Instantiates a new Game object.
   *
   * @param name       the name
   * @param objectType the object type
   *
   * @throws InvalidClassInstance     the invalid class instance
   * @throws ComponentExistsException the component exists exception
   */
  public GameObject(String name, ObjectType objectType)
      throws InvalidClassInstance, ComponentExistsException {
    this.name = name;
    this.children = new ConcurrentHashMap<>();
    this.components = new ConcurrentHashMap<>();
    this.addComponent(Transform.class);
    this.objectType = objectType;
    this.parent = Optional.empty();
  }

  /**
   * Instantiates a new Game object.
   *
   * @param name   the name
   * @param parent the parent
   *
   * @throws InvalidClassInstance     the invalid class instance
   * @throws ComponentExistsException the component exists exception
   */
  public GameObject(String name, GameObject parent)
      throws InvalidClassInstance, ComponentExistsException {
    this(name, parent, ObjectType.OTHER);
  }

  /**
   * Instantiates a new GameObject.
   *
   * @param name       the name
   * @param parent     the parent
   * @param objectType the object type
   *
   * @throws InvalidClassInstance     the invalid class instance
   * @throws ComponentExistsException the component exists exception
   */
  public GameObject(String name, GameObject parent, ObjectType objectType)
      throws InvalidClassInstance, ComponentExistsException {
    this.parent = Optional.of(parent);
    this.name = name;
    this.children = new ConcurrentHashMap<>();
    this.components = new ConcurrentHashMap<>();
    this.parent.get().addChild(this);
    this.addComponent(Transform.class);
    this.objectType = objectType;
  }

  /**
   * destroy.
   *
   * @param g the g
   */
  public static void destroy(GameObject g) {
  }

  public Entity getEntity() {
    if (entity != null) {
      return entity;
    } else {
      entity = new Entity(name, Entity.EntityType.NONE);
      return entity;
    }
  }

  public Container getContainer() {
    //todo this will shit out nulls
    return new Container(getTexture(), getTransform().getX(), getTransform().getY(), getTransform().getRotation(), getComponent(Health.class).getHealth(), getComponent(Ammo.class).getAmmo(), getName(), getObjectType());
  }

  /**
   * Has parent boolean.
   *
   * @return the boolean
   */
  public boolean hasParent() {
    return this.parent.isPresent();
  }

  /**
   * Gets object type.
   *
   * @return the object type
   */
  public ObjectType getObjectType() {
    return objectType;
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
   * Gets components.
   *
   * @return the components
   */
  public ConcurrentHashMap<Class<?>, Component> getComponents() {
    return components;
  }

  /**
   * Add child.
   *
   * @param child the child
   */
  private void addChild(GameObject child) {
    this.children.put(child.getName(), child);
  }

  /**
   * Add component t.
   *
   * @param <T>  the type parameter
   * @param type the type
   *
   * @return the t
   *
   * @throws ComponentExistsException the component exists exception
   * @throws InvalidClassInstance     the invalid class instance
   */
  public <T extends Component> T addComponent(Class<T> type)
      throws ComponentExistsException, InvalidClassInstance {
    if (componentExists(type)) {
      throw new ComponentExistsException(type.getName() + "exists");
    }
    try {
      T t = type.getConstructor(GameObject.class).newInstance(this);
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
   *
   * @return the component
   */
  public <T extends Component> T getComponent(Class<T> type) {
    if (componentExists(type)) {
      return type.cast(components.get(type));
    } else {
      try {
        return addComponent(type);
      } catch (ComponentExistsException | InvalidClassInstance e) {
        e.printStackTrace();
      }
      return null;
    }
  }

  /**
   * Component exists boolean.
   *
   * @param <T>  the type parameter
   * @param type the type
   *
   * @return the boolean
   */
  public <T extends Component> boolean componentExists(Class<T> type) {
    return components.containsKey(type);
  }

  /**
   * Has texture boolean.
   *
   * @return the boolean
   */
  public boolean hasTexture() {
    return this.componentExists(Texture.class);
  }

  /**
   * Gets texture.
   *
   * @return the texture
   */
  public String getTexture() {
    return this.getComponent(Texture.class).getTexture();
  }

  /**
   * Sets rotation.
   *
   * @param rotation the rotation
   */
  public void setRotation(int rotation) {
    this.getComponent(Transform.class).setRotation(rotation);
  }

  /**
   * Remove child.
   *
   * @param childName the child name
   */
  public void removeChild(String childName) {
    children.remove(childName);
  }

  /**
   * Find object game object.
   *
   * @param tag the tag
   *
   * @return the game object
   */
  public GameObject findObject(String tag) {
    return this.parent.isPresent() ? findObject(tag) : findObjectHelper(tag, this);
  }

  /**
   * Gets textured.
   *
   * @return the textured
   */
  public ArrayList<GameObject> getTextured() {
    ArrayList<GameObject> out = new ArrayList<>();
    for (GameObject c :
        this.children.values()) {
      if (c.componentExists(Texture.class)) {
        out.add(c);
      }
      out.addAll(c.getTextured());
    }
    return out;
  }

  public GameObject findObject(ObjectType type) {
    GameObject out;
    if (this.parent.isPresent()) {
      out = findObject(type);
    } else {
      out = findObjectHelper(type, this);
    }
    return out;
  }

  private GameObject findObjectHelper(ObjectType t, GameObject g) {
    GameObject out;
    if (g.getObjectType() == t) {
      return g;
    } else {
      for (var c : g.children.keySet()) {
        out = findObjectHelper(t, g.children.get(c));
        if (out != null) {
          return out;
        }
      }
      return null;
    }
  }

  //TODO these may need to be static.
  private GameObject findObjectHelper(String t, GameObject g) {
    if (g.name.equals(t)) {
      return g;
    }
    for (var c : g.children.keySet()) {
      findObjectHelper(t, g.children.get(c));
    }
    return null;
  }

  /**
   * Gets parent.
   *
   * @return the parent
   */
  public GameObject getParent() {
    return parent.get();
  }

  /**
   * Gets children.
   *
   * @return the children
   */
  public ConcurrentHashMap<String, GameObject> getChildren() {
    return children;
  }

  /**
   * Gets transform.
   *
   * @return the transform
   */
  public Transform getTransform() {
    if (this.componentExists(Transform.class)) {
      return this.getComponent(Transform.class);
    } else {
      System.out.println("No Transform!");
      return this.getComponent(Transform.class);
    }
  }

  /**
   * Sets transform.
   *
   * @param transform the transform
   */
  public void setTransform(Transform transform) {
    setTransform(transform.getX(), transform.getY());
  }

  /**
   * Sets transform.
   *
   * @param x the x
   * @param y the y
   */
  public void setTransform(int x, int y) {
    this.getComponent(Transform.class).setPosition(x, y);
  }

  @Override
  public String toString() {
    return "GameObject{"
        +
        "transform="
        +
        ", name='"
        +
        name
        +
        '\''
        +
        '}'
        ;
  }
}
