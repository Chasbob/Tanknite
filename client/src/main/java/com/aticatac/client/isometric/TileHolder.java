package com.aticatac.client.isometric;

import com.aticatac.client.server.Position;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TileHolder {
  public TileHolder(Position ps, int texture) {
    this.ps = ps;
    this.texture = texture;
  }

  public Position getPosition() {
    return ps;
  }

  public void setPosition(Position ps) {
    this.ps = ps;
  }

  public int getTexture() {
    return texture;
  }

  public void setTexture(int texture) {
    this.texture = texture;
  }

  private Position ps = new Position();
  private int texture;

  public static void textureToInt(TextureRegion textureRegion) {

  }
}
