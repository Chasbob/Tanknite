package com.aticatac.common.components;

import com.aticatac.common.objectsystem.GameObject;

public class Health extends Component {
  private int health = 0;

  /**
   * construct for component: Health.
   *
   * @param parent
   */
  public Health(GameObject parent) {
    super(parent);
  }

  /**
   * Gets health.
   *
   * @return
   */
  public int getHealth() {
    return health;
  }

  /**
   * Sets health.
   *
   * @param health
   */
  public void setHealth(int health) {
    this.health = health;
  }
}