package com.aticatac.server.PowerUpObjects;

import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.objectsystem.GameObject;

public class AmmoObject extends GameObject {
  public AmmoObject(String name, GameObject parent) throws InvalidClassInstance, ComponentExistsException {
    super(name, (parent));
  }
}
