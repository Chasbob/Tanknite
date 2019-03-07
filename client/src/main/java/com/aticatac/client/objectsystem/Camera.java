package com.aticatac.client.objectsystem;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;

/**
 * The type Camera.
 */
public class Camera extends Component {
  /**
   * The Cam.
   */
  public OrthoCamera cam;

  /**
   * Instantiates a new Component.
   *
   * @param gameObject the component parent
   */
  public Camera(GameObject gameObject) {
    super(gameObject);
    cam = new OrthoCamera(640, 640, this);
  }
}
