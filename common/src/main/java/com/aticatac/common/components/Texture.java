package com.aticatac.common.components;

import com.aticatac.common.objectsystem.GameObject;

/**
 * The type Texture.
 */
public class Texture extends Component {
  private String texture;

  /**
   * Instantiates a new Component.
   *
   * @param gameObject the component parent
   */
  public Texture(GameObject gameObject) {
    super(gameObject);
    this.setTexture("");
  }

  /**
   * Gets texture.
   *
   * @return the texture
   */
  public String getTexture() {
    return texture;
  }

  /**
   * Sets texture.
   *
   * @param texture the texture
   */
  public void setTexture(String texture) {
    this.texture = texture;
  }

  @Override
  public String toString() {
    return "texture{" +
    "texture='" + texture + '\'' +
    '}';
  }
}
