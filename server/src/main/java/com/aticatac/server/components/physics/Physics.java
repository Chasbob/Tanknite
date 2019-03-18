package com.aticatac.server.components.physics;

import com.aticatac.common.objectsystem.ObjectType;
import com.aticatac.server.components.Acceleration;
import com.aticatac.server.components.BulletCollisionBox;
import com.aticatac.server.components.CollisionBox;
import com.aticatac.server.components.Component;
import com.aticatac.server.components.transform.Position;
import com.aticatac.server.components.transform.Transform;
import com.aticatac.server.objectsystem.DataServer;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.GameObject;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Physics component will control the physics for the objects in the game. It will consider the new positions of objects
 * and alter any physics values (e.g. velocity) for the object. The positions of the object are kept in TransformModel
 * and will be altered by the logic, not this component.
 *
 * @author Claire Fletcher
 */
@SuppressWarnings("ALL")
public class Physics extends Component {
  /**
   * The gravity acting for all objects
   */
  private static int gravity = 10;
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

  /**
   * Creates a new Physics with a parent.
   *
   * @param gameObject the game object
   */
  public Physics(GameObject gameObject) {
    super(gameObject);
  }
  /**
   * Checks if the object can move in a direction and returns if it can and the new/current position.
   *
   * @return Position of the object
   */
  /**
   * BulletController move position.
   *
   * @param rotation the bearing
   *
   * @return the position
   */
  public PhysicsResponse move(int rotation) {
    Position position = this.getGameObject().getComponent(Transform.class).getPosition();
    int xCoord = position.getX();
    int yCoord = position.getY();
    //converting the dt from nanoseconds to seconds
    int dt = 1;
    //Distance it moves is the change in time * the above velocity
    int distance = dt * velocity;
    //distance travelled in x direction is cos theta * distance
    int xr = (int) Math.cos(Math.toRadians(rotation));
    int distanceX = distance * -xr;
    //distance travelled in y direction is sin theta * distance
    int yr = (int) Math.sin(Math.toRadians(rotation));
    int distanceY = distance * -yr;
    //then add those to the original x and y
    int newX = xCoord + distanceX;
    int newY = yCoord + distanceY;
    Position newPosition = new Position(newX, newY);
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
    if (getGameObject().getObjectType() == ObjectType.TANK) {
      //gets the old box position
      ArrayList<Position> box = this.getGameObject().getComponent(CollisionBox.class).getCollisionBox();
      //removes the old positions form data server
      this.getGameObject().getComponent(CollisionBox.class).removeBoxFromData(box);
      //sets a new collision box using new position
      this.getGameObject().getComponent(CollisionBox.class).setCollisionBox(newPosition);
      //gets new position box
      box = this.getGameObject().getComponent(CollisionBox.class).getCollisionBox();
      //checks the box collision coords against the occupied
      for (int i = 0; i < box.size(); i++) {
        //map of the coordinates that are occupied
        ConcurrentHashMap<Position, Entity> occupiedCoordinates = DataServer.INSTANCE.getOccupiedCoordinates();
        Position position = box.get(i);
        PhysicsResponse returnPosition = getCollisionArray(position, oldPosition, occupiedCoordinates);
        if (returnPosition != null) {
          //sets the box positions back to the old ones and puts that back into the data server
          this.getGameObject().getComponent(CollisionBox.class).setCollisionBox(oldPosition);
          this.getGameObject().getComponent(CollisionBox.class).addBoxToData(box, this.getGameObject().getName());
          return returnPosition;
        }
      }
      //adds the box positions to the server
      this.getGameObject().getComponent(CollisionBox.class).addBoxToData(box, this.getGameObject().getName());
    } else if (getGameObject().getObjectType() == ObjectType.BULLET) {
      //sets the new positions in collision box
      this.getGameObject().getComponent(BulletCollisionBox.class).setCollisionBox(newPosition);
      //gets new position box
      ArrayList<Position> box = this.getGameObject().getComponent(BulletCollisionBox.class).getCollisionBox();
      //checks the box collision coords against the occupied
      for (int i = 0; i < box.size(); i++) {
        //map of the coordinates that are occupied
        ConcurrentHashMap<Position, Entity> occupiedCoordinates = DataServer.INSTANCE.getOccupiedCoordinates();
        Position position = box.get(i);
        PhysicsResponse returnPosition = getCollisionArray(position, oldPosition, occupiedCoordinates);
        if (returnPosition != null) {
          //sets the box positions back to the old ones and puts that back into the data server
          this.getGameObject().getComponent(BulletCollisionBox.class).setCollisionBox(oldPosition);
          return returnPosition;
        }
      }
    }
    //string for what has been collided with
    Entity collisionType;
    //map of the coordinates that are occupied
    ConcurrentHashMap<Position, Entity> occupiedCoordinates = DataServer.INSTANCE.getOccupiedCoordinates();
    //checks the position of the object against the occupied coordinates
    PhysicsResponse returnPosition = getCollisionArray(newPosition, oldPosition, occupiedCoordinates);
    if (returnPosition != null) {
      return returnPosition;
    }
    //else:
    collisionType = new Entity();
    //Returning a collision type and also the position
    PhysicsResponse noCollision = new PhysicsResponse(collisionType, newPosition);
    return noCollision;
  }

  /**
   * @param newPosition
   * @param oldPosition
   * @param occupiedCoordinates
   *
   * @return
   */
  private PhysicsResponse getCollisionArray(Position newPosition, Position oldPosition, ConcurrentHashMap<Position, Entity> occupiedCoordinates) {
    Entity collisionType;
    if (occupiedCoordinates.containsKey(newPosition)) {
      collisionType = occupiedCoordinates.get(newPosition);
      //checks the coordinate is not itself
      if (collisionType.getName().equals(this.getGameObject().getName())) {
        return null;
      }
      //Returning a collision type and also the position
      return new PhysicsResponse(collisionType, oldPosition);
    }
    return null;
  }

  /**
   * Sets acceleration for the object.
   */
  //Allows for power ups that increase this, to happen.
  private void setAcceleration() {
    if (this.getGameObject().getComponent(Acceleration.class).getPowerUpExists()) {
      acceleration = (gravity * (this.getGameObject().getComponent(Acceleration.class).getFrictionCoefficient() + objectMass) + thrust) / objectMass;
    } else {
      acceleration = 0;
    }
  }
}
