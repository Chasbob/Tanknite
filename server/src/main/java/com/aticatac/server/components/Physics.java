package com.aticatac.server.components;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.objectsystem.GameObject;
import org.apache.commons.collections4.BidiMap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Physics component will control the physics for the objects in the game. It will consider the new positions of
 * objects and alter any physics values (e.g. velocity) for the object. The positions of the object are kept in
 * TransformModel and will be altered by the logic, not this component.
 *
 * @author Claire Fletcher
 */
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
  private int velocity = 10;


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
  private Object[] move(String direction) {

    //the position for this tank.
    Position position = this.getGameObject().getComponent(Transform.class).getPosition();

    //If the health is below 10 then tank cannot move
    if((this.getGameObject().getComponent(Health.class).getHealth()) <= 10){

      Object[] cannotMove = {"none", position};
      return cannotMove;
    }

    //Old Positions
    int oldX = position.getX();
    int oldY = position.getY();

    //new proposed positions
    int newX = oldX;
    int newY = oldY;

    //change in time for calculations
    int dt = 1;

    //Set acceleration
    setAcceleration();

    //velocity altered if there is a power up
    if (acceleration != 0) {
      velocity = velocity + (acceleration * dt);
    }

    // travelling
    int movement = velocity*dt;

    //Calculates the new coordinates
    if (direction.equals("down")) {
      //only moving on y coord
      newY = oldY + (movement);
    }
    if (direction.equals("up")) {
      //only moving on y coord
      newY = oldY - (movement);
    }
    if (direction.equals("left")) {
      //only moving on x coord
      newX = oldX + (movement);
    }
    if (direction.equals("right")) {
      //only moving on x coord
      newX = oldX - (movement);
    }

    Position newPosition = new Position(newX, newY);
    if (newPosition.getX() > 1920 || newPosition.getX() < 20 || newPosition.getY() > 1920 || newPosition.getY() < 30) {
      return new Object[]{"none", position};
    }
    //Returning a collision type and also the position
    return collision(newPosition, position, "tank");

  }

  /**
   * BulletController move position.
   *
   * @param rotation the bearing
   * @return the position
   */
  public Object[] bulletMove(int rotation) {
    Position position = this.getGameObject().getComponent(Transform.class).getPosition();
    int xCoord = position.getX();
    int yCoord = position.getY();
    //converting the dt from nanoseconds to seconds
    int dt = 1;
    //Distance it moves is the change in time * the above velocity
    int distance = dt * velocity;
    //distance travelled in x direction is cos theta * distance
    int distanceX = distance * (int) Math.round(Math.cos(rotation));
    //distance travelled in y direction is sin theta * distance
    int distanceY = distance * (int) Math.round(Math.sin(rotation));
    //then add those to the original x and y
    int newX = xCoord + distanceX;
    int newY = yCoord + distanceY;

    Position newPosition = new Position(newX, newY);

    //returning a collision type and also the position
    Object[] returnPosition = collision(newPosition, position, "bullet");
    return returnPosition;

  }


  /**
   * Checks if object can move forwards and returns new position
   *
   * @return Position of the object
   */
  public Object[] moveUp() {
    return move("up");
  }

  /**
   * Checks if object can move backwards and returns new position
   *
   * @return Position of the object
   */
  public Object[] moveDown() {
    return move("down");
  }

  /**
   * Checks if object can move left and returns new position
   *
   * @return Position of the object
   */
  public Object[] moveLeft() {
    return move("left");
  }

  /**
   * Checks if object can more right and returns new position
   *
   * @return Position of the object
   */
  public Object[] moveRight() {
    return move("right");
  }


    /**
     * Collision method to check if objects have collided
     *
     * @param newPosition The previous position of the tank
     * @param oldPosition The new calculates position of the tank
     * @return An array of the collision type and the new position
     */
    //TODO new bullet collision method
    private Object[] collision (Position newPosition, Position oldPosition, String objectType) {


      if(objectType == "tank"){

        //creates and sets the new box coords based on the new position
        ArrayList<Position> box = this.getGameObject().getComponent(CollisionBox.class).getCollisionBox();
        //removes the old positions
        this.getGameObject().getComponent(CollisionBox.class).removeBoxFromData(box);
        //sets the new positions
        this.getGameObject().getComponent(CollisionBox.class).setCollisionBox(newPosition);
        //gets new position box
        box = this.getGameObject().getComponent(CollisionBox.class).getCollisionBox();


        //checks the box collision coords against the occupied
        for(int i=0; i<box.size(); i++){

          //map of the coordinates that are occupied
          HashMap<Position, String> occupiedCoordinates = DataServer.INSTANCE.getOccupiedCoordinates();

          Position position = box.get(i);
          Object[] returnPosition = getCollisionArray(position, oldPosition, occupiedCoordinates);
          if(returnPosition != null){
            //sets the box positions back to the old ones and puts that back into the data server
            this.getGameObject().getComponent(CollisionBox.class).setCollisionBox(oldPosition);
            this.getGameObject().getComponent(CollisionBox.class).addBoxToData(box, this.getGameObject().getName());
            return returnPosition;
          }

        }


        //adds the box positions to the server
        this.getGameObject().getComponent(CollisionBox.class).addBoxToData(box, this.getGameObject().getName());

      }

      //string for what has been collided with
      String collisionType;

      //map of the coordinates that are occupied
      HashMap<Position, String> occupiedCoordinates = DataServer.INSTANCE.getOccupiedCoordinates();


      //checks the position of the tank against the occupied coordinates
      Object[] returnPosition = getCollisionArray(newPosition, oldPosition, occupiedCoordinates);
      if (returnPosition != null) return returnPosition;

      //else:
      collisionType = "none";

      //Returning a collision type and also the position
      Object[] noCollision = new Object[2];
      noCollision[0] = collisionType;
      noCollision[1] = newPosition;

      return noCollision;

    }

  /**
   *
   * @param newPosition
   * @param oldPosition
   * @param occupiedCoordinates
   * @return
   */
    private Object[] getCollisionArray(Position newPosition, Position oldPosition, HashMap<Position, String> occupiedCoordinates) {

      String collisionType;

      if (occupiedCoordinates.containsKey(newPosition)) {

        collisionType = occupiedCoordinates.get(newPosition);

        //checks the coordinate is not itself
        if (collisionType.equals(this.getGameObject().getName())) {
          return null;
        }

        //Returning a collision type and also the position
        Object[] returnPosition = new Object[2];
        returnPosition[0] = collisionType;
        returnPosition[1] = oldPosition;

        return returnPosition;
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
