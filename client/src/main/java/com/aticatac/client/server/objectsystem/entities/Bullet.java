package com.aticatac.client.server.objectsystem.entities;

import com.aticatac.client.server.Position;
import com.aticatac.client.server.bus.event.BulletCollisionEvent;
import com.aticatac.client.server.bus.event.BulletsChangedEvent;
import com.aticatac.client.server.objectsystem.Entity;
import com.aticatac.client.server.objectsystem.interfaces.Tickable;
import com.aticatac.client.server.objectsystem.physics.Physics;
import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.common.objectsystem.containers.Container;
import java.util.Objects;

import static com.aticatac.client.bus.EventBusFactory.serverEventBus;

/**
 * The type Bullet.
 */
public class Bullet extends Entity implements Tickable {
  private final Entity shooter;
  /**
   * The Entity.
   */
  private final int damage;
  private final Physics physics;
  private final boolean freezeBullet;
  private int bearing;
  private Position prevPosistion;

  /**
   * Instantiates a new Bullet.
   *
   * @param shooter      the shooter
   * @param position     the position
   * @param bearing      the bearing
   * @param damage       the damage
   * @param freezeBullet the freeze bullet
   */
  public Bullet(final Entity shooter, final Position position, final int bearing, final int damage, boolean freezeBullet) {
    super(Integer.toString(Objects.hash(shooter, position, bearing, damage)), EntityType.BULLET, position);
    this.damage = damage;
    this.logger.info(shooter.toString() + position.toString() + "\t" + bearing);
    this.shooter = shooter;
    this.setPosition(position);
    this.setPrevPosistion(this.getPosition());
    this.setBearing(bearing);
    this.freezeBullet = freezeBullet;
    // TODO: Make freeze bulet a different colour to regular bullet
    physics = new Physics(getType(), getName());
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
    if (!getPrevPosistion().equals(getPosition())) {
      this.logger.trace(getPosition());
      setPrevPosistion(getPosition());
    }
    move();
  }

  private void move() {
    var response = getPhysics().move(getBearing(), getPosition(), 0);
    if (!response.getCollisions().contains(Entity.outOfBounds)) {
      for (Entity e :
          response.getCollisions()) {
        if (e.getType() != EntityType.NONE) {
        }
        switch (e.getType()) {
          case NONE:
            break;
          case WALL:
          case OUTOFBOUNDS:
            serverEventBus.post(new BulletCollisionEvent(this, e));
            return;
          case TANK:
            if (!e.getName().equals(getShooter().getName())) {
              serverEventBus.post(new BulletCollisionEvent(this, e));
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
          case BULLETSPRAY_POWERUP:
            break;
          case FREEZEBULLET_POWERUP:
            break;
        }
      }
      setPosition(response.getPosition(), false);
      serverEventBus.post(new BulletsChangedEvent(BulletsChangedEvent.Action.UPDATE, getContainer()));
      this.logger.trace(getPosition());
    }
  }

  /**
   * On bullet collision.
   *
   * @param e the e
   */
  public void onBulletCollision(Entity e) {
    serverEventBus.post(new BulletCollisionEvent(this, e));
  }

  public Container getContainer() {
    return new Container(getPosition().getX(), getPosition().getY(), getBearing(), getName(), EntityType.BULLET);
  }

  @Override
  public String toString() {
    return "Bullet{" +
        "shooter=" + getShooter() +
        ", damage=" + getDamage() +
        '}';
  }

  /**
   * The Shooter.
   *
   * @return the shooter
   */
  public Entity getShooter() {
    return shooter;
  }

  /**
   * Gets physics.
   *
   * @return the physics
   */
  public Physics getPhysics() {
    return physics;
  }

  /**
   * The Bearing.
   *
   * @return the bearing
   */
  public int getBearing() {
    return bearing;
  }

  /**
   * Sets bearing.
   *
   * @param bearing the bearing
   */
  public void setBearing(int bearing) {
    this.bearing = bearing;
  }

  /**
   * The Position.
   *
   * @return the prev posistion
   */
  public Position getPrevPosistion() {
    return prevPosistion;
  }

  /**
   * Sets prev posistion.
   *
   * @param prevPosistion the prev posistion
   */
  public void setPrevPosistion(Position prevPosistion) {
    this.prevPosistion = prevPosistion;
  }

  /**
   * Gets freeze bullet.
   *
   * @return the freeze bullet
   */
  public boolean getFreezeBullet() {
    return freezeBullet;
  }
}
