package com.aticatac.server.objectsystem.entities;

import com.aticatac.server.bus.service.BulletOutputService;
import com.aticatac.server.components.physics.PhysicsResponse;
import com.aticatac.server.components.transform.Position;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.interfaces.Tickable;
import com.aticatac.server.objectsystem.physics.CallablePhysics;
import com.aticatac.server.objectsystem.physics.CollisionBox;
import java.util.Objects;
import org.apache.log4j.Logger;

/**
 * The type Bullet.
 */
public class Bullet implements Tickable {
  /**
   * The Shooter.
   */
  public final Entity shooter;
  /**
   * The Entity.
   */
  public final Entity entity;
  /**
   * The Bearing.
   */
  public final int bearing;
  /**
   * The Output.
   */
  private final Logger logger;
  private final int damage;
  private final BulletOutputService outputService;
  /**
   * The Position.
   */
  public Position position;
  private Position prevPosistion;
  private CollisionBox box;

  /**
   * Instantiates a new Bullet.
   *
   * @param shooter  the shooter
   * @param position the position
   * @param bearing  the bearing
   * @param damage   the damage
   */
  public Bullet(final Entity shooter, final Position position, final int bearing, final int damage) {
    this.damage = damage;
    this.logger = Logger.getLogger(getClass());
    this.logger.info(shooter.toString() + position.toString() + bearing);
    this.shooter = shooter;
    this.entity = Entity.bullet;
    this.position = position;
    this.prevPosistion = this.position;
    this.bearing = bearing;
    this.box = new CollisionBox(this.position, Entity.EntityType.BULLET);
    outputService = new BulletOutputService(this);
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
    if (!prevPosistion.equals(position)) {
      this.logger.info(position);
      prevPosistion = position;
    }
    try {
      Entity hit = move();
      if (hit.type != Entity.EntityType.NONE) {
        outputService.onBulletCollision(hit);
      }
    } catch (Exception e) {
      this.logger.error(e);
    }
  }

  private Entity move() throws Exception {
    this.logger.trace("Move.");
    CallablePhysics physics = new CallablePhysics(position, entity, entity.name, bearing);
    PhysicsResponse physicsData = physics.call();
    if (physicsData.entity.type == Entity.EntityType.NONE || physicsData.entity.name.equals(shooter.name)) {
      updateCollisionBox(physicsData.position);
      return Entity.empty;
    } else {
      this.logger.info(physicsData);
      return physicsData.entity;
    }
  }

  private void setPosition(Position p) {
    this.position = p;
    box.setPosition(p);
  }

  private void updateCollisionBox(Position newPosition) {
    //remove old box
//    DataServer.INSTANCE.removeBoxFromData(box);
    //set new position and box
    setPosition(newPosition);
    //add new box to map
//    DataServer.INSTANCE.addBoxToData(box, entity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shooter, entity, bearing, position, box);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Bullet bullet = (Bullet) o;
    return bearing == bullet.bearing &&
        shooter.equals(bullet.shooter) &&
        entity.equals(bullet.entity) &&
        position.equals(bullet.position) &&
        box.equals(bullet.box);
  }
}
