package com.aticatac.server.objectsystem.physics;

import com.aticatac.common.model.Vector;
import com.aticatac.common.model.VectorF;
import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.transform.Position;
import com.aticatac.server.objectsystem.DataServer;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.test.GameMode;
import java.util.HashSet;

@SuppressWarnings("ALL")
public class Physics {
  /**
   * The gravity acting for all objects
   */
  private static int gravity = 10;
  /**
   * The D.
   */
  private final DataServer d;
  private final Entity entity;
  /**
   * The mass of this object
   */
  private int objectMass = 10;
  /**
   * The thrust for this tank
   */
  private int thrust = 10;
  /**
   * The acceleration for this tank
   */
  private int acceleration;
  /**
   * The velocity for this tank
   */
//  private int velocity = 5;
  private Position position;
  private int rotation;

  public Physics(final Position position, final EntityType type, final String name) {
    this.position = position;
    this.entity = new Entity(name, type);
    d = DataServer.INSTANCE;
  }

  public PhysicsResponse move(int rotation, final Position position) {
    this.position = position;
    int xCoord = this.position.getX();
    int yCoord = this.position.getY();
    int dt = 1;
    int distance = dt * entity.type.velocity;
    double xr = Math.cos(Math.toRadians(rotation));
    double distanceX = distance * -xr;
    double yr = Math.sin(Math.toRadians(rotation));
    double distanceY = distance * -yr;
    double newX = xCoord + distanceX;
    double newY = yCoord + distanceY;
    Position newPosition = new Position((int) newX, (int) newY);
    double dx = (newX - xCoord);
    double dy = (newY - yCoord);
    double ddx = Math.ceil(dx / entity.type.radius);
    double ddy = Math.ceil(dy / entity.type.radius);
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
    CollisionBox box = new CollisionBox(newPosition, entity.type);
    for (int i = 0; i < box.getBox().size(); i++) {
      Position position = box.getBox().get(i);
      Entity collision = findCollision(position);
      if (collision.type != EntityType.NONE) {
        collisions.add(collision);
      }
    }
    return collisions;
  }

  private Entity findCollision(Position p) {
    if (GameMode.inMap(new Vector(p.getX(), p.getY()))) {
      return d.INSTANCE.getEntity(p);
    } else {
      return Entity.outOfBounds;
    }
  }

}
