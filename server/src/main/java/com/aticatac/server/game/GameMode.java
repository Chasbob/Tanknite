package com.aticatac.server.game;

import com.aticatac.common.model.CommandModel;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.model.Vector;
import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.Position;
import com.aticatac.server.bus.EventBusFactory;
import com.aticatac.server.bus.event.BulletCollisionEvent;
import com.aticatac.server.bus.event.BulletsChangedEvent;
import com.aticatac.server.bus.event.PlayersChangedEvent;
import com.aticatac.server.bus.event.PowerupsChangedEvent;
import com.aticatac.server.bus.event.ShootEvent;
import com.aticatac.server.bus.event.TankCollisionEvent;
import com.aticatac.server.objectsystem.DataServer;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.entities.AITank;
import com.aticatac.server.objectsystem.entities.Bullet;
import com.aticatac.server.objectsystem.entities.Tank;
import com.aticatac.server.objectsystem.entities.powerups.AmmoPowerup;
import com.aticatac.server.objectsystem.physics.CollisionBox;
import com.google.common.eventbus.Subscribe;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.log4j.Logger;

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
  protected final CopyOnWriteArraySet<Entity> powerups;
  /**
   * The Frame.
   */
  protected final Update frame;
  protected final Logger logger;
  private final int min = 0;
  private final int max = 1920;

  /**
   * Instantiates a new Game mode.
   *
   * @throws InvalidClassInstance     the invalid class instance
   * @throws ComponentExistsException the component exists exception
   */
  GameMode() {
    this.playerMap = new ConcurrentHashMap<>();
    this.logger = Logger.getLogger(getClass());
    frame = new Update();
    bullets = new CopyOnWriteArraySet<>();
    powerups = new CopyOnWriteArraySet<>();
    EventBusFactory.getEventBus().register(this);
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

  private static EntityType randomPowerUP() {
    SecureRandom random = new SecureRandom();
    EntityType entityType = EntityType.NONE;
    while (!entityType.isPowerUp()) {
      int x = random.nextInt((EntityType.class).getEnumConstants().length);
      entityType = (EntityType.class).getEnumConstants()[x];
    }
    return entityType;
  }

  @Subscribe
  public void processPlayerInput(CommandModel event) {
    if (playerMap.containsKey(event.getId())) {
      playerMap.get(event.getId()).addFrame(event);
    }
  }

  public CopyOnWriteArraySet<Entity> getPowerups() {
    return powerups;
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
//      for (int i = 0; i < 1; i++) {
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
    AmmoPowerup ammoPowerUp = null; // get name of powerUp
    Position ammoPosition = getClearPosition();
    ammoPowerUp = new AmmoPowerup("ammoPowerUp", ammoPosition);
    DataServer.INSTANCE.addBoxToData(ammoPowerUp.getCollisionBox(), ammoPowerUp);
    powerups.add(ammoPowerUp);
    this.logger.trace("Adding: " + ammoPowerUp.toString());
  }

  @Override
  public void startGame() {
  }

  @Override
  public void gameOver() {
  }

  private Tank createTank(String player, boolean isAI) {
//I can't be bothered to reason how this would work with booleans so you get this counter
//    Position position = getClearPosition(EntityType.TANK.radius);
    Position position = getClearPosition();
    return createTank(player, isAI, position.getX(), position.getY());
  }

  //todo game this for given radius
  private Position getClearPosition(int radius) {
    int count = 1;
    Position position = new Position();
    ConcurrentHashMap<Position, Entity> map = DataServer.INSTANCE.getOccupiedCoordinates();
    while (count > 0) {
      count = 0;
      position = new Position(ThreadLocalRandom.current().nextInt(min, max + 1),
          ThreadLocalRandom.current().nextInt(min, max + 1));
      this.logger.trace("Trying position: " + position.toString());
      CollisionBox box = new CollisionBox(position, radius);
      HashSet<Position> boxCheck = box.getBox();
      for (int i = 0; i < boxCheck.size(); i++) {
        if (map.containsKey(boxCheck.contains(i))) {
          count++;
        }
      }
    }
    return position;
  }

  private Position getClearPosition() {
    int count = 1;
    Position position = new Position();
    ConcurrentHashMap<Position, Entity> map = DataServer.INSTANCE.getOccupiedCoordinates();
    while (count > 0) {
      count = 0;
      position = new Position(ThreadLocalRandom.current().nextInt(min, max + 1),
          ThreadLocalRandom.current().nextInt(min, max + 1));
      this.logger.trace("Trying position: " + position.toString());
      CollisionBox box = new CollisionBox(position, EntityType.TANK.radius);
      HashSet<Position> boxCheck = box.getBox();
      for (int i = 0; i < boxCheck.size(); i++) {
        if (map.containsKey(boxCheck.contains(i))) {
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
      DataServer.INSTANCE.setCoordinates(position, tank);
      return tank;
    } else {
      Tank tank = new Tank(player, position, 100, 30);
      DataServer.INSTANCE.setCoordinates(position, tank);
      return tank;
    }
  }

  protected void createPowerUps() {
    EntityType type = Entity.randomPowerUP();
    Position p = getClearPosition(type.radius);
    Entity newPowerup = new Entity(String.valueOf(Objects.hash(type, p)), type, p);
    DataServer.INSTANCE.addEntity(newPowerup);
    powerups.add(newPowerup);
    EventBusFactory.getEventBus().post(new PowerupsChangedEvent(PowerupsChangedEvent.Action.ADD, newPowerup.getContainer()));
  }

  @Subscribe
  public void processPlayerCollision(TankCollisionEvent e) {
    switch (e.getHit().getType()) {
      case NONE:
        break;
      case TANK:
        break;
      case BULLET:
        break;
      case WALL:
        break;
      case OUTOFBOUNDS:
        break;
      case AMMO_POWERUP:
      case SPEED_POWERUP:
      case HEALTH_POWERUP:
      case DAMAGE_POWERUP:
        this.logger.info(e);
        DataServer.INSTANCE.removeBoxFromData(e.getHit().getCollisionBox());
        EventBusFactory.getEventBus().post(new PowerupsChangedEvent(PowerupsChangedEvent.Action.REMOVE, e.getHit().getContainer()));
        powerups.remove(e.getHit());
        playerMap.get(e.getEntity()).setDamageIncrease(true);
        break;
    }
  }

  @Subscribe
  public void bulletCollision(BulletCollisionEvent event) {
    this.logger.info(event);
    if (event.getHit().getType() == EntityType.TANK) {
      playerMap.get(event.getHit().getName()).hit(event.getBullet().getDamage());
      this.logger.info(event);
    }
    bullets.remove(event.getBullet());
    EventBusFactory.getEventBus().post(new BulletsChangedEvent(BulletsChangedEvent.Action.REMOVE, event.getBullet().getContainer()));
  }

  @Subscribe
  public void processPlayerOutput(ShootEvent output) {
    bullets.add(output.getBullet());
  }

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
