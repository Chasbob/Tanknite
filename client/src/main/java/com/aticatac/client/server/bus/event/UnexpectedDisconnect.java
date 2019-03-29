package com.aticatac.client.server.bus.event;

/**
 * The type Unexpected disconnect.
 */
public class UnexpectedDisconnect {
  private final String username;

  /**
   * Gets username.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Instantiates a new Unexpected disconnect.
   *
   * @param username the username
   */
  public UnexpectedDisconnect(final String username) {
    this.username = username;
  }
}
