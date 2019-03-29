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
import com.aticatac.common.GameResult;
import com.aticatac.common.Stoppable;
import com.aticatac.common.model.CommandModel;
import com.aticatac.common.model.Vector;
import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.common.objectsystem.containers.KillLogEvent;
import com.google.common.collect.Streams;
import com.google.common.eventbus.Subscribe;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.log4j.Logger;

import static com.aticatac.client.bus.EventBusFactory.serverEventBus;

/**
 * The type Game mode.
 */
@SuppressWarnings("ALL")
public class BaseGame implements Runnable, Callable<GameResult>, Stoppable {
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
  /**
   * The Bullets.
   */
  protected final CopyOnWriteArraySet<Bullet> bullets;
  /**
   * The Powerups.
   */
  protected final CopyOnWriteArraySet<Entity> powerups;
  /**
   * The Frame.
   */
  protected final Logger logger;
  private final int min = 0;
  private final int max = 1920;
  /**
   * The Power up count.
   */
  protected int powerUpCount;
  private AIUpdateService aiUpdateService;
  private volatile boolean run;
  private int counter;
  private int playerCount;

  /**
   * Instantiates a new Game mode.
   *
   * @throws InvalidClassInstance     the invalid class instance
   * @throws ComponentExistsException the component exists exception
   */
  public BaseGame() {
    this.logger = Logger.getLogger(getClass());
    serverEventBus.register(this);
    this.playerMap = new ConcurrentHashMap<>();
    bullets = new CopyOnWriteArraySet<>();
    powerups = new CopyOnWriteArraySet<>();
    aiUpdateService = new AIUpdateService(playerMap, powerups);
  }

  /**
   * In map boolean.
   *
   * @param v the v
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

  /**
   * Reset.
   */
  public void reset() {
    for (Tank tank : playerMap.values()) {
      DataServer.INSTANCE.removeBoxFromData(tank.getCollisionBox());
      tank.reset(getClearPosition(EntityType.TANK.radius));
    }
    playerCount = playerMap.size();
    bullets.clear();
    powerups.clear();
  }

  /**
   * Process player input.
   *
   * @param event the event
   */
  @Subscribe
  public void processPlayerInput(CommandModel event) {
    if (playerMap.containsKey(event.getId())) {
      playerMap.get(event.getId()).addFrame(event);
    }
  }

  /**
   * Gets powerups.
   *
   * @return the powerups
   */
  public CopyOnWriteArraySet<Entity> getPowerups() {
    return powerups;
  }

  /**
   * Gets bullets.
   *
   * @return the bullets
   */
  public CopyOnWriteArraySet<Bullet> getBullets() {
    return bullets;
  }

  /**
   * Gets player map.
   *
   * @return the player map
   */
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

  /**
   * Add player.
   *
   * @param player the player
   */
  public void addPlayer(String player) {
    if (!playerMap.containsKey(player)) {
      Tank tank = createTank(player, false);
      playerMap.put(player, tank);
      addAI("ai");
    }
  }

  /**
   * Remove player.
   *
   * @param player the player
   */
  public void removePlayer(String player) {
    Position position = playerMap.get(player).getPosition();
    serverEventBus.post(new PlayersChangedEvent(PlayersChangedEvent.Action.REMOVE, playerMap.get(player).getPlayerContainer()));
    DataServer.INSTANCE.removeBoxFromData(playerMap.get(player).getCollisionBox());
    playerMap.get(player).deactivate();
    playerCount--;
    if (playerCount <= 1) {
      run = false;
    }
    Entity newPowerup = new Entity(String.valueOf(Objects.hash(EntityType.AMMO_POWERUP, position)), EntityType.AMMO_POWERUP, position);
    DataServer.INSTANCE.addEntity(newPowerup);
    powerups.add(newPowerup);
    serverEventBus.post(new PowerupsChangedEvent(PowerupsChangedEvent.Action.ADD, newPowerup.getContainer()));
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

  /**
   * Create tank tank.
   *
   * @param player the player
   * @param isAI   the is ai
   * @param x      the x
   * @param y      the y
   * @return the tank
   */
  public Tank createTank(String player, boolean isAI, int x, int y) {
    Position position = new Position(x, y);
    if (isAI) {
      AITank tank = new AITank(player, position, 100, 30, playerMap.size());
      DataServer.INSTANCE.setCoordinates(position, tank);
      this.logger.info(tank);
      serverEventBus.post(new PlayersChangedEvent(PlayersChangedEvent.Action.ADD, tank.getPlayerContainer()));
      playerCount++;
      return tank;
    } else {
      Tank tank = new Tank(player, position, 100, 30, playerMap.size());
      DataServer.INSTANCE.setCoordinates(position, tank);
      this.logger.info(tank);
      serverEventBus.post(new PlayersChangedEvent(PlayersChangedEvent.Action.ADD, tank.getPlayerContainer()));
      playerCount++;
      return tank;
    }
  }

  /**
   * Create power ups.
   */
  protected void createPowerUps() {
    if (powerUpCount <= 0) {
      EntityType type = Entity.randomPowerUP();
      Position p = getClearPosition(type.radius);
      Entity newPowerup = new Entity(String.valueOf(Objects.hash(type, p)), type, p);
      DataServer.INSTANCE.addEntity(newPowerup);
      powerups.add(newPowerup);
      powerUpCount = 600;
      serverEventBus.post(new PowerupsChangedEvent(PowerupsChangedEvent.Action.ADD, newPowerup.getContainer()));
    }
  }

  /**
   * Process player collision.
   *
   * @param e the e
   */
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
        DataServer.INSTANCE.removeBoxFromData(e.getHit().getCollisionBox());
        serverEventBus.post(new PowerupsChangedEvent(PowerupsChangedEvent.Action.REMOVE, e.getHit().getContainer(), e.getEntity().getContainer()));
        powerups.remove(e.getHit());
        playerMap.get(e.getEntity().getName()).ammoIncrease(10);
        break;
      case SPEED_POWERUP:
        DataServer.INSTANCE.removeBoxFromData(e.getHit().getCollisionBox());
        serverEventBus.post(new PowerupsChangedEvent(PowerupsChangedEvent.Action.REMOVE, e.getHit().getContainer(), e.getEntity().getContainer()));
        powerups.remove(e.getHit());
        playerMap.get(e.getEntity().getName()).setSpeedIncrease(1200);
        break;
      case HEALTH_POWERUP:
        DataServer.INSTANCE.removeBoxFromData(e.getHit().getCollisionBox());
        serverEventBus.post(new PowerupsChangedEvent(PowerupsChangedEvent.Action.REMOVE, e.getHit().getContainer(), e.getEntity().getContainer()));
        powerups.remove(e.getHit());
        playerMap.get(e.getEntity().getName()).heal(10);
        break;
      case DAMAGE_POWERUP:
        DataServer.INSTANCE.removeBoxFromData(e.getHit().getCollisionBox());
        serverEventBus.post(new PowerupsChangedEvent(PowerupsChangedEvent.Action.REMOVE, e.getHit().getContainer(), e.getEntity().getContainer()));
        powerups.remove(e.getHit());
        playerMap.get(e.getEntity().getName()).setDamageIncrease(1200);
        break;
      case BULLETSPRAY_POWERUP:
        // TODO: Tell player they can press shift to unleash a bullet spray
        DataServer.INSTANCE.removeBoxFromData(e.getHit().getCollisionBox());
        serverEventBus.post(new PowerupsChangedEvent(PowerupsChangedEvent.Action.REMOVE, e.getHit().getContainer(), e.getEntity().getContainer()));
        powerups.remove(e.getHit());
        playerMap.get(e.getEntity().getName()).setBulletSprays(playerMap.get(e.getEntity().getName()).getBulletSprays() + 1);
        break;
      case FREEZEBULLET_POWERUP:
        // TODO: Tell player they can press control to shoot a freeze bullet
        DataServer.INSTANCE.removeBoxFromData(e.getHit().getCollisionBox());
        serverEventBus.post(new PowerupsChangedEvent(PowerupsChangedEvent.Action.REMOVE, e.getHit().getContainer(), e.getEntity().getContainer()));
        powerups.remove(e.getHit());
        playerMap.get(e.getEntity().getName()).setFreezeBullets(playerMap.get(e.getEntity().getName()).getFreezeBullets() + 1);
        break;
    }
  }

  /**
   * Bullet collision.
   *
   * @param event the event
   */
  @Subscribe
  public void bulletCollision(BulletCollisionEvent event) {
    if (event.getHit().getType() == EntityType.TANK) {
      int result = playerMap.get(event.getHit().getName()).hit(event.getBullet().getDamage(), event.getBullet().getFreezeBullet());
      if (result <= 0) {
        serverEventBus.post(new KillLogEvent(event.getBullet().getShooter().getName(), event.getHit().getName()));
      }
    }
    bullets.remove(event.getBullet());
    serverEventBus.post(new BulletsChangedEvent(BulletsChangedEvent.Action.REMOVE, event.getBullet().getContainer()));
  }

  /**
   * Process player output.
   *
   * @param output the output
   */
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
    try {
      call();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public GameResult call() throws Exception {
    reset();
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
    GameResult result = new GameResult();
    for (Tank tank : playerMap.values()) {
      if (tank.isAlive()) {
        result.addWinner(tank.getName());
        result.addKD(tank.getName(), tank.getKillCount(), 0);
      }
    }
    return result;
  }

  @Override
  public void shutdown() {
    run = false;
    reset();
    aiUpdateService = new AIUpdateService(playerMap, powerups);
  }
}
