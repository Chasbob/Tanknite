package com.aticatac.common.components;

import com.aticatac.common.objectsystem.GameObject;

import java.io.Serializable;

public abstract class Component<T> implements Serializable {

    public GameObject componentParent;

    public Component(GameObject componentParent){
        this.componentParent = componentParent;
    }

}
