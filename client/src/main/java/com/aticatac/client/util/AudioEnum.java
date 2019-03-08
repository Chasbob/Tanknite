package com.aticatac.client.util;

import org.apache.log4j.Logger;

/**
 * The enum Audio enum.
 */
public enum AudioEnum {
  /**
   * Instance audio enum.
   */
  INSTANCE;
  private final Logger logger;

  AudioEnum() {
    this.logger = Logger.getLogger(getClass());
    this.logger.trace("boo");
  }
}
