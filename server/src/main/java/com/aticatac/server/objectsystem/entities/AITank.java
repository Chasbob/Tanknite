package com.aticatac.server.objectsystem.entities;

import com.aticatac.common.model.Command;
import com.aticatac.common.objectsystem.Container;
import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.bus.EventBusFactory;
import com.aticatac.server.bus.listener.AIInputListener;
import com.aticatac.server.bus.service.PlayerOutputService;
import com.aticatac.server.components.ai.AI;
import com.aticatac.server.components.ai.AIInput;
import com.aticatac.server.components.ai.Decision;
import com.aticatac.server.components.ai.PlayerState;
import com.aticatac.server.components.transform.Position;
import com.aticatac.server.objectsystem.DataServer;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.physics.CollisionBox;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;

@SuppressWarnings("ALL")
public class AITank extends Tank {
  protected final ConcurrentLinkedQueue<AIInput> frames;
  protected final Entity entity;
  protected final Logger logger;
  protected final PlayerOutputService outputService;
  private final AI ai;
  protected Position position;
  protected AIInput input;
  protected CollisionBox box;
  protected int health;
  protected int maxHealth;
  protected int maxAmmo;
  protected int ammo;

  //todo add in a parameter boolean which is ai true or false
  //TODO add in the parameter changes everywhere
  public AITank(String name, Position p, int health, int ammo) {
    super(name, p, health, ammo);
    entity = new Entity(name, EntityType.TANK);
    frames = new ConcurrentLinkedQueue<>();
//    input = new AIInput();
    position = p;
    logger = Logger.getLogger(getClass());
    this.box = new CollisionBox(position, EntityType.TANK);
    this.maxHealth = 100;
    this.health = health;
    this.ammo = ammo;
    this.maxAmmo = 30;
    this.outputService = new PlayerOutputService(entity);
    ai = new AI();
    EventBusFactory.getEventBus().register(new AIInputListener(frames, this.position, this.health));
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
//      try {
      AIInput i = new AIInput(new PlayerState(position, health), 30, input.getPlayers(), input.getPowerups());
      Decision decision = ai.getDecision(i);
      if (decision.getCommand() != null && decision.getCommand() != Command.DEFAULT) {
        this.logger.trace(decision.getCommand());
        try {
          move(decision.getCommand().vector.angle());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
//      } catch (Exception e) {
//        this.logger.error(e);
//        this.logger.error("Error while moving.");
//      }
    }
//      output.setTurretOutput(this.getComponent(TurretController.class).tick());
  }

  public Container getContainer() {
    return new Container(position.getX(), position.getY(), 0, 100, 30, entity.name, EntityType.TANK);
  }
//  public void move(int bearing) {
//    CallablePhysics physics = new CallablePhysics(position, entity, entity.name, bearing);
//    PhysicsResponse physicsData = physics.call();
////    if (!position.equals(physicsData.getPosition())) {
////      this.logger.trace(physicsData);
////      updateCollisionBox(physicsData.position);
////    }
//    if (physicsData.entity.type == EntityType.NONE) {
//      this.logger.trace(physicsData);
//      updateCollisionBox(physicsData.position);
//    } else if (physicsData.entity.type.isPowerUp()) {
//      updateCollisionBox(physicsData.position);
//    }
//    outputService.onPlayerHit(physicsData.entity, getContainer());
//  }

  private void updateCollisionBox(Position newPosition) {
    //remove old box
    DataServer.INSTANCE.removeBoxFromData(box);
    //set new position and box
    setPosition(newPosition);
    //add new box to map
    DataServer.INSTANCE.addBoxToData(box, entity);
  }
}
