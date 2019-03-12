package com.aticatac.server.components;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.model.Command;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.controller.TankController;
import com.aticatac.server.gamemanager.Manager;
import com.aticatac.server.prefabs.TankObject;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class GameManager extends Component {
  private HashMap<String, GameObject> playerMap = new HashMap<>();

  /**
   * Instantiates a new Component.
   *
   * @param gameObject the component parent
   */
  public GameManager(GameObject gameObject) {
    super(gameObject);
    try {
      new GameObject("Player Container", this.getGameObject());
    } catch (Exception ignored) {
    }
  }

  public HashMap<String, GameObject> getPlayerMap() {
    return playerMap;
  }

  public void addPlayer(String player) {
    if (!playerMap.containsKey(player)) {
      playerMap.put(player, createTank(player, false));
      Position p = playerMap.get(player).getTransform().getPosition();
      playerMap.put(player + "AI", createTank(player + "AI", true, p.getX(), p.getY() + 40));
    }
  }

  //TODO addAI which passes in that AI is true.
  //This needs to be called upon the start of the game as all clients shld then be added.
  public void addAI(String name) {
    //IN here it could check the count to know how many times it needs to create an AI
    //count how many times the addPLayer is called.
    if (!playerMap.containsKey(name)) {
      playerMap.put(name, createTank(name, true));
    }
  }

  public void removeClient(String username) {
    playerMap.remove(username);
  }

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

  public TankObject createTank(String player, boolean isAI) {
    try {
      Position position = new Position(ThreadLocalRandom.current().nextInt(Manager.INSTANCE.getMin(), Manager.INSTANCE.getMax() + 1),
      ThreadLocalRandom.current().nextInt(Manager.INSTANCE.getMin(), Manager.INSTANCE.getMax() + 1));
      //checks if this is a valid coordinate when generated is not in the map then moves on.
      while (DataServer.INSTANCE.getOccupiedCoordinates().containsKey(position)) {
        position = new Position(ThreadLocalRandom.current().nextInt(Manager.INSTANCE.getMin(), Manager.INSTANCE.getMax() + 1),
        ThreadLocalRandom.current().nextInt(Manager.INSTANCE.getMin(), Manager.INSTANCE.getMax() + 1));
      }
      TankObject tank = new TankObject(getGameObject().getChildren().get("Player Container"),
      player,
      position,
      100,
      30,
      isAI);
      DataServer.INSTANCE.setCoordinates(position, "Tank");
      return tank;
    } catch (InvalidClassInstance | ComponentExistsException e) {
      this.logger.error(e);
    }
    return null;
  }

  public TankObject createTank(String player, boolean isAI, int x, int y) {
    try {
      Position position = new Position(x, y);
      TankObject tank = new TankObject(getGameObject().getChildren().get("Player Container"),
      player,
      position,
      100,
      30,
      isAI);
      DataServer.INSTANCE.setCoordinates(position, "Tank");
      return tank;
    } catch (InvalidClassInstance | ComponentExistsException e) {
      this.logger.error(e);
    }
    return null;
  }
}
