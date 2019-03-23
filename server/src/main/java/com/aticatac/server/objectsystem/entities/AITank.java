package com.aticatac.server.objectsystem.entities;

import com.aticatac.common.model.Command;
import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.Position;
import com.aticatac.server.ai.AI;
import com.aticatac.server.ai.AIInput;
import com.aticatac.server.ai.Decision;
import com.aticatac.server.ai.PlayerState;
import com.aticatac.server.bus.EventBusFactory;
import com.aticatac.server.bus.listener.AIInputListener;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.aticatac.server.objectsystem.DataServer;
import com.aticatac.server.objectsystem.physics.CollisionBox;
import org.apache.log4j.Logger;

@SuppressWarnings("ALL")
public class AITank extends Tank {
  protected final ConcurrentLinkedQueue<AIInput> frames;
  protected final Logger logger;
  private final AI ai;
  protected AIInput input;

  public AITank(String name, Position p, int health, int ammo) {
    super(name, p, health, ammo);
    frames = new ConcurrentLinkedQueue<>();
    setPosition(p);
    logger = Logger.getLogger(getClass());
    this.setMaxHealth(100);
    this.setHealth(health);
    this.setAmmo(ammo);
    this.setMaxAmmo(30);
    ai = new AI();
    EventBusFactory.getEventBus().register(new AIInputListener(frames));
  }

  public void tick() {
    logger.trace("tick");
    if (getFrames().size() > 5) {
      getFrames().clear();
    }
    AIInput i = new AIInput(new PlayerState(getPosition(), getHealth()), 30, input.getPlayers(), input.getPowerups());
    Decision decision = ai.getDecision(i);
    if (!getFrames().isEmpty()) {
      setInput(getFrames().poll());
      setRotation(getInput().getBearing());
      try {
        if (getHealth() > 10 && decision.getCommand() != null && decision.getCommand() != Command.DEFAULT) {
          move(decision.getCommand().vector, isSpeedIncrease());
        }
      } catch (Exception e) {
        this.logger.error(e);
        this.logger.error("Error while moving.");
      }
    }
    if (decision.getShoot()) {
      this.logger.trace("shoot");
      if (!(getAmmo() == 0 || getHealth() == 0) && getFramesToShoot() < 0) {
        setAmmo(getAmmo() - 1);
        if (isDamageIncrease()) {
          addBullet(new Bullet(this, getPosition().copy(), decision.getAngle(), 20));
        } else {
          addBullet(new Bullet(this, getPosition().copy(), decision.getAngle(), 10));
        }
        setFramesToShoot(60);
      }
    }
    if (getFramesToShoot() == 1) {
      this.logger.trace("Ready to fire!");
    }
    setFramesToShoot(getFramesToShoot() - 1);
  }
}
