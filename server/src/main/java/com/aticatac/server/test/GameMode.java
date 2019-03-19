package com.aticatac.server.test;

import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.model.CommandModel;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.model.Vector;
import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.bus.EventBusFactory;
import com.aticatac.server.bus.event.PlayersChangedEvent;
import com.aticatac.server.bus.listener.BulletCollisionListener;
import com.aticatac.server.bus.listener.PlayerInputListener;
import com.aticatac.server.bus.listener.PlayerOutputListener;
import com.aticatac.server.components.transform.Position;
import com.aticatac.server.objectsystem.DataServer;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.IO.inputs.PlayerInput;
import com.aticatac.server.objectsystem.entities.AITank;
import com.aticatac.server.objectsystem.entities.Bullet;
import com.aticatac.server.objectsystem.entities.Tank;
import com.aticatac.server.objectsystem.entities.powerups.AmmoPowerup;
import com.aticatac.server.objectsystem.entities.powerups.DamagePowerup;
import com.aticatac.server.objectsystem.entities.powerups.HealthPowerup;
import com.aticatac.server.objectsystem.entities.powerups.SpeedPowerup;
import com.aticatac.server.objectsystem.physics.CollisionBox;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * The type Game mode.
 */
@SuppressWarnings("ALL")
public abstract class GameMode implements Game {
  /**
   * The constant maxXY.
   */
  public static final Vector maxXY = new Vector(1920, 1920);
  /**
   * The constant maxX.
   */
  public static final Vector maxX = new Vector(1920, 0);
  /**
   * The constant maxY.
   */
  public static final Vector maxY = new Vector(0, 1920);
  /**
   * The Player map.
   */
  protected final ConcurrentHashMap<String, Tank> playerMap;
  protected final CopyOnWriteArraySet<Bullet> bullets;
//  protected final ArrayList<Bullet> bullets;
  /**
   * The Frame.
   */
  protected final Update frame;
  protected final Logger logger;
  private final int min = 320;
  private final int max = 1920 - 320;
//  private GameObject root;

  /**
   * Instantiates a new Game mode.
   *
   * @throws InvalidClassInstance     the invalid class instance
   * @throws ComponentExistsException the component exists exception
   */
  GameMode() throws InvalidClassInstance, ComponentExistsException {
//    this.root = new GameObject("root", ObjectType.ROOT);
//    new GameObject("Player Container", root, ObjectType.PLAYER_CONTAINER);
    this.playerMap = new ConcurrentHashMap<>();
    this.logger = Logger.getLogger(getClass());
    frame = new Update(true);
    bullets = new CopyOnWriteArraySet<>();
    EventBusFactory.getEventBus().register(new PlayerInputListener(this.playerMap));
    EventBusFactory.getEventBus().register(new PlayerOutputListener(this.bullets));
    EventBusFactory.getEventBus().register(new BulletCollisionListener(this.playerMap, this.bullets));
  }

  /**
   * In map boolean.
   *
   * @param v the v
   *
   * @return the boolean
   */
  public static boolean inMap(Vector v) {
    return (v.x < maxXY.x && v.y < maxXY.y);
  }

  public CopyOnWriteArraySet<Bullet> getBullets() {
    return bullets;
  }

  public ConcurrentHashMap<String, Tank> getPlayerMap() {
    return playerMap;
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
      Tank tank = createTank(player, false);
      playerMap.put(player, tank);
      EventBusFactory.getEventBus().post(new PlayersChangedEvent(PlayersChangedEvent.Action.ADD, tank.getContainer()));
//      for (int i = 0; i < 10; i++) {
//        Tank tank2 = createTank(player + " AI" + i, true);
//        playerMap.put(player + " AI" + i, tank2);
//        EventBusFactory.getEventBus().post(new PlayersChangedEvent(PlayersChangedEvent.Action.ADD, tank2.getContainer()));
//      }
    }
  }

  @Override
  public void removePlayer(String player) {
    EventBusFactory.getEventBus().post(new PlayersChangedEvent(PlayersChangedEvent.Action.REMOVE, playerMap.get(player).getContainer()));
    playerMap.remove(player);
  }

  @Override
  public void startGame() {
  }

  @Override
  public void gameOver() {
  }

  @Override
  public void playerInput(CommandModel model) {
    //Gets the tank that the command came from
    PlayerInput input = new PlayerInput(model);
    var tank = playerMap.get(model.getId());
    tank.addFrame(input);
  }

  private Tank createTank(String player, boolean isAI) {
    //I can't be bothered to reason how this would work with booleans so you get this counter
    Position position = getClearPosition();
    return createTank(player, isAI, position.getX(), position.getY());
  }

  @NotNull
  private Position getClearPosition() {
    int count = 1;
    Position position = new Position();
    ConcurrentHashMap<Position, Entity> map = DataServer.INSTANCE.getOccupiedCoordinates();
    while (count > 0) {
      count = 0;
      position = new Position(ThreadLocalRandom.current().nextInt(min, max + 1),
          ThreadLocalRandom.current().nextInt(min, max + 1));
      this.logger.info("Trying position: " + position.toString());
      CollisionBox box = new CollisionBox(position, EntityType.TANK.radius);
      ArrayList<Position> boxCheck = box.getBox();
      for (int i = 0; i < boxCheck.size(); i++) {
        if (map.containsKey(boxCheck.get(i))) {
          count++;
        }
      }
    }
    return position;
  }

  private Tank createTank(String player, boolean isAI, int x, int y) {
    Position position = new Position(x, y);
    if (isAI) {
      AITank tank = new AITank(player, position, 100, 30);
      DataServer.INSTANCE.setCoordinates(position, tank.getBaseEntity());
      return tank;
    } else {
      Tank tank = new Tank(player, position, 100, 30);
      DataServer.INSTANCE.setCoordinates(position, tank.getBaseEntity());
      return tank;
    }
  }

  /**
   * Gets root.
   *
   * @return the root
   */
//  public GameObject getRoot() {
//    return root;
//  }
  private void createPowerUps() {
    // TODO: Make PowerUpController tick based
    // TODO: Where should this method be called?
    int randomTime = new Random().nextInt(15);
    //delay for randomTime
    try {
      Thread.sleep(randomTime * 1000);
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
    }
    EntityType powerUpId = Entity.randomPowerUP();
//do we want to keep track of the power ups that exist or just put them in the map?
    switch (powerUpId) {
      case HEALTH_POWERUP:
        HealthPowerup healthPowerUp = null; // get name of powerUp
        Position healthPosition = getClearPosition();
        healthPowerUp = new HealthPowerup("healthPowerUp", healthPosition);
        DataServer.INSTANCE.setCoordinates(healthPosition, healthPowerUp.getBaseEntity());
        break;
      case AMMO_POWERUP:
        AmmoPowerup ammoPowerUp = null; // get name of powerUp
        Position ammoPosition = getClearPosition();
        ammoPowerUp = new AmmoPowerup("ammoPowerUp", ammoPosition);
        DataServer.INSTANCE.setCoordinates(ammoPosition, ammoPowerUp.getBaseEntity());
        break;
      case DAMAGE_POWERUP:
        DamagePowerup damagePowerUp = null; // get name of powerUp
        Position damagePosition = getClearPosition();
        damagePowerUp = new DamagePowerup("damagePowerUp", damagePosition);
        DataServer.INSTANCE.setCoordinates(damagePosition, damagePowerUp.getBaseEntity());
        break;
      case SPEED_POWERUP:
        SpeedPowerup speedPowerUp = null; // get name of powerUp
        Position speedPosition = getClearPosition();
        speedPowerUp = new SpeedPowerup("speedPowerUp", speedPosition);
        DataServer.INSTANCE.setCoordinates(speedPosition, speedPowerUp.getBaseEntity());
    }
  }
//  public Position powerupPosition() {
//    ConcurrentHashMap<Position, Entity> map = DataServer.INSTANCE.getOccupiedCoordinates();
//    Position position = new Position(ThreadLocalRandom.current().nextInt(min, max + 1),
//        ThreadLocalRandom.current().nextInt(min, max + 1));
//    CollisionBox box = new CollisionBox(position, EntityType.TANK.radius); // TODO: will tank have same radius as power ups?
//    //checks if this is a valid coordinate when generated is not in the map then moves on.
//    while (DataServer.INSTANCE.getOccupiedCoordinates().containsKey(position) || DataServer.INSTANCE.containsBox(box)) {
//      position = new Position(ThreadLocalRandom.current().nextInt(min, max + 1),
//          ThreadLocalRandom.current().nextInt(min, max + 1));
//      box.setPosition(position);
//    }
//    return position;
//  }

  /**
   * N add ai.
   *
   * @param n the n
   */
  public void nAddAI(int n) {
    for (int i = 0; i < n; i++) {
      addAI("AiTank " + Integer.toString(i + 1));
    }
  }

  private void addAI(String id) {
    if (!playerMap.containsKey(id)) {
      playerMap.put(id, createTank(id, true));
      Position p = playerMap.get(id).getPosition();
    }
  }
}
