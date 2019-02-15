package com.aticatac.client.objectsystem;

import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.objectsystem.GameObject;

public class ObjectHelper {
    public static void AddRenderer(GameObject g,String location) throws InvalidClassInstance, ComponentExistsException {
        g.addComponent(Renderer.class).setTexture(location);
    }
}
