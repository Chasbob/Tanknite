package com.aticatac.client.server.bus.event;

public class UnexpectedDisconnect {
  private final String username;

  public String getUsername() {
    return username;
  }

  public UnexpectedDisconnect(final String username) {
    this.username = username;
  }
}
