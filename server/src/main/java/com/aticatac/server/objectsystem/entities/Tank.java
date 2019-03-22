package com.aticatac.server.objectsystem.entities;

import com.aticatac.common.model.Vector;
import com.aticatac.common.objectsystem.Container;
import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.ai.PlayerState;
import com.aticatac.server.bus.EventBusFactory;
import com.aticatac.server.bus.event.PlayersChangedEvent;
import com.aticatac.server.bus.event.TankCollisionEvent;
import com.aticatac.server.bus.service.PlayerOutputService;
import com.aticatac.server.networking.Server;
import com.aticatac.server.objectsystem.DataServer;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.IO.inputs.PlayerInput;
import com.aticatac.server.objectsystem.interfaces.Collidable;
import com.aticatac.server.objectsystem.interfaces.DependantTickable;
import com.aticatac.server.objectsystem.interfaces.Hurtable;
import com.aticatac.server.objectsystem.physics.Physics;
import com.aticatac.server.transform.Position;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;

public class Tank<T extends PlayerInput> extends Entity implements DependantTickable<PlayerInput>, Hurtable {
  protected final ConcurrentLinkedQueue<PlayerInput> frames;
  //  protected final Entity entity;
  protected final Logger logger;
  protected final PlayerOutputService outputService;
  private final Physics physics;
  //  protected Position position;
  protected PlayerInput input;
  //  protected CollisionBox box;
  protected int health;
  protected int maxHealth;
  protected int maxAmmo;
  protected int ammo;
  protected boolean damageIncrease = false;
  protected boolean speedIncrease = false;
  protected boolean shuttingDown = false;
  private int framesToShoot;

  //todo add in a parameter boolean which is ai true or false
  //TODO add in the parameter changes everywhere
  public Tank(String name, Position p, int health, int ammo) {
    super(name, EntityType.TANK, p);
//    entity = new Entity(name, EntityType.TANK);
    frames = new ConcurrentLinkedQueue<>();
    input = new PlayerInput();
//    position = p;
    logger = Logger.getLogger(getClass());
//    this.box = new CollisionBox(position, EntityType.TANK);
    this.maxHealth = 100;
    this.health = health;
    this.ammo = ammo;
    this.maxAmmo = 30;
    this.outputService = new PlayerOutputService(getBaseEntity());
    physics = new Physics(position, type, name);
    framesToShoot = 120;
  }

  public Entity getEntity() {
    return getBaseEntity();
  }

  @Override
  public void tick() {
    logger.trace("tick");
    if (!frames.isEmpty()) {
      input = frames.poll();
      this.logger.trace(input.shoot);
      if (!input.moveCommands.isEmpty()) {
        Vector result = new Vector();
        for (int i = 0; i < input.moveCommands.size(); i++) {
          logger.trace("result at i = " + i + ": " + result.toString());
          result.add(input.moveCommands.get(i).vector);
        }
        logger.trace("Result: " + result.angle());
        try {
          if (health > 10) {
            move(result.angle(), speedIncrease);
          }
        } catch (Exception e) {
          this.logger.error(e);
          this.logger.error("Error while moving.");
        }
      }
      if (input.shoot) {
        this.logger.info("shoot");
        if (!(ammo == 0 || health == 0) && framesToShoot < 0) {
          setAmmo(ammo - 1);
          if (damageIncrease) {
            outputService.addBullet(new Bullet(getBaseEntity(), position.copy(), input.bearing, 20));
          } else {
            outputService.addBullet(new Bullet(getBaseEntity(), position.copy(), input.bearing, 10));
          }
          framesToShoot = 60;
//          DataServer.INSTANCE.addBoxToData(new CollisionBox(position, EntityType.TANK.radius), getBaseEntity());
//        this.getComponent(TurretController.class).shoot(input.bearing);
        }
      }
    }
    if (framesToShoot == 1) {
      this.logger.info("Ready to fire!");
    }
    framesToShoot--;
//    framesToShoot = clamp(framesToShoot - 1);
  }

  public void addFrame(final PlayerInput frame) {
    frames.add(frame);
  }

  public void addAndConsume(final PlayerInput frame) {
    frames.add(frame);
    tick();
  }

  void move(int bearing, final boolean speedIncrease) {
    var response = physics.move(bearing, position, speedIncrease);
    if (!response.getCollisions().contains(outOfBounds)) {
      for (Entity e :
          response.getCollisions()) {
        if (e.type != EntityType.NONE && e.type != EntityType.WALL && !e.name.equals(name)) {
          this.logger.info(e);
        }
        switch (e.type) {
          case NONE:
            break;
          case TANK:
            if (!e.name.equals(name)) {
              return;
            }
            break;
          case BULLET:
            break;
          case WALL:
            return;
          case OUTOFBOUNDS:
            return;
          case AMMO_POWERUP:
            int newAmmo = ammo + 10;
            if (newAmmo > maxAmmo) {
              ammo = maxAmmo;
            } else {
              ammo = newAmmo;
            }
            outputService.onPlayerHit(e, getContainer());
            EventBusFactory.getEventBus().post(new TankCollisionEvent(getEntity(), e));
            break;
          case SPEED_POWERUP:
            // TODO: Implement thread for 20 seconds (in terms of ticks) where speedIncrease = true
            outputService.onPlayerHit(e, getContainer());
            EventBusFactory.getEventBus().post(new TankCollisionEvent(getEntity(), e));
            break;
          case HEALTH_POWERUP:
            heal(10);
            outputService.onPlayerHit(e, getContainer());
            EventBusFactory.getEventBus().post(new TankCollisionEvent(getEntity(), e));
            break;
          case DAMAGE_POWERUP:
            outputService.onPlayerHit(e, getContainer());
            EventBusFactory.getEventBus().post(new TankCollisionEvent(getEntity(), e));
            break;
        }
      }
      setPosition(response.getPosition(), true);
      EventBusFactory.getEventBus().post(new PlayersChangedEvent(PlayersChangedEvent.Action.UPDATE, getContainer()));
    }
  }

  public int getAmmo() {
    return ammo;
  }

  public void setAmmo(int ammo) {
    this.ammo = ammo;
  }

  @Override
  public int getHealth() {
    return 0;
  }

  @Override
  public int hit(final int damage) {
    this.logger.info(clamp(health - damage));
    if (health <= 10 && health > 0) {
      // TODO, Thread to call remove player after 20 seconds (in terms of ticks)
    } else if (health <= 0) {
      Server.ServerData.INSTANCE.getGame().removePlayer(this.getName());
    }
    return health = clamp(health - damage);
  }

  @Override
  public int heal(final int amount) {
    return health = clamp(health + amount);
  }

  private int clamp(int value) {
    if (value < 0) {
      return 0;
    }
    if (value > maxHealth) {
      return maxHealth;
    }
    return value;
  }

  public Container getContainer() {
    return new Container(position.getX(), position.getY(), 0, health, ammo, name, EntityType.TANK);
  }

  @Override
  public boolean intersects(final Collidable collidable) {
    ArrayList<Position> other = collidable.getCollisionBox().getBox();
    other.addAll(collisionBox.getBox());
    return other.size() != (new HashSet<>(other).size());
  }

  @Override
  public void addToData() {
    DataServer.INSTANCE.addBoxToData(getCollisionBox(), getBaseEntity());
  }

//  public void move(int x, int y) {
//    setPosition(new Position(x, y), true);
//    EventBusFactory.getEventBus().post(new PlayersChangedEvent(PlayersChangedEvent.Action.UPDATE, getContainer()));
//  }

  public PlayerState getPlayerState() {
    return new PlayerState(position, health);
  }
}