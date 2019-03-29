package com.aticatac.client.isometric;

/**
 * The type Dynamic shadow.
 */
public class DynamicShadow {
  private IsoContainer parent;

  /**
   * Instantiates a new Dynamic shadow.
   *
   * @param parent the parent
   */
  public DynamicShadow(IsoContainer parent) {
    this.parent = parent;
  }

  /**
   * Gets parent.
   *
   * @return the parent
   */
  public IsoContainer getParent() {
    return parent;
  }
}
