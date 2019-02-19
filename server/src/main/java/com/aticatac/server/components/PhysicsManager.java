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
 * TransformModel and will be altered by the logic, not this component.
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
     * Checks if the object can move in a direction and returns if it can and the new/current position.
     *
     * @return Position of the object
     */
    private Position move(String direction) {
        //the position for this tank.
        Position position = this.getGameObject().getComponent(Transform.class).getPosition();
        //Positions of everything
        //TODO make this get the server component from a different object not this one
        ArrayList<Position> occupiedCoordinates = this.getGameObject().getComponent(ServerData.class).getOccupiedCoordinates();
        //old positions
        double oldX = position.getX();
        double oldY = position.getY();
        //new proposed positions
        double newX = oldX;
        double newY = oldY;
        //converting the dt from nanoseconds to seconds
        long dt = (this.getGameObject().getComponent(Time.class).timeDifference()) / 1000000000;
        //velocity altered if there is a powerup
        //TODO Check that this works as don't think it does
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
        if (collision(newPosition, position)) {
            return position;
        }
        return newPosition;
    }

    /**
     * Checks if object can move forwards and returns new position
     *
     * @return Position of the object
     */
    public Position moveUp() {
        return move("up");
    }

    /**
     * Checks if object can move backwards and returns new position
     *
     * @return Position of the object
     */
    public Position moveDown() {
        return move("down");
    }

    /**
     * Checks if object can move left and returns new position
     *
     * @return Position of the object
     */
    public Position moveLeft() {
        return move("left");
    }

    /**
     * Checks if object can more right and returns new position
     *
     * @return Position of the object
     */
    public Position moveRight() {
        return move("right");
    }
    //Test bullet calculation with bearings

    /**
     * Bullet move position.
     *
     * @param bearing the bearing
     * @return the position
     */
    public Position bulletMove(double bearing) {
        Position position = this.getGameObject().getComponent(Transform.class).getPosition();
        double xCoord = position.getX();
        double yCoord = position.getY();
        //converting the dt from nanoseconds to seconds
        long dt = (this.getGameObject().getComponent(Time.class).timeDifference()) / 1000000000;
        //Distance it moves is the change in time * the above velocity
        double distance = dt * velocity;
        //distance travelled in x direction is cos theta * distance
        double distanceX = distance * Math.cos(Math.toRadians(bearing));
        //distance travelled in y direction is sin theta * distance
        double distanceY = distance * Math.sin(Math.toRadians(bearing));
        //then add those to the original x and y
        double newX = xCoord + distanceX;
        double newY = yCoord + distanceY;
        Position newPosition = new Position(newX, newY);
        if (collision(newPosition, position)) {
            return position;
        }
        return newPosition;
    }

    /**
     *
     */
    private boolean collision(Position newPosition, Position oldPosition) {
        //TODO make this get the server component from a different object not this one
        ArrayList<Position> occupiedCoordinates = this.getGameObject().getComponent(ServerData.class).getOccupiedCoordinates();
        //checks for collisions
        for (int i = 0; i < occupiedCoordinates.size(); i++) {
            //Checks if new position is already occupied
            //Checks the occupied coordinate isn't the current position (TODO is this needed)
            if (newPosition == occupiedCoordinates.get(i) && occupiedCoordinates.get(i) != oldPosition) {
                return true;
            }
        }
        return false;
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
