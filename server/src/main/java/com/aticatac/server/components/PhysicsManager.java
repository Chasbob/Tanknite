package com.aticatac.server.components;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.ServerData;
import com.aticatac.common.components.SpeedPowerUp;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.objectsystem.GameObject;

import java.util.ArrayList;

/**
 * PhysicsManager component will control the physics for the objects in the game. It will consider the new positions of
 * objects and alter any physics values (e.g. velocity) for the object. The positions of the object are kept in
 * Transform and will be altered by the logic, not this component.
 */
public class PhysicsManager extends Component {
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
     * Creates a new PhysicsManager with a parent.
     *
     * @param gameObject the game object
     */
    public PhysicsManager(GameObject gameObject) {
        super(gameObject);
    }


    /**
     *
     * @param objectName
     * @return
     */
    public Position initialisePosition(String objectName){

        //randomly generate X
        double initialX = 0;
        //randomly generate Y
        double initialY = 0;

        return new Position(initialX, initialY);
    }


    /**
     * Checks if the object can move in a direction and returns if it can and the new/current position.
     *
     * @return Position of the object
     */
    private Object[] move(String direction) {

        //the position for this tank.
        Position position = this.getGameObject().getComponent(Transform.class).getPosition();

        //Old Positions
        double oldX = position.getX();
        double oldY = position.getY();
        //new proposed positions
        double newX = oldX;
        double newY = oldY;
        //converting the dt from nanoseconds to seconds
        long dt = (this.getGameObject().getComponent(Time.class).timeDifference()) / 1000000000;

        //Set acceleration
        setAcceleration();

        //velocity altered if there is a power up
        if (acceleration != 0) {
            double newVelocity = velocity + (acceleration * dt);
            velocity = newVelocity;
        }
        //Calculates the new coordinates
        if (direction == "up" || direction == "down") {
            //only moving on y coord
            newY = oldY + (velocity * dt);
        }
        if (direction == "left" || direction == "right") {
            //only moving on x coord
            newX = oldX + (velocity * dt);
        }

        Position newPosition = new Position(newX, newY);

        //Returning a collision type and also the position
        Object[] returnPosition = collision(newPosition, position);

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
    //Test bullet calculation with bearings

    /**
     * Bullet move position.
     *
     * @param bearing the bearing
     * @return the position
     */
    public Object[] bulletMove(double bearing) {
        Position position = this.getGameObject().getComponent(Transform.class).getPosition();
        double xCoord = position.getX();
        double yCoord = position.getY();
        //converting the dt from nanoseconds to seconds
        long dt = (this.getGameObject().getComponent(Time.class).timeDifference()) / 1000000000;
        //Distance it moves is the change in time * the above velocity
        double distance = dt * velocity;
        //distance travelled in x direction is cos theta * distance
        double distanceX = distance * Math.cos(bearing);
        //distance travelled in y direction is sin theta * distance
        double distanceY = distance * Math.sin(bearing);
        //then add those to the original x and y
        double newX = xCoord + distanceX;
        double newY = yCoord + distanceY;

        Position newPosition = new Position(newX, newY);

        //Returning a collision type and also the position
        Object[] returnPosition = collision(newPosition, position);

        return returnPosition;
    }

    /**
     *
     */
    private Object[] collision(Position newPosition, Position oldPosition) {
        ArrayList<Position> occupiedCoordinates = this.getGameObject().getComponent(ServerData.class).getOccupiedCoordinates();
        ArrayList<Position> occupiedCoordinatesTank = this.getGameObject().getComponent(ServerData.class).getOccupiedCoordinates();

        Integer collisionType;
        // other object = 1, tank = 2, nothing = 0

        //checks for collisions with
        for (int i = 0; i < occupiedCoordinates.size(); i++) {
            //Checks if new position is already occupied
            //Checks the occupied coordinate isn't the current position.
            if (newPosition == occupiedCoordinates.get(i) && occupiedCoordinates.get(i) != oldPosition) {

                collisionType = 1;
                //Returning a collision type and also the position
                Object[] returnPosition = new Object[2];
                returnPosition[0] = collisionType;
                returnPosition[1] = newPosition;

                return returnPosition;
            }
        }

        //checks for collisions
        for (int i = 0; i < occupiedCoordinatesTank.size(); i++) {
            //Checks if new position is already occupied
            //Checks the occupied coordinate isn't the current position.
            if (newPosition == occupiedCoordinatesTank.get(i) && occupiedCoordinatesTank.get(i) != oldPosition) {
                collisionType = 2;
                //Returning a collision type and also the position
                Object[] returnPosition = new Object[2];
                returnPosition[0] = collisionType;
                returnPosition[1] = newPosition;

                return returnPosition;
            }
        }

        collisionType = 0;

        //Returning a collision type and also the position
        Object[] returnPosition = new Object[2];
        returnPosition[0] = collisionType;
        returnPosition[1] = newPosition;

        return returnPosition;
    }

    /**
     * Sets acceleration for the object.
     */
    //Allows for power ups that increase this, to happen.
    private void setAcceleration() {
        if (this.getGameObject().getComponent(SpeedPowerUp.class) == null) {
            acceleration = 0;
        } else {
            acceleration = (gravity * (this.getGameObject().getComponent(SpeedPowerUp.class).getFrictionCoefficient() + objectMass) + thrust) / objectMass;
        }
    }
}
