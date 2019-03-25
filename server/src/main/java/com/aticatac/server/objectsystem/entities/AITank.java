package com.aticatac.server.objectsystem.entities;

import com.aticatac.common.model.Command;
import com.aticatac.server.Position;
import com.aticatac.server.ai.AI;
import com.aticatac.server.ai.AIInput;
import com.aticatac.server.ai.Decision;
import com.aticatac.server.ai.PlayerState;
import com.aticatac.server.bus.listener.AIInputListener;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;

import static com.aticatac.server.bus.EventBusFactory.eventBus;

@SuppressWarnings("ALL")
public class AITank extends Tank {
  protected final ConcurrentLinkedQueue<AIInput> frames;
  protected final Logger logger;
  private final AI ai;
  protected AIInput input;

  public AITank(String name, Position p, int health, int ammo) {
    super(name, p, health, ammo);
    frames = new ConcurrentLinkedQueue<>();
    logger = Logger.getLogger(getClass());
    ai = new AI();
    eventBus.register(new AIInputListener(frames));
  }

  @Override
  public void tick() {
    logger.trace("tick");
    if (frames.size() > 5) {
      frames.clear();
    }
    if (!frames.isEmpty()) {
      input = frames.poll();
      AIInput i = new AIInput(new PlayerState(getPosition(), getHealth()), 30, input.getPlayers(), input.getPowerups());
      Decision decision = ai.getDecision(i);
      setRotation(decision.getAngle());
      try {
        if (getHealth() > 10 && decision.getCommand() != null && decision.getCommand() != Command.DEFAULT) {
          move(decision.getCommand().vector, isSpeedIncrease());
        }
      } catch (Exception e) {
        this.logger.error(e);
        this.logger.error("Error while moving.");
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
          setFramesToShoot(30);
        }
      }
    }
    if (getFramesToShoot() == 1) {
      this.logger.trace("Ready to fire!");
    }
    setFramesToShoot(getFramesToShoot() - 1);
  }
}
