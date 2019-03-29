package com.aticatac.client.server.objectsystem.entities;

import com.aticatac.client.server.Position;
import com.aticatac.client.server.ai.AI;
import com.aticatac.client.server.ai.AIInput;
import com.aticatac.client.server.ai.Decision;
import com.aticatac.client.server.ai.PlayerState;
import com.aticatac.client.server.bus.listener.AIInputListener;
import com.aticatac.client.server.objectsystem.Entity;
import com.aticatac.client.server.objectsystem.physics.CollisionBox;
import com.aticatac.common.model.Command;
import com.aticatac.common.objectsystem.EntityType;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;

import static com.aticatac.client.bus.EventBusFactory.serverEventBus;

/**
 * The type Ai tank.
 */
@SuppressWarnings("ALL")
public class AITank extends Tank {
  /**
   * The Frames.
   */
  protected final ConcurrentLinkedQueue<AIInput> frames;
  /**
   * The Entity.
   */
  protected final Entity entity;
  /**
   * The Logger.
   */
  protected final Logger logger;
  private final AI ai;
  /**
   * The Position.
   */
  protected Position position;
  /**
   * The Input.
   */
  protected AIInput input;
  /**
   * The Box.
   */
  protected CollisionBox box;
  /**
   * The Health.
   */
  protected int health;
  /**
   * The Max health.
   */
  protected int maxHealth;
  /**
   * The Max ammo.
   */
  protected int maxAmmo;
  /**
   * The Ammo.
   */
  protected int ammo;

  /**
   * Instantiates a new Ai tank.
   *
   * @param name     the name
   * @param p        the p
   * @param health   the health
   * @param ammo     the ammo
   * @param playerNo the player no
   */
//todo add in a parameter boolean which is ai true or false
  //TODO add in the parameter changes everywhere
  public AITank(String name, Position p, int health, int ammo, int playerNo) {
    super(name, p, health, ammo, playerNo);
    entity = new Entity(name, EntityType.TANK);
    frames = new ConcurrentLinkedQueue<>();
    logger = Logger.getLogger(getClass());
    ai = new AI();
    serverEventBus.register(new AIInputListener(frames));
  }

  @Override
  public void tick() {
    logger.trace("tick");
    setFrozen(getFrozen() - 1);
    setDamageIncrease(getDamageIncrease() - 1);
    setSpeedIncrease(getSpeedIncrease() - 1);
    setDeathCountdown(getDeathCountdown() - 1);
    if(getDeathCountdown() == 0){
      hit(10,false);
    }
    if (frames.size() > 5) {
      frames.clear();
    }
    if (!frames.isEmpty()) {
      input = frames.poll();
      AIInput i = new AIInput(new PlayerState(getPosition(), getHealth()), getAmmo(), getFreezeBullets(), getBulletSprays(), input.getPlayers(), input.getPowerups());
      Decision decision = ai.getDecision(i);
      setRotation(decision.getAngle());
      try {
        if (getHealth() > 10 && decision.getCommand() != null && decision.getCommand() != Command.DEFAULT && getFrozen() < 0) {
          move(decision.getCommand().vector);
        }
      } catch (Exception e) {
        this.logger.error(e);
        this.logger.error("Error while moving.");
      }
      if (decision.getShoot() == Decision.ShootType.NORMAL) {
        this.logger.trace("shoot");
        if (!(getAmmo() == 0 || getHealth() == 0) && getFramesToShoot() < 0 && getFrozen() < 0) {
          setAmmo(getAmmo() - 1);
          if (getDamageIncrease() > 0) {
            addBullet(new Bullet(this, turretCalculation(getPosition().copy(), decision.getAngle()), decision.getAngle(), 20, false));
          } else {
            addBullet(new Bullet(this, turretCalculation(getPosition().copy(), decision.getAngle()), decision.getAngle(), 10, false));
          }
          setFramesToShoot(30);
        }
      }
      if (decision.getShoot() == Decision.ShootType.FREEZE) {
        this.logger.trace("freeze bullet");
        if (getFreezeBullets() > 0 && getFrozen() < 0) {
          addBullet(new Bullet(this, turretCalculation(getPosition().copy(), decision.getAngle()), decision.getAngle(), 0, true));
          // TODO: Make tank turn blue while it is frozen
          setFreezeBullets(getFreezeBullets() - 1);
        }
      }
      if (decision.getShoot() == Decision.ShootType.SPRAY) {
        this.logger.trace("bullet spray");
        if (getBulletSprays() > 0 && getFrozen() < 0) {
          for (int j = 0; j < 375; j += 15) {
            addBullet(new Bullet(this, getPosition().copy(), decision.getAngle() + j, 5, false));
          }
          setBulletSprays(getBulletSprays() - 1);
        }
      }
    }
    if (getFramesToShoot() == 1) {
      this.logger.trace("Ready to fire!");
    }
    setFramesToShoot(getFramesToShoot() - 1);
  }
}
