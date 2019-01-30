package com.aticatac.common.objectsystem;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Transform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GameObject {
    public String Name;
    public GameObject parent;
    public List<GameObject> children;

    public HashMap<Class<?>,Component> Components;

    public GameObject(GameObject parent){
        Constructor(parent);
    }

    public GameObject(GameObject parent, ArrayList<Class<? extends Component>> components){
        Constructor(parent);

        addComponents(components);
    }

    public void addComponents(List<Class<? extends Component>> components) {
        for (Class<? extends Component> c:components) {
            addComponent(c);
        }
    }

    private void Constructor(GameObject parent) {
        this.parent = parent;
        if (parent!=null) this.parent.children.add(this);

        Components = new HashMap<>();
        addComponent(Transform.class);
    }

    public static void Destroy(GameObject object){
        for (GameObject child:object.children) {
            Destroy(child);
        }
        if (object.parent!=null) object.parent.children.remove(object);
        if (object.parent!=null) object.parent = null;
    }

    public <T extends Component> T addComponent(Class<T> type) {
        if (ComponentExists(type)) return null;
        try {
            T t = type.getConstructor(GameObject.class).newInstance(this);
            Components.put(type,t);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T extends Component> void removeComponent(Class<T> type) {
        Components.remove(type);
    }

    public <T extends Component> T getComponent(Class<T> type) {
        return (type.cast(Components.get(type)));
    }

    private <T extends Component> boolean ComponentExists(Class<T> type){
        return Components.containsKey(type);
    }
}
