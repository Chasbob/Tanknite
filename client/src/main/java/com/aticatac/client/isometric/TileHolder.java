package com.aticatac.client.isometric;

import com.aticatac.client.server.Position;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * The type Tile holder.
 */
public class TileHolder {
  /**
   * Instantiates a new Tile holder.
   *
   * @param ps      the ps
   * @param texture the texture
   */
  public TileHolder(Position ps, int texture) {
    this.ps = ps;
    this.texture = texture;
  }

  /**
   * Gets position.
   *
   * @return the position
   */
  public Position getPosition() {
    return ps;
  }

  /**
   * Sets position.
   *
   * @param ps the ps
   */
  public void setPosition(Position ps) {
    this.ps = ps;
  }

  /**
   * Gets texture.
   *
   * @return the texture
   */
  public int getTexture() {
    return texture;
  }

  /**
   * Sets texture.
   *
   * @param texture the texture
   */
  public void setTexture(int texture) {
    this.texture = texture;
  }

  private Position ps = new Position();
  private int texture;

  /**
   * Texture to int.
   *
   * @param textureRegion the texture region
   */
  public static void textureToInt(TextureRegion textureRegion) {

  }
}
