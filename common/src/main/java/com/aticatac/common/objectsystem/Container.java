package com.aticatac.common.objectsystem;

import com.aticatac.common.components.Texture;

public class Container {
  private final String texture;
  private final float x;
  private final float y;
  private final float r;
  private final String id;
  private final ObjectType objectType;

  public Container(GameObject g) {
    this.id = g.getName();
    objectType = g.getObjectType();
    if (g.componentExists(Texture.class)) {
      this.texture = g.getComponent(Texture.class).getTexture();
    } else {
      this.texture = "";
    }
    this.x = (float) g.getTransform().getX();
    this.y = (float) g.getTransform().getY();
    this.r = (float) g.getTransform().getRotation();
  }

  public String getTexture() {
    return texture;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public float getR() {
    return r;
  }

  public String getId() {
    return id;
  }

  public ObjectType getObjectType() {
    return objectType;
  }
}
