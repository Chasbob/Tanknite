package com.aticatac.server.objectsystem.physics;

import com.aticatac.common.model.Command;
import com.aticatac.common.model.Vector;
import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.components.physics.PhysicsResponse;
import com.aticatac.server.components.transform.Position;
import com.aticatac.server.objectsystem.DataServer;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.test.GameMode;

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
  private int velocity = 5;
  private Position position;
  private int rotation;

  public Physics(final Position position, final EntityType type, final String name) {
    this.position = position;
    this.entity = new Entity(name, type, position);
    d = DataServer.INSTANCE;
  }

  public PhysicsResponse move(int rotation, final Position position) {
    this.position = position;
    int xCoord = this.position.getX();
    int yCoord = this.position.getY();
    int dt = 1;
    int distance = dt * velocity;
    double xr = Math.cos(Math.toRadians(rotation));
    double distanceX = distance * -xr;
    double yr = Math.sin(Math.toRadians(rotation));
    double distanceY = distance * -yr;
    double newX = xCoord + distanceX;
    double newY = yCoord + distanceY;
    Position newPosition = new Position((int) newX, (int) newY);
    return collision(newPosition, this.position);
  }

  public PhysicsResponse inputCommand(Command command) {
    if (command.isMovement()) {
      return move(command.vector.angle(), position);
    } else {
      return new PhysicsResponse(Entity.empty, position);
    }
  }

  private PhysicsResponse collision(Position newPosition, Position oldPosition) {
    CollisionBox box = new CollisionBox(newPosition, entity.type);
    for (int i = 0; i < box.getBox().size(); i++) {
      Position position = box.getBox().get(i);
      PhysicsResponse returnPosition = findCollisions(position, oldPosition);
      if (returnPosition.entity.type != EntityType.NONE) {
        return returnPosition;
      }
    }
    return findCollisions(newPosition, oldPosition);
  }

  private PhysicsResponse findCollisions(Position newPosition, Position oldPosition) {
    if (GameMode.inMap(new Vector(newPosition.getX(), newPosition.getY()))) {
      Entity collisionType;
      if (d.occupied(newPosition)) {
        collisionType = d.getEntity(newPosition);
        if (collisionType.equals(entity)) {
          return new PhysicsResponse(Entity.empty, newPosition);
        } else {
          return new PhysicsResponse(collisionType, newPosition);
        }
      } else {
        return new PhysicsResponse(Entity.empty, newPosition);
      }
    } else {
      return new PhysicsResponse(Entity.outOfBounds, oldPosition);
    }
  }
}
