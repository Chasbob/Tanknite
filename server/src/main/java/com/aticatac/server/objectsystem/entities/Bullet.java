package com.aticatac.server.objectsystem.entities;

import com.aticatac.server.components.physics.PhysicsResponse;
import com.aticatac.server.components.transform.Position;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.IO.outputs.BulletOutput;
import com.aticatac.server.objectsystem.interfaces.Tickable;
import com.aticatac.server.objectsystem.physics.CallablePhysics;
import com.aticatac.server.objectsystem.physics.CollisionBox;
import org.apache.log4j.Logger;

public class Bullet implements Tickable {
  public final Entity shooter;
  public final Entity entity;
  public final int bearing;
  public final BulletOutput output;
  private final Logger logger;
  public Position position;
  private CollisionBox box;

  public Bullet(final Entity shooter, final Position position, final int bearing) {
    this.logger = Logger.getLogger(getClass());
    this.logger.info(shooter.toString() + position.toString() + bearing);
    this.shooter = shooter;
    this.entity = Entity.bullet;
    this.position = position;
    this.bearing = bearing;
    this.output = new BulletOutput();
    this.box = new CollisionBox(this.position, Entity.EntityType.BULLET);
  }

  @Override
  public BulletOutput tick() {
    this.logger.trace("Tick.");
    output.reset();
    try {
      output.setHit(move());
    } catch (Exception e) {
      this.logger.error(e);
    }
    return output;
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
}
