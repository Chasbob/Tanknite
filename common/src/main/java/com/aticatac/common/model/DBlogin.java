package com.aticatac.common.model;

public class DBlogin extends Model {
  private String username;
  private String password;
  private boolean register;

  public DBlogin(final String username, final String password) {
    this.username = username;
    this.password = password;
    this.register = false;
  }

  public DBlogin(final String username, final String password, final boolean register) {
    this(username, password);
    this.register = register;
  }

  public String getPassword() {
    return password;
  }

  @Override
  public String toString() {
    return "DBlogin{"
      +
      "username='"
      +
      username
      +
      '\''
      +
      ", password='"
      +
      password
      +
      '\''
      +
      '}';
  }

  public String getUsername() {
    return username;
  }
}
