package com.aticatac.common;

/**
 * The enum Common data.
 */
public enum CommonData {
  /**
   * Instance common data.
   */
  INSTANCE;
  private int discoveryPort = 5000;

  CommonData() {
  }

  /**
   * Gets discovery port.
   *
   * @return the discovery port
   */
  public int getDiscoveryPort() {
    return discoveryPort;
  }
}
