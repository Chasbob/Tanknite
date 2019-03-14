package com.aticatac.server.test;

import com.aticatac.common.components.DataServer;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.model.CommandModel;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.common.objectsystem.ObjectType;
import com.aticatac.common.prefabs.TankObject;
import com.aticatac.server.components.controller.TankController;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.log4j.Logger;

/**
 * The type Game mode.
 */
@SuppressWarnings("ALL")
abstract class GameMode implements Game {
  /**
   * The Player map.
   */
  protected final HashMap<String, GameObject> playerMap;
  private final Logger logger;
  private final int min = 320;
  private final int max = 1920 - 320;
  private GameObject root;

  /**
   * Instantiates a new Game mode.
   *
   * @throws InvalidClassInstance     the invalid class instance
   * @throws ComponentExistsException the component exists exception
   */
  GameMode() throws InvalidClassInstance, ComponentExistsException {
    this.root = new GameObject("root", ObjectType.ROOT);
    new GameObject("Player Container", root, ObjectType.PLAYER_CONTAINER);
    this.playerMap = new HashMap<>();
    this.logger = Logger.getLogger(getClass());
  }

  /**
   * Player count int.
   *
   * @return the int
   */
  public int playerCount() {
    return this.playerMap.size();
  }

  @Override
  public void addPlayer(String player) {
    if (!playerMap.containsKey(player)) {
      playerMap.put(player, createTank(player, false));
      Position p = playerMap.get(player).getTransform().getPosition();
    }
  }

  @Override
  public void removePlayer(String player) {
    playerMap.remove(player);
  }

  @Override
  public void startGame() {
  }

  @Override
  public void playerInput(CommandModel model) {
    //Gets the tank that the command came from
    var tank = playerMap.get(model.getId());
    switch (model.getCommand()) {
      //TODO set name of tank game object to player id and pass that in to logic.
      case UP:
        tank.getComponent(TankController.class).moveUp();
        logger.trace("Player: " + model.getId() + " sent command: " + model.getCommand());
        break;
      case DOWN:
        tank.getComponent(TankController.class).moveDown();
        logger.trace("Player: " + model.getId() + " sent command: " + model.getCommand());
        break;
      case LEFT:
        tank.getComponent(TankController.class).moveLeft();
        logger.trace("Player: " + model.getId() + " sent command: " + model.getCommand());
        break;
      case RIGHT:
        tank.getComponent(TankController.class).moveRight();
        logger.trace("Player: " + model.getId() + " sent command: " + model.getCommand());
        break;
      case SHOOT:
        tank.getComponent(TankController.class).shoot();
        logger.trace("Player: " + model.getId() + " sent command: " + model.getCommand());
    }
  }

  private TankObject createTank(String player, boolean isAI) {
    Position position = new Position(ThreadLocalRandom.current().nextInt(min, max + 1),
        ThreadLocalRandom.current().nextInt(min, max + 1));
    //checks if this is a valid coordinate when generated is not in the map then moves on.
    while (DataServer.INSTANCE.getOccupiedCoordinates().containsKey(position)) {
      position = new Position(ThreadLocalRandom.current().nextInt(min, max + 1),
          ThreadLocalRandom.current().nextInt(min, max + 1));
    }
    return createTank(player, isAI, position.getX(), position.getY());
  }

  private TankObject createTank(String player, boolean isAI, int x, int y) {
    try {
      Position position = new Position(x, y);
      TankObject tank = new TankObject(root.getChildren().get("Player Container"),
          player,
          position,
          100,
          30,
          isAI);
      DataServer.INSTANCE.setCoordinates(position, player);
      return tank;
    } catch (InvalidClassInstance | ComponentExistsException e) {
      this.logger.error(e);
    }
    return null;
  }

  /**
   * Gets root.
   *
   * @return the root
   */
  public GameObject getRoot() {
    return root;
  }

  public void nAddAI(int n) {
    for (int i = 0; i < n; i++) {
      addAI("AI " + Integer.toString(i + 1));
    }
  }

  private void addAI(String id) {
    if (!playerMap.containsKey(id)) {
      playerMap.put(id, createTank(id, true));
      Position p = playerMap.get(id).getTransform().getPosition();
    }
  }
}
