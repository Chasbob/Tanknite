package com.aticatac.server.components;

import com.aticatac.common.objectsystem.GameObject;

import java.io.Serializable;

public abstract class Component<T> extends Thread implements Serializable {

    public GameObject componentParent;

    public Component(GameObject componentParent){
        this.componentParent = componentParent;
    }
}
