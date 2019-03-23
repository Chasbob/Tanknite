package com.aticatac.server.objectsystem.entities;

import com.aticatac.common.objectsystem.Container;
import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.bus.EventBusFactory;
import com.aticatac.server.bus.event.BulletCollisionEvent;
import com.aticatac.server.bus.event.BulletsChangedEvent;
import com.aticatac.server.bus.service.BulletOutputService;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.interfaces.Tickable;
import com.aticatac.server.objectsystem.physics.Physics;
import com.aticatac.server.transform.Position;
import java.util.Objects;
import org.apache.log4j.Logger;

/**
 * The type Bullet.
 */
public class Bullet extends Entity implements Tickable {
  /**
   * The Shooter.
   */
  public final Entity shooter;
  /**
   * The Entity.
   */
  /**
   * The Output.
   */
  private final Logger logger;
  private final int damage;
  private final BulletOutputService outputService;
  private final Physics physics;
  /**
   * The Bearing.
   */
  public int bearing;
  /**
   * The Position.
   */
  private Position prevPosistion;

  /**
   * Instantiates a new Bullet.
   *
   * @param shooter  the shooter
   * @param position the position
   * @param bearing  the bearing
   * @param damage   the damage
   */
  public Bullet(final Entity shooter, final Position position, final int bearing, final int damage) {
    super(Integer.toString(Objects.hash(shooter, position, bearing, damage)), EntityType.BULLET, position);
    this.damage = damage;
    this.logger = Logger.getLogger(getClass());
    this.logger.info(shooter.toString() + position.toString() + "\t" + bearing);
    this.shooter = shooter;
    this.setPosition(position);
    this.prevPosistion = this.getPosition();
    this.bearing = bearing;
    outputService = new BulletOutputService(this);
    physics = new Physics(this.getPosition(), getType(), getName());
  }

  /**
   * Gets damage.
   *
   * @return the damage
   */
  public int getDamage() {
    return damage;
  }

  @Override
  public void tick() {
    if (!prevPosistion.equals(getPosition())) {
      this.logger.trace(getPosition());
      prevPosistion = getPosition();
    }
    move();
  }

  private void move() {
    var response = physics.move(bearing, getPosition(), false);
    if (!response.getCollisions().contains(Entity.outOfBounds)) {
      for (Entity e :
          response.getCollisions()) {
        if (e.getType() != EntityType.NONE) {
          this.logger.info("Hit: " + e);
        }
        switch (e.getType()) {
          case NONE:
            break;
          case WALL:
          case OUTOFBOUNDS:
            EventBusFactory.getEventBus().post(new BulletCollisionEvent(this, e));
            return;
          case TANK:
            if (!e.getName().equals(shooter.getName())) {
              EventBusFactory.getEventBus().post(new BulletCollisionEvent(this, e));
            }
            break;
          case BULLET:
            break;
          case AMMO_POWERUP:
            break;
          case SPEED_POWERUP:
            break;
          case HEALTH_POWERUP:
            break;
          case DAMAGE_POWERUP:
            break;
        }
      }
      setPosition(response.getPosition(), false);
      EventBusFactory.getEventBus().post(new BulletsChangedEvent(BulletsChangedEvent.Action.UPDATE, getContainer()));
      this.logger.trace(getPosition());
    }
  }

  public Container getContainer() {
    return new Container(getPosition().getX(), getPosition().getY(), bearing, 0, 0, getName(), EntityType.BULLET);
  }

  @Override
  public String toString() {
    return "Bullet{" +
        "shooter=" + shooter +
        ", damage=" + damage +
        '}';
  }
}
