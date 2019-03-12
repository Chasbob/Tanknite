package com.aticatac.common.objectsystem;

import com.aticatac.common.components.Ammo;
import com.aticatac.common.components.Health;
import com.aticatac.common.components.Texture;

public class Container {
  private final String texture;
  private final float x;
  private final float y;
  private final float r;
  private final int health;
  private final int ammo;
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
//    this.health=0;
//    this.ammo=0;
    if (g.componentExists(Health.class)) {
      this.health = g.getComponent(Health.class).getHealth();
    } else {
      this.health = 100;
    }
    if (g.componentExists(Ammo.class)) {
      this.ammo = g.getComponent(Ammo.class).getAmmo();
    } else {
      this.ammo = 30;
    }
  }

  public Container() {
    this.texture = "";
    this.x = 1000;
    this.y = 1000;
    this.r = 0;
    this.health = 100;
    this.ammo = 30;
    this.id = "";
    this.objectType = ObjectType.OTHER;
  }

  public int getHealth() {
    return health;
  }

  public int getAmmo() {
    return ammo;
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
