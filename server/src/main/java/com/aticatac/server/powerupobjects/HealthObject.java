package com.aticatac.server.powerupobjects;

import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.objectsystem.GameObject;

public class HealthObject extends GameObject {
  public HealthObject(String name, GameObject parent) throws InvalidClassInstance, ComponentExistsException {
    super(name, (parent));
  }
}
