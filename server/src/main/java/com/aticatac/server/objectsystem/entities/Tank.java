package com.aticatac.server.objectsystem.entities;

import com.aticatac.common.model.Vector;
import com.aticatac.common.objectsystem.Container;
import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.bus.EventBusFactory;
import com.aticatac.server.bus.event.PlayersChangedEvent;
import com.aticatac.server.bus.service.PlayerOutputService;
import com.aticatac.server.components.ai.PlayerState;
import com.aticatac.server.components.physics.PhysicsResponse;
import com.aticatac.server.components.transform.Position;
import com.aticatac.server.objectsystem.DataServer;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.IO.inputs.PlayerInput;
import com.aticatac.server.objectsystem.interfaces.Collidable;
import com.aticatac.server.objectsystem.interfaces.DependantTickable;
import com.aticatac.server.objectsystem.interfaces.Hurtable;
import com.aticatac.server.objectsystem.physics.CollisionBox;
import com.aticatac.server.objectsystem.physics.Physics;
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
  protected CollisionBox box;
  protected int health;
  protected int maxHealth;
  protected int maxAmmo;
  protected int ammo;

  //todo add in a parameter boolean which is ai true or false
  //TODO add in the parameter changes everywhere
  public Tank(String name, Position p, int health, int ammo) {
    super(name, EntityType.TANK, p);
//    entity = new Entity(name, EntityType.TANK);
    frames = new ConcurrentLinkedQueue<>();
    input = new PlayerInput();
//    position = p;
    logger = Logger.getLogger(getClass());
    this.box = new CollisionBox(position, EntityType.TANK);
    this.maxHealth = 100;
    this.health = health;
    this.ammo = ammo;
    this.maxAmmo = 30;
    this.outputService = new PlayerOutputService(getBaseEntity());
    physics = new Physics(position, type, name);
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
          if(health>10) {
            move(result.angle());
          }
        } catch (Exception e) {
          this.logger.error(e);
          this.logger.error("Error while moving.");
        }
      }
      if (input.shoot) {
        this.logger.info("shoot");
        if(!(ammo ==0 || health == 0)) {
          setAmmo(ammo-1);
          outputService.addBullet(new Bullet(entity, position, input.bearing, 10));
          DataServer.INSTANCE.addBoxToData(new CollisionBox(entity.getPosition(), EntityType.TANK.radius), entity);
//        this.getComponent(TurretController.class).shoot(input.bearing);
        }
      }
//      output.setTurretOutput(this.getComponent(TurretController.class).tick());
    }
  }

  public void addFrame(final PlayerInput frame) {
    frames.add(frame);
  }

  public void addAndConsume(final PlayerInput frame) {
    frames.add(frame);
    tick();
  }

  public Container getContainer() {
    return new Container(position.getX(), position.getY(), 0, health, ammo, entity.name, EntityType.TANK);
  }

  public void move(int bearing) {
//    CallablePhysics physics = new CallablePhysics(position, entity, entity.name, bearing);
    PhysicsResponse physicsData = physics.move(bearing, position);
    this.logger.trace(physicsData.entity);
    switch (physicsData.entity.type) {
      case TANK:
        if (physicsData.entity.equals(entity)) {
          updateCollisionBox(physicsData.position);
          break;
        }
      case OUTOFBOUNDS:
      case WALL:
        outputService.onPlayerHit(physicsData.entity, getContainer());
        break;
      default:
        outputService.onPlayerHit(physicsData.entity, getContainer());
        updateCollisionBox(physicsData.position);
    }
//    if (!position.equals(physicsData.getPosition())) {
//      this.logger.trace(physicsData);
//      updateCollisionBox(physicsData.position);
//    }
//    if (physicsData.entity.type != EntityType.NONE) {
//      if (physicsData.entity.type.isPowerUp()) {
//        updateCollisionBox(physicsData.position);
//      }
//      outputService.onPlayerHit(physicsData.entity, getContainer());
//    }
  }

  private void updateCollisionBox(Position newPosition) {
    //remove old box
    DataServer.INSTANCE.removeBoxFromData(box);
    //set new position and box
    setPosition(newPosition);
    //add new box to map
    DataServer.INSTANCE.addBoxToData(box, entity);
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

  @Override
  public CollisionBox getCollisionBox() {
    return box;
  }

  @Override
  public boolean intersects(final Collidable collidable) {
    ArrayList<Position> other = collidable.getCollisionBox().getBox();
    other.addAll(box.getBox());
    return other.size() != (new HashSet<>(other).size());
  }

  @Override
  public void addToData() {
    DataServer.INSTANCE.addBoxToData(getCollisionBox(), entity);
  }

  public PlayerState getPlayerState() {
    return new PlayerState(position, health);
  }
}