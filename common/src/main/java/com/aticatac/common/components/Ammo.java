package com.aticatac.common.components;

import com.aticatac.common.objectsystem.GameObject;

public class Ammo extends Component {
  private int ammo = 0;

  /**
   * construct for component: Ammo.
   *
   * @param parent
   */
  public Ammo(GameObject parent) {
    super(parent);
  }

  /**
   * Gets ammo.
   *
   * @return
   */
  public int getAmmo() {
    return ammo;
  }

  /**
   * Sets ammo.
   *
   * @param ammo
   */
  public void setAmmo(int ammo) {
    this.ammo = ammo;
  }
}
