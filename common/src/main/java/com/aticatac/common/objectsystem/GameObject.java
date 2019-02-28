

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;

import java.util.ArrayList;
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
public class GameObject extends AbstractObject {
    private HashMap<Class<?>, Component> components;
    private Optional<GameObject> parent;
    public List<GameObject> children;

    public GameObject(String name) throws InvalidClassInstance, ComponentExistsException {
        this(name, ObjectType.OTHER);
    }

    /**
     * Instantiates a new Game object.
     *
     * @param name the name
     * @throws InvalidClassInstance     the invalid class instance
     * @throws ComponentExistsException the component exists exception
     */
    public GameObject(String name, ObjectType objectType) throws InvalidClassInstance, ComponentExistsException {
        this.name = name;
        this.children = new HashMap<>();
        this.components = new HashMap<>();
        this.addComponent(Transform.class);
        this.transform = getComponent(Transform.class);
        this.objectType = objectType;
        this.parent = Optional.empty();
    }

    /**
     * Instantiates a new Game object.
     *
     * @param name   the name
     * @param parent the parent
     * @throws InvalidClassInstance     the invalid class instance
     * @throws ComponentExistsException the component exists exception
     */
    public GameObject(String name, GameObject parent) throws InvalidClassInstance, ComponentExistsException {
        this(name, parent, ObjectType.OTHER);
    }

    /**
     * Instantiates a new GameObject.
     *
     * @param name       the name
     * @param parent     the parent
     * @param objectType the object type
     * @throws InvalidClassInstance     the invalid class instance
     * @throws ComponentExistsException the component exists exception
     */
    public GameObject(String name, GameObject parent, ObjectType objectType) throws InvalidClassInstance, ComponentExistsException {
        this.parent = Optional.of(parent);
        this.name = name;
        this.children = new HashMap<>();
        this.components = new HashMap<>();
        this.parent.get().addChild(this);
        this.addComponent(Transform.class);
        this.transform = getComponent(Transform.class);
        this.objectType = objectType;
    }

    /**
     * Destroy.
     *
     * @param g the g
     */
    public static void Destroy(GameObject g) {
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
    public HashMap<Class<?>, Component> getComponents() {
        return components;
    }

    /**
     * Add child.
     *
     * @param child the child
     */
    void addChild(GameObject child) {
        this.children.put(child.getName(), child);
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
     * @param childName the child name
     */
    public void removeChild(String childName) {
        children.remove(childName);
    }

    /**
     * Find object game object.
     *
     * @param tag        the tag
     * @param gameObject the game object
     * @return game object
     */
    public GameObject findObject(String tag, GameObject gameObject) {
        return gameObject.parent.isPresent() ? findObject(tag, gameObject.parent.get()) : findObjectHelper(tag, gameObject);
    }

    //TODO these may need to be static.
    private GameObject findObjectHelper(String t, GameObject g) {
        if (g.name.equals(t)) return g;
        for (var c : g.children.keySet()) {
            findObjectHelper(t, g.children.get(c));
        }
        return null;
    }

    //Getters and Setters
    public AbstractObject getParent() {
    public void findObject(String tag, GameObject gameObject){}

    //Getters and Setters
    public AbstractObject getParent() {
        return parent.get();
    }

    /**
     * Gets children.
     *
     * @return the children
     */
    public List<GameObject> getChildren() {
        ArrayList<GameObject> output = new ArrayList<>();
        for (String key :
                this.children.keySet()) {
            output.add(this.children.get(key));
        }
        return output;
    }

    /**
     * Gets transform.
     *
     * @return the transform
     */
    public Transform getTransform() {
        return transform;
    }

    /**
     * Sets transform.
     *
     * @param transform the transform
     */
    public void setTransform(Transform transform) {
        setTransform(transform.getX(), transform.getY());
    }

    public void setTransform(double x, double y) {
        this.transform.setPosition(x, y);
    }

    @Override
    public String toString() {
        return "GameObject{" +
                "transform=" + transform +
                ", name='" + name + '\'' +
                '}';
    }
}
