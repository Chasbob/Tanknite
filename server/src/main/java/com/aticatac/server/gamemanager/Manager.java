package com.aticatac.server.gamemanager;

import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.model.Command;
import com.aticatac.common.model.CommandModel;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.GameManager;
import org.apache.log4j.Logger;

/**
 * The enum Manager.
 */
public enum Manager {
  /**
   * Instance manager.
   */
  INSTANCE;
  private final int min = 320;
  private final int max = 1920 - 320;
  private final Logger logger;
  private GameObject root;

  Manager() {
    this.logger = Logger.getLogger(getClass());
    try {
      this.root = new GameObject("Root");
      this.root.addComponent(GameManager.class);
    } catch (InvalidClassInstance | ComponentExistsException invalidClassInstance) {
      throw new ExceptionInInitializerError(invalidClassInstance);
    }
  }

  /**
   * Gets root.
   *
   * @return the root
   */
  public GameObject getRoot() {
    return this.root;
  }

  /**
   * Gets min.
   *
   * @return the min
   */
  public int getMin() {
    return min;
  }

  /**
   * Gets max.
   *
   * @return the max
   */
  public int getMax() {
    return max;
  }

  /**
   * Add client.
   *
   * @param username the username
   */
  public void addClient(String username) {
    this.root.getComponent(GameManager.class).addPlayer(username);
    this.logger.info("added client: " + username);
    this.logger.trace("root: " + this.root.getComponent(GameManager.class).getPlayerMap().size());
  }

  /**
   * Remove client.
   *
   * @param username the username
   */
  public void removeClient(String username) {
    root.getComponent(GameManager.class).removeClient(username);
  }

  /**
   * Player input.
   *
   * @param player the player
   * @param cmd    the cmd
   */
  public void playerInput(String player, Command cmd) {
    root.getComponent(GameManager.class).playerInput(player, cmd);
  }

  /**
   * Player input.
   *
   * @param commandModel the command model
   */
  public void playerInput(CommandModel commandModel) {
    this.playerInput(commandModel.getId(), commandModel.getCommand());
  }

  /**
   * End of game.
   */
  public void endOfGame() {
    //TODO
  }
}
