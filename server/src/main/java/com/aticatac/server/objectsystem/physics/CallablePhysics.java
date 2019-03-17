package com.aticatac.server.objectsystem.physics;

import com.aticatac.common.model.Vector;
import com.aticatac.server.components.physics.PhysicsResponse;
import com.aticatac.server.components.transform.Position;
import com.aticatac.server.objectsystem.DataServer;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.test.GameMode;
import java.util.concurrent.Callable;

/**
 * The type Callable physics.
 */
public class CallablePhysics implements Callable<PhysicsResponse> {
  /**
   * The gravity acting for all objects
   */
  private static int gravity = 10;
  /**
   * The D.
   */
  private final DataServer d;
  private final Entity ignore;
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
  private Entity entity;
  private String name;
  private int rotation;

  /**
   * Instantiates a new Callable physics.
   *
   * @param p        the p
   * @param entity   the entity
   * @param name     the name
   * @param rotation the rotation
   */
  public CallablePhysics(Position p, Entity entity, String name, int rotation) {
    this(p, entity, name, rotation, Entity.bullet);
  }

  public CallablePhysics(Position p, Entity entity, String name, int rotation, Entity ignore) {
    position = p;
    this.entity = entity;
    this.name = name;
    this.rotation = rotation;
    d = DataServer.INSTANCE;
    this.velocity = entity.type.velocity;
    this.ignore = ignore;
  }

  /**
   * BulletController move position.
   *
   * @param rotation the bearing
   *
   * @return the position
   */
  public PhysicsResponse move(int rotation) {
    int xCoord = position.getX();
    int yCoord = position.getY();
    //converting the dt from nanoseconds to seconds
    int dt = 1;
    //Distance it moves is the change in time * the above velocity
    int distance = dt * velocity;
    //distance travelled in x direction is cos theta * distance
    double xr = Math.cos(Math.toRadians(rotation));
    double distanceX = distance * -xr;
    //distance travelled in y direction is sin theta * distance
    double yr = Math.sin(Math.toRadians(rotation));
    double distanceY = distance * -yr;
    //then add those to the original x and y
    double newX = xCoord + distanceX;
    double newY = yCoord + distanceY;
    Position newPosition = new Position((int) newX, (int) newY);
    //returning a collision type and also the position
    return collision(newPosition, position);
  }

  /**
   * Collision method to check if objects have collided
   *
   * @param newPosition The previous position of the tank
   * @param oldPosition The new calculates position of the tank
   *
   * @return An array of the collision type and the new position
   */
  private PhysicsResponse collision(Position newPosition, Position oldPosition) {
//    if (entity.type == Entity.EntityType.TANK) {
    //gets the old box position
    //removes the old positions form data server
//      d.removeBoxFromData(box);
    //checks the box collision coords against the occupied
    CollisionBox box = new CollisionBox(newPosition, entity.type);
    for (int i = 0; i < box.getBox().size(); i++) {
      //map of the coordinates that are occupied
      Position position = box.getBox().get(i);
      PhysicsResponse returnPosition = findCollisions(position, oldPosition);
      if (returnPosition.entity.type != Entity.EntityType.NONE) {
        //sets the box positions back to the old ones and puts that back into the data server
//          d.addBoxToData(box.getBox(), new Entity(name, entity.type));
        return returnPosition;
      }
      if (entity.type == Entity.EntityType.BULLET) {
        return returnPosition;
      }
//      }
      //adds the box positions to the server
//      d.addBoxToData(box.getBox(), new Entity(name, Entity.EntityType.TANK));
    }
//    else if (getGameObject().getObjectType() == ObjectType.BULLET) {
//      //sets the new positions in collision box
//      this.getGameObject().getComponent(BulletCollisionBox.class).setCollisionBox(newPosition);
//      //gets new position box
//      ArrayList<Position> box = this.getGameObject().getComponent(BulletCollisionBox.class).getCollisionBox();
//      //checks the box collision coords against the occupied
//      for (int i = 0; i < box.size(); i++) {
//        //map of the coordinates that are occupied
//        ConcurrentHashMap<Position, Entity> occupiedCoordinates = DataServer.INSTANCE.getOccupiedCoordinates();
//        Position position = box.get(i);
//        PhysicsResponse returnPosition = findCollisions(position, oldPosition, occupiedCoordinates);
//        if (returnPosition != null) {
//          //sets the box positions back to the old ones and puts that back into the data server
//          this.getGameObject().getComponent(BulletCollisionBox.class).setCollisionBox(oldPosition);
//          return returnPosition;
//        }
//      }
//    }
    //string for what has been collided with
    //map of the coordinates that are occupied
//    ConcurrentHashMap<Position, Entity> occupiedCoordinates = DataServer.INSTANCE.getOccupiedCoordinates();
    //checks the position of the object against the occupied coordinates
    return findCollisions(newPosition, oldPosition);
    //else:
  }

  private PhysicsResponse findCollisions(Position newPosition, Position oldPosition) {
    if (GameMode.inMap(new Vector(newPosition.getX(), newPosition.getY()))) {
      Entity collisionType;
      if (d.occupied(newPosition)) {
        collisionType = d.getEntity(newPosition);
        //checks the coordinate is not itself
        if (collisionType.equals(entity)) {
          return new PhysicsResponse(newPosition);
        }
        //Returning a collision type and also the position
        return new PhysicsResponse(collisionType, oldPosition);
      }
      return new PhysicsResponse(newPosition);
    } else {
      return new PhysicsResponse(oldPosition);
    }
  }

  @Override
  public PhysicsResponse call() throws Exception {
    return move(rotation);
  }
}