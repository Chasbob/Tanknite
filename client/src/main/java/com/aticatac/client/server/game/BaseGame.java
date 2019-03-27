package com.aticatac.client.server.game;

import com.aticatac.client.server.Position;
import com.aticatac.client.server.bus.event.BulletCollisionEvent;
import com.aticatac.client.server.bus.event.BulletsChangedEvent;
import com.aticatac.client.server.bus.event.PlayersChangedEvent;
import com.aticatac.client.server.bus.event.PowerupsChangedEvent;
import com.aticatac.client.server.bus.event.ShootEvent;
import com.aticatac.client.server.bus.event.TankCollisionEvent;
import com.aticatac.client.server.bus.service.AIUpdateService;
import com.aticatac.client.server.objectsystem.DataServer;
import com.aticatac.client.server.objectsystem.Entity;
import com.aticatac.client.server.objectsystem.entities.AITank;
import com.aticatac.client.server.objectsystem.entities.Bullet;
import com.aticatac.client.server.objectsystem.entities.Tank;
import com.aticatac.client.server.objectsystem.interfaces.Tickable;
import com.aticatac.client.server.objectsystem.physics.CollisionBox;
import com.aticatac.common.model.CommandModel;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.model.Vector;
import com.aticatac.common.objectsystem.EntityType;
import com.google.common.collect.Streams;
import com.google.common.eventbus.Subscribe;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.log4j.Logger;

import static com.aticatac.client.server.bus.EventBusFactory.eventBus;

/**
 * The type Game mode.
 */
@SuppressWarnings("ALL")
public class BaseGame implements Runnable {
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
  private final AIUpdateService aiUpdateService;
  protected int powerUpCount;
  private volatile boolean run;
  private int counter;

  /**
   * Instantiates a new Game mode.
   *
   * @throws InvalidClassInstance     the invalid class instance
   * @throws ComponentExistsException the component exists exception
   */
  public BaseGame() {
    this.logger = Logger.getLogger(getClass());
    eventBus.register(this);
    this.playerMap = new ConcurrentHashMap<>();
    frame = new Update();
    bullets = new CopyOnWriteArraySet<>();
    powerups = new CopyOnWriteArraySet<>();
    aiUpdateService = new AIUpdateService(playerMap, powerups);
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

  public void addPlayer(String player) {
    if (!playerMap.containsKey(player)) {
      Tank tank = createTank(player, false);
      playerMap.put(player, tank);
      eventBus.post(new PlayersChangedEvent(PlayersChangedEvent.Action.ADD, tank.getContainer()));
      for (int i = 0; i < 1; i++) {
        Tank tank2 = createTank(player + " AI" + i, true);
        playerMap.put(player + " AI" + i, tank2);
        eventBus.post(new PlayersChangedEvent(PlayersChangedEvent.Action.ADD, tank2.getContainer()));
      }
    }
  }

  public void removePlayer(String player) {
    eventBus.post(new PlayersChangedEvent(PlayersChangedEvent.Action.REMOVE, playerMap.get(player).getContainer()));
    Position position = playerMap.get(player).getPosition();
    eventBus.post(new PlayersChangedEvent(PlayersChangedEvent.Action.REMOVE, playerMap.get(player).getContainer()));
    DataServer.INSTANCE.removeBoxFromData(playerMap.get(player).getCollisionBox());
    playerMap.remove(player);
    Entity newPowerup = new Entity(String.valueOf(Objects.hash(EntityType.AMMO_POWERUP, position)), EntityType.AMMO_POWERUP, position);
    DataServer.INSTANCE.addEntity(newPowerup);
    powerups.add(newPowerup);
    eventBus.post(new PowerupsChangedEvent(PowerupsChangedEvent.Action.ADD, newPowerup.getContainer()));
  }

  private Tank createTank(String player, boolean isAI) {
//I can't be bothered to reason how this would work with booleans so you get this counter
//    Position position = getClearPosition(EntityType.TANK.radius);
    Position position = getClearPosition(EntityType.TANK.radius);
    return createTank(player, isAI, position.getX(), position.getY());
  }

  //todo game this for given radius
  private Position getClearPosition(int radius) {
    int count = 1;
    Position position = new Position();
    while (count > 0) {
      count = 0;
      ConcurrentHashMap<Position, Entity> map = DataServer.INSTANCE.getOccupiedCoordinates();
      position = new Position(ThreadLocalRandom.current().nextInt(min, max + 1), ThreadLocalRandom.current().nextInt(min, max + 1));
      CollisionBox box = new CollisionBox(position, radius);
      HashSet<Position> boxCheck = box.getBox();
      for (Position p : boxCheck) {
        if (map.containsKey(p)) {
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
    if (powerUpCount <= 0) {
      EntityType type = Entity.randomPowerUP();
      Position p = getClearPosition(type.radius);
      Entity newPowerup = new Entity(String.valueOf(Objects.hash(type, p)), type, p);
      DataServer.INSTANCE.addEntity(newPowerup);
      powerups.add(newPowerup);
      powerUpCount = 600;
      eventBus.post(new PowerupsChangedEvent(PowerupsChangedEvent.Action.ADD, newPowerup.getContainer()));
    }
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
        this.logger.info(e);
        DataServer.INSTANCE.removeBoxFromData(e.getHit().getCollisionBox());
        eventBus.post(new PowerupsChangedEvent(PowerupsChangedEvent.Action.REMOVE, e.getHit().getContainer()));
        powerups.remove(e.getHit());
        playerMap.get(e.getEntity().getName()).ammoIncrease(10);
        break;
      case SPEED_POWERUP:
        this.logger.info(e);
        DataServer.INSTANCE.removeBoxFromData(e.getHit().getCollisionBox());
        eventBus.post(new PowerupsChangedEvent(PowerupsChangedEvent.Action.REMOVE, e.getHit().getContainer()));
        powerups.remove(e.getHit());
        playerMap.get(e.getEntity().getName()).setSpeedIncrease(1200);
        break;
      case HEALTH_POWERUP:
        this.logger.info(e);
        DataServer.INSTANCE.removeBoxFromData(e.getHit().getCollisionBox());
        eventBus.post(new PowerupsChangedEvent(PowerupsChangedEvent.Action.REMOVE, e.getHit().getContainer()));
        powerups.remove(e.getHit());
        playerMap.get(e.getEntity().getName()).heal(10);
        break;
      case DAMAGE_POWERUP:
        this.logger.info(e);
        DataServer.INSTANCE.removeBoxFromData(e.getHit().getCollisionBox());
        eventBus.post(new PowerupsChangedEvent(PowerupsChangedEvent.Action.REMOVE, e.getHit().getContainer()));
        powerups.remove(e.getHit());
        playerMap.get(e.getEntity().getName()).setDamageIncrease(1200);
        break;
      case BULLETSPRAY_POWERUP:
        // TODO: Tell player they can press shift to unleash a bullet spray
        this.logger.info(e);
        DataServer.INSTANCE.removeBoxFromData(e.getHit().getCollisionBox());
        eventBus.post(new PowerupsChangedEvent(PowerupsChangedEvent.Action.REMOVE, e.getHit().getContainer()));
        powerups.remove(e.getHit());
        playerMap.get(e.getEntity().getName()).setBulletSprays(playerMap.get(e.getEntity().getName()).getBulletSprays() + 1);
        break;
      case FREEZEBULLET_POWERUP:
        // TODO: Tell player they can press control to shoot a freeze bullet
        this.logger.info(e);
        DataServer.INSTANCE.removeBoxFromData(e.getHit().getCollisionBox());
        eventBus.post(new PowerupsChangedEvent(PowerupsChangedEvent.Action.REMOVE, e.getHit().getContainer()));
        powerups.remove(e.getHit());
        playerMap.get(e.getEntity().getName()).setFreezeBullets(playerMap.get(e.getEntity().getName()).getFreezeBullets() + 1);
        break;
    }
  }

  @Subscribe
  public void bulletCollision(BulletCollisionEvent event) {
    this.logger.info(event);
    if (event.getHit().getType() == EntityType.TANK) {
      playerMap.get(event.getHit().getName()).hit(event.getBullet().getDamage(), event.getBullet().getFreezeBullet());
      this.logger.info(event);
    }
    bullets.remove(event.getBullet());
    eventBus.post(new BulletsChangedEvent(BulletsChangedEvent.Action.REMOVE, event.getBullet().getContainer()));
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

  @Override
  public void run() {
    counter = 0;
    run = true;
    this.logger.trace("Running...");
    while (run) {
      powerUpCount--;
      createPowerUps();
      this.logger.trace("tick");
      aiUpdateService.update(playerMap, powerups);
      Streams.concat(bullets.stream(), playerMap.values().stream()).forEach(Tickable::tick);
      double nanoTime = System.nanoTime();
      while (System.nanoTime() - nanoTime < 1000000000 / 60) {
        try {
          Thread.sleep(0);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}