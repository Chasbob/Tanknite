package com.aticatac.server.objectsystem.physics;

import com.aticatac.common.model.Vector;
import com.aticatac.common.model.VectorF;
import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.Position;
import com.aticatac.server.game.GameMode;
import com.aticatac.server.objectsystem.DataServer;
import com.aticatac.server.objectsystem.Entity;
import java.util.HashSet;

@SuppressWarnings("ALL")
public class Physics {
  /**
   * The gravity acting for all objects
   */
  private static int gravity = 1;
  /**
   * The D.
   */
  private final DataServer d;
  private final Entity entity;
  /**
   * The mass of this object
   */
  private int objectMass = 1;
  /**
   * The thrust for this tank
   */
  private int thrust = 1;
  /**
   * The acceleration for this tank
   */
  private int acceleration;
  /**
   * The velocity for this tank
   */
  private int velocity;
  private Position position;
  private int rotation;

  public Physics(final Position position, final EntityType type, final String name) {
    this.position = position;
    this.entity = new Entity(name, type);
    d = DataServer.INSTANCE;
  }

  public PhysicsResponse move(int rotation, final Position position, boolean speedPowerUp) {
    this.position = position;
    int xCoord = this.position.getX();
    int yCoord = this.position.getY();


    int dt = 1;
    setAcceleration("positive", speedPowerUp);
    velocity += acceleration*dt;

    int distance = dt * velocity;
    double xr = Math.cos(Math.toRadians(rotation));
    double distanceX = distance * -xr;
    double yr = Math.sin(Math.toRadians(rotation));
    double distanceY = distance * -yr;
    double newX = xCoord + distanceX;
    double newY = yCoord + distanceY;
    Position newPosition = new Position((int) newX, (int) newY);
    double dx = (newX - xCoord);
    double dy = (newY - yCoord);
    double ddx = Math.ceil(dx / entity.getType().radius);
    double ddy = Math.ceil(dy / entity.getType().radius);
//    System.out.println(ddx + ", " + ddy);
    double x = 0;
    double y = 0;
    VectorF oV = new VectorF(position.getX(), position.getY());
    VectorF v = new VectorF((float) newX, (float) newY);
    VectorF scl = v.cpy().sub(oV);
    HashSet<Entity> collisions = new HashSet<>();
    for (int i = 0; i < 10; i++) {
      oV = v.cpy().add(scl.cpy().scl(i * 0.1f));
      final Position newPosition1 = new Position((int) oV.x, (int) oV.y);
      collisions.addAll(collision2(newPosition1));
    }
    return new PhysicsResponse(collisions, newPosition);
  }


  private HashSet<Entity> collision2(Position newPosition) {
    HashSet<Entity> collisions = new HashSet<>();
    CollisionBox box = new CollisionBox(newPosition, entity.getType());
    for (Position p :
        box.getBox()) {
      Entity collision = findCollision(p);
      if (collision.getType() != EntityType.NONE) {
        collisions.add(collision);
      }
    }
//    for (int i = 0; i < box.getBox().size(); i++) {
//      Position position = box.getBox().get(i);
//      Entity collision = findCollision(position);
//      if (collision.getType() != EntityType.NONE) {
//        collisions.add(collision);
//      }
//    }
    return collisions;
  }

  private Entity findCollision(Position p) {
    if (GameMode.inMap(new Vector(p.getX(), p.getY()))) {
      return d.INSTANCE.getEntity(p);
    } else {
      return Entity.outOfBounds;
    }
  }
  private void setAcceleration(String sign, boolean speedPowerUp) {
    if(sign.equals("positive")) {
      if(speedPowerUp){
        acceleration = (gravity * ((0 + objectMass) + thrust)) / objectMass;
      }else {
        if (velocity < entity.getType().velocity) {
          acceleration = (gravity * ((0 + objectMass) + thrust)) / objectMass;
        } else if (velocity >= entity.getType().velocity) {
          velocity = entity.getType().velocity;
          acceleration = 0;
        }
      }
    }else{
      acceleration = -(gravity *((0 + objectMass) + thrust))/objectMass;
    }
  }


}
