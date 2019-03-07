package com.aticatac.client.objectsystem;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * The type Ortho camera.
 */
public class OrthoCamera extends OrthographicCamera {
  private Component c;

  /**
   * Instantiates a new Ortho camera.
   *
   * @param viewportWidth  the viewport width
   * @param viewportHeight the viewport height
   * @param component      the component
   */
  public OrthoCamera(float viewportWidth, float viewportHeight, Component component) {
    this.viewportWidth = viewportWidth;
    this.viewportHeight = viewportHeight;
    this.near = 0;
    c = component;
    update();
  }

  @Override
  public void update() {
    Position p = c.getGameObject().getComponent(Transform.class).getPosition();
    //super.position.set((float)p.x,(float)p.y,0);
    super.update();
  }
}
