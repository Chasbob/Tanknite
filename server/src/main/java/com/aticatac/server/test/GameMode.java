package com.aticatac.server.test;

import com.aticatac.common.components.transform.Position;
import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.model.Command;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.common.objectsystem.ObjectType;
import com.aticatac.server.components.CollisionBox;
import com.aticatac.server.components.DataServer;
import com.aticatac.server.components.controller.TankController;
import com.aticatac.server.gamemanager.Manager;
import com.aticatac.server.prefabs.TankObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.log4j.Logger;

@SuppressWarnings("ALL")
abstract class GameMode implements Game {
  protected final HashMap<String, GameObject> playerMap;
  private final Logger logger;
  private final int min = 320;
  private final int max = 1920 - 320;
  private GameObject root;

  GameMode() throws InvalidClassInstance, ComponentExistsException {
    this.root = new GameObject("root", ObjectType.ROOT);
    new GameObject("Player Container", root, ObjectType.PLAYER_CONTAINER);
    this.playerMap = new HashMap<>();
    this.logger = Logger.getLogger(getClass());
  }

  @Override
  public void removePlayer(String player) {
    playerMap.remove(player);
  }

  @Override
  public void startGame() {
  }

  @Override
  public void playerInput(String player, Command cmd) {
    //Gets the tank that the command came from
    var tank = playerMap.get(player);
    switch (cmd) {
      //TODO set name of tank game object to player id and pass that in to logic.
      case UP:
        tank.getComponent(TankController.class).moveUp();
        logger.trace("Player: " + player + " sent command: " + cmd);
        break;
      case DOWN:
        tank.getComponent(TankController.class).moveDown();
        logger.trace("Player: " + player + " sent command: " + cmd);
        break;
      case LEFT:
        tank.getComponent(TankController.class).moveLeft();
        logger.trace("Player: " + player + " sent command: " + cmd);
        break;
      case RIGHT:
        tank.getComponent(TankController.class).moveRight();
        logger.trace("Player: " + player + " sent command: " + cmd);
        break;
      case SHOOT:
        tank.getComponent(TankController.class).shoot();
        logger.trace("Player: " + player + " sent command: " + cmd);
    }
  }

  @Override
  public void addPlayer(String player) {
    if (!playerMap.containsKey(player)) {
      playerMap.put(player, createTank(player, false));
      Position p = playerMap.get(player).getTransform().getPosition();
      for (int i = 0; i < 2; i++) {
        playerMap.put(player + "AI" + i, createTank(player + "AI" + i, true));
      }
    }
  }

  private TankObject createTank(String player, boolean isAI) {

    boolean positionClean = false;
    Position position = new Position();
    System.out.println("Before While");

    //while loop generating positions until finds one that isn't occupied
    while (!positionClean) {

      System.out.println("Position not clean");

      //creates random position
      position = new Position(ThreadLocalRandom.current().nextInt(Manager.INSTANCE.getMin(), Manager.INSTANCE.getMax() + 1),
          ThreadLocalRandom.current().nextInt(Manager.INSTANCE.getMin(), Manager.INSTANCE.getMax() + 1));

      //creates a collision box for tank
      this.getGameObject().getComponent(CollisionBox.class).setCollisionBox(position);

      //gets the collision box and occupied map
      ArrayList<Position> box = this.getGameObject().getComponent(CollisionBox.class).getCollisionBox();
      HashMap<Position, String> occupiedCoordinates = DataServer.INSTANCE.getOccupiedCoordinates();

      //checks if any of the positions already exist
      for(int i=0; i<box.size(); i++){

        System.out.println("Checking box");
        positionClean = !occupiedCoordinates.containsKey(box.get(i));

      }
    }

    TankObject tank = new TankObject(getGameObject().getChildren().get("Player Container"),
        player,
        position,
        100,
        30,
        isAI);

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

  public GameObject getRoot() {
    return root;
  }
}
