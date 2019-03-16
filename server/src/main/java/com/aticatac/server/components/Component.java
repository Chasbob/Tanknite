package com.aticatac.server.components;

import com.aticatac.server.objectsystem.GameObject;
import org.apache.log4j.Logger;

/**
 * The type Component.
 */
public abstract class Component {
  public final Logger logger;
  /**
   * The Component parent.
   */
  protected GameObject gameObject;

  /**
   * Instantiates a new Component.
   *
   * @param gameObject the component parent
   */
  public Component(GameObject gameObject) {
    this.gameObject = gameObject;
    this.logger = Logger.getLogger(getClass());
  }

  public Component() {
    this.logger = Logger.getLogger(getClass());
  }

  /**
   * Gets game object.
   *
   * @return the game object
   */
  public GameObject getGameObject() {
    return gameObject;
  }

  public void setGameObject(GameObject gameObject) {
    this.gameObject = gameObject;
  }
//    public abstract void update(Component c);
}
