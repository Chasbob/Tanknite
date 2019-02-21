package com.aticatac.common.objectsystem;

import com.aticatac.common.components.Texture;
import com.aticatac.common.model.TransformModel;

public class Container {
    TransformModel transformModel;
    Texture texture;

    @Override
    public String toString() {
        return "Container{" +
                "transformModel=" + transformModel +
                ", texture=" + texture +
                '}';
    }
}
