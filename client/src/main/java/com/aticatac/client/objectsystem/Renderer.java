package com.aticatac.client.objectsystem;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * The type Renderer.
 */
public class Renderer extends Component {
  private Texture texture;

  /**
   * Instantiates a new Component.
   *
   * @param gameObject the component parent
   */
  public Renderer(GameObject gameObject) {
    super(gameObject);
  }

  /**
   * Gets texture.
   *
   * @return the texture
   */
  public Texture getTexture() {
    return texture;
  }

  /**
   * Sets texture.
   *
   * @param texture the texture
   */
  public void setTexture(Texture texture) {
    this.texture = texture;
  }

  /**
   * Sets texture.
   *
   * @param texture the texture
   */
  public void setTexture(String texture) {
    this.texture = new Texture(Gdx.files.internal(texture));
  }
}
