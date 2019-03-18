package com.aticatac.server.components;

import com.aticatac.server.objectsystem.GameObject;

public class Ammo extends Component {
  private int ammo = 0;
  private int maxAmmo = 30;

  public Ammo(GameObject parent) {
    super(parent);
  }

  public int getAmmo() {
    return ammo;
  }

  public void setAmmo(int ammo) {
    this.ammo = ammo;
  }

  public boolean hasAmmo() {
    return ammo > 0;
  }

  public void use() {
    ammo -= 1;
  }
}
