package com.aticatac.client.objectsystem;

import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.objectsystem.GameObject;

/**
 * The type Object helper.
 */
public class ObjectHelper {
  /**
   * Add renderer.
   *
   * @param g        the g
   * @param location the location
   * @throws InvalidClassInstance     the invalid class instance
   * @throws ComponentExistsException the component exists exception
   */
  public static void AddRenderer(GameObject g, String location) throws InvalidClassInstance, ComponentExistsException {
    g.addComponent(Renderer.class).setTexture(location);
  }
}
