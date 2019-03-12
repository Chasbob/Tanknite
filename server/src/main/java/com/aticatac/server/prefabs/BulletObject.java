package com.aticatac.server.prefabs;

import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.Physics;
import com.aticatac.server.components.Time;

public class BulletObject extends GameObject {
  /**
   * Instantiates a new GameObject.
   *
   * @param name   the name
   * @param parent the parent
   * @throws InvalidClassInstance     the invalid class instance
   * @throws ComponentExistsException the component exists exception
   */
  public BulletObject(String name, GameObject parent) throws InvalidClassInstance, ComponentExistsException {
    super(name, (parent));
    this.addComponent(Physics.class);
    this.addComponent(Time.class);
  }
}
