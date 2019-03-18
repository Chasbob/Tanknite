package com.aticatac.server.components;

import com.aticatac.server.objectsystem.GameObject;

public class Damage extends Component {
  private int damage = 10;

  public Damage(GameObject parent) {
    super(parent);
  }

  public int getDamage() {
    return damage;
  }

  public void setDamage(int damage) {
    this.damage = damage;
  }
}