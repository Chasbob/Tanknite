package com.aticatac.client.objectsystem;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;

public class Camera extends Component {
    public OrthoCamera cam;

    /**
     * Instantiates a new Component.
     *
     * @param gameObject the component parent
     */
    public Camera(GameObject gameObject) {
        super(gameObject);
        cam = new OrthoCamera(640, 640,this);
    }
}
