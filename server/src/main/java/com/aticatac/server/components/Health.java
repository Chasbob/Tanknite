package com.aticatac.server.components;

import com.aticatac.server.objectsystem.GameObject;

public class Health extends Component {
  private int health = 0;
  private int maxhealth = 100;

  public Health(GameObject parent) {
    super(parent);
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public int getMaxHealth() {
    return maxhealth;
  }

  private void setMaxHealth(int health) {
    this.health = health;
  }

  public int hit(final int damage) {
    health -= damage;
    return health;
  }
}
