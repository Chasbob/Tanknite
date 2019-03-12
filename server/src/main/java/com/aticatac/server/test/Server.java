package com.aticatac.server.test;

import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;

public class Server {
 private GameMode gameMode;

  public Server() throws InvalidClassInstance, ComponentExistsException {
    this.gameMode = new Survival();
  }

}
