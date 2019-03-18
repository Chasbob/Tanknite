package com.aticatac.server.objectsystem.entities;

import com.aticatac.common.model.Vector;
import com.aticatac.common.objectsystem.Container;
import com.aticatac.common.objectsystem.ObjectType;
import com.aticatac.server.bus.EventBusFactory;
import com.aticatac.server.bus.service.PlayerOutputService;
import com.aticatac.server.components.physics.PhysicsResponse;
import com.aticatac.server.components.transform.Position;
import com.aticatac.server.objectsystem.DataServer;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.IO.inputs.PlayerInput;
import com.aticatac.server.objectsystem.interfaces.Collidable;
import com.aticatac.server.objectsystem.interfaces.DependantTickable;
import com.aticatac.server.objectsystem.interfaces.Hurtable;
import com.aticatac.server.objectsystem.physics.CallablePhysics;
import com.aticatac.server.objectsystem.physics.CollisionBox;
import com.google.common.eventbus.EventBus;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;

public class Tank implements DependantTickable<PlayerInput>, Hurtable {
  private final Queue<PlayerInput> frames;
  private final Entity entity;
  private final Logger logger;
  private final PlayerOutputService outputService;
  //  private final String name;
  private Position position;
  private PlayerInput input;
  private CollisionBox box;
  private int health;
  private int maxHealth;
  private int maxAmmo;
  private int ammo;

  //todo add in a parameter boolean which is ai true or false
  //TODO add in the parameter changes everywhere
  public Tank(String name, Position p, int health, int ammo) {
    entity = new Entity(name, Entity.EntityType.TANK);
    frames = new ConcurrentLinkedQueue<>();
    input = new PlayerInput();
    position = p;
    logger = Logger.getLogger(getClass());
    this.box = new CollisionBox(position, Entity.EntityType.TANK);
    this.maxHealth = 100;
    this.health = health;
    this.ammo = ammo;
    this.maxAmmo = 30;
    this.outputService = new PlayerOutputService(entity);
  }

  public Position getPosition() {
    return position;
  }

  private void setPosition(Position p) {
    this.position = p;
    box.setPosition(p);
  }

  public Entity getEntity() {
    return entity;
  }

  public String getName() {
    return entity.name;
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
          move(result.angle());
        } catch (Exception e) {
          this.logger.error(e);
          this.logger.error("Error while moving.");
        }
      }
      if (input.shoot) {
        this.logger.info("shoot");
        outputService.addBullet(new Bullet(entity, position, input.bearing, 10));
//        this.getComponent(TurretController.class).shoot(input.bearing);
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
    return new Container(position.getX(), position.getY(), 0, 100, 30, entity.name, ObjectType.TANK);
  }

  public void move(int bearing) throws Exception {
    CallablePhysics physics = new CallablePhysics(position, entity, entity.name, bearing);
    PhysicsResponse physicsData = physics.call();
    if (!position.equals(physicsData.getPosition())) {
      this.logger.trace(physicsData);
      updateCollisionBox(physicsData.position);
    }
    if (physicsData.entity.type != Entity.EntityType.NONE) {
      outputService.onPlayerHit(physicsData.entity,getContainer());

    }
  }

  private void updateCollisionBox(Position newPosition) {
    //remove old box
    DataServer.INSTANCE.removeBoxFromData(box);
    //set new position and box
    setPosition(newPosition);
    //add new box to map
    DataServer.INSTANCE.addBoxToData(box, entity);
  }

  @Override
  public int getHealth() {
    return 0;
  }

  @Override
  public int hit(final int damage) {
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
}