package com.aticatac.client.isometric;

public class DynamicShadow {
  private IsoContainer parent;

  public DynamicShadow(IsoContainer parent) {
    this.parent = parent;
  }

  public IsoContainer getParent() {
    return parent;
  }
}
