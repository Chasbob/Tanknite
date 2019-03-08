package com.aticatac.server.components;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.objectsystem.GameObject;

import org.apache.commons.collections4.BidiMap;

import java.util.ArrayList;

/**
 * Physics component will control the physics for the objects in the game. It will consider the new positions of
 * objects and alter any physics values (e.g. velocity) for the object. The positions of the object are kept in
 * TransformModel and will be altered by the logic, not this component.
 *
 * @author Claire Fletcher
 *
 */
public class Physics extends Component {
    /**
     * The gravity acting for all objects
     */
    private static double gravity = 10;
    /**
     * The mass of this object
     */
    private double objectMass = 10;
    /**
     * The thrust for this tank
     */
    private double thrust = 10;
    /**
     * The acceleration for this tank
     */
    private double acceleration;
    /**
     * The velocity for this tank
     */
    private double velocity = 10;

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
        double oldX = position.getX();
        double oldY = position.getY();

        //new proposed positions
        double newX = oldX;
        double newY = oldY;

        //change in time for calculations
        double dt = 1;

        //Set acceleration
        setAcceleration();

        //velocity altered if there is a power up
        if (acceleration != 0) {
            velocity = velocity + (acceleration * dt);
        }

        // travelling
        double movement = velocity*dt;

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

        //Returning a collision type and also the position
        return collision(newPosition, position, direction);

    }

     /**
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
    //Test bullet calculation with bearings

    /**
     * BulletController move position.
     *
     * @param rotation the bearing
     * @return the position
     */
    public Object[] bulletMove(double rotation) {
        Position position = this.getGameObject().getComponent(Transform.class).getPosition();
        double xCoord = position.getX();
        double yCoord = position.getY();
        //converting the dt from nanoseconds to seconds
        long dt = 1;
        //Distance it moves is the change in time * the above velocity
        double distance = dt * velocity;
        //distance travelled in x direction is cos theta * distance
        double distanceX = distance * Math.cos(rotation);
        //distance travelled in y direction is sin theta * distance
        double distanceY = distance * Math.sin(rotation);
        //then add those to the original x and y
        double newX = xCoord + distanceX;
        double newY = yCoord + distanceY;

        Position newPosition = new Position(newX, newY);

        //Returning a collision type and also the position
        Object[] returnPosition = collision(newPosition, position, "bullet");

        return returnPosition;
    }

    /**
     * Collision method to check if objects have collided
     *
     * @param newPosition The previous position of the tank
     * @param oldPosition The new calculates position of the tank
     * @return An array of the collision type and the new position
     */
    //TODO new bullet collision method
    private Object[] collision(Position newPosition, Position oldPosition, String direction) {

        //string for what has been collided with
        String collisionType;

        //creates and sets the new box coords based on the new position
        ArrayList<Position> box = this.getGameObject().getComponent(CollisionBox.class).getCollisionBox();
        //removes the old positions
        this.getGameObject().getComponent(CollisionBox.class).removeBoxfromData(box);
        //sets the new positions
        this.getGameObject().getComponent(CollisionBox.class).setCollisionBox(newPosition);
        //gets new position box
        box = this.getGameObject().getComponent(CollisionBox.class).getCollisionBox();


        //map of the coordinates that are occupied
        BidiMap<Position, String> occupiedCoordinates = DataServer.INSTANCE.getOccupiedCoordinates();


        //checks the box collision coords against the occupied
        for(int i=0; i<box.size(); i++){

            Position position = box.get(i);
            Object[] returnPosition = getCollisionArray(position, oldPosition, occupiedCoordinates);
            if(returnPosition != null){
                //sets the box positions back to the old ones and puts that back into the data server
                this.getGameObject().getComponent(CollisionBox.class).setCollisionBox(oldPosition);
                this.getGameObject().getComponent(CollisionBox.class).addBoxToData(box, this.getGameObject().getName());
                return returnPosition;
            }
            else {
                //adds the new positions into the dataserver
                //TODO Fix this line
                //this.getGameObject().getComponent(CollisionBox.class).addBoxToData(box, this.getGameObject().getName());
            }

        }


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

    private Object[] getCollisionArray(Position newPosition, Position oldPosition, BidiMap<Position, String> occupiedCoordinates) {

        String collisionType;

        if(occupiedCoordinates.containsKey(newPosition)){

            collisionType = occupiedCoordinates.get(newPosition);

            //checks the coordinate is not itself
            if(collisionType.equals(this.getGameObject().getName())){
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
