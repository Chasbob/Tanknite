package com.aticatac.server.components;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.SpeedPowerUp;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.common.components.transform.*;

import java.util.ArrayList;

/**
 * PhysicsManager component will control the physics for the objects in the game.
 * It will consider the new positions of objects and alter any physics values (e.g. velocity) for the object.
 * The positions of the object are kept in Transform and will be altered by the logic, not this component.
 * */

public class PhysicsManager extends Component {

    /**The gravity acting for all objects*/
    private static double gravity = 10;

    /**The mass of this object*/
    private double objectMass = 10;
    /**The thrust for this tank*/
    private double thrust = 10;
    /**The acceleration for this tank*/
    private double acceleration;
    /**The velocity for this tank*/
    private double velocity = 10;

    /**
     * Creates a new PhysicsManager with a parent.
     * @param componentParent The parent of the PhysicsManager
     */
    public PhysicsManager(GameObject componentParent){
        super(componentParent);
    }



    /**
     * Checks if the object can move in a direction and returns if it can and the new/current position.
     * @return Position of the object
     */
    private Position move(String direction){

        //the position for this tank.
        Position position = componentParent.getComponent(Transform.class).GetPosition();
        //Positions of everything
        //TODO make this get the server component from a different object not this one
        ArrayList<Position> occupiedCoordinates = componentParent.findObject("String").getComponent(Server.class).getOccupiedCoordinates();

        //old positions
        double oldX = position.getX();
        double oldY = position.getY();
        //new proposed positions
        double newX = oldX;
        double newY = oldY;

        //converting the dt from nanoseconds to seconds
        long dt = (componentParent.getComponent(Time.class).timeDifference())/1000000000;

        //velocity altered if there is a powerup
        if(acceleration != 0) {
            double newVelocity = velocity + (acceleration * dt);
            velocity = newVelocity;
        }

        //Calculates the new coordinates
        if(direction == "up" || direction == "down"){

            //only moving on y coord
            newY = oldY + (velocity * dt);

        }

        if(direction == "left" || direction == "right"){

            //only moving on x coord
            newX = oldX + (velocity * dt);

        }

        //Sets the new coordinates as a position
        Position newPosition = new Position(newX, newY);

        //checks for collisions
        for(int i=0; i<occupiedCoordinates.size(); i++){

            //Checks if new position is already occupied
            //Checks the occupied coordinate isn't the current position (TODO is this needed)
            if(newPosition == occupiedCoordinates.get(i) && occupiedCoordinates.get(i) != position){

                return position;
            }

        }

        //No collision occurred
        return newPosition;
    }

    /**
     * Checks if object can move forwards and returns new position
     * @return Position of the object
     */
    public Position moveUp(){

        return move("up");

    }

    /**
     * Checks if object can move backwards and returns new position
     * @return Position of the object
     */
    public Position moveDown(){

        return move("down");

    }

    /**
     * Checks if object can move left and returns new position
     * @return Position of the object
     */
    public Position moveLeft(){

        return move("left");

    }

    /**
     * Checks if object can more right and returns new position
     * @return Position of the object
     */
    public Position moveRight(){

        return move("right");

    }


    /**
     * Sets acceleration for the object.
     */
    //Allows for powerups that increase this, to happen.
    private void setAcceleration(){

        if(componentParent.getComponent(SpeedPowerUp.class) == null){

            acceleration = 0;

        }

        else{

            acceleration = (gravity*(componentParent.getComponent(SpeedPowerUp.class).getFrictionCoefficient() + objectMass) + thrust)/ objectMass;

        }

    }


    /**
     * Checks if a bullet has collided with something
     * @return
     */
    public boolean bulletMovement(String direction, Position position){

        //will start from the location of the tank.

        //TODO make this get the server component from a different object not this one
        ArrayList<Position> occupiedCoordinates = componentParent.findObject("String").getComponent(Server.class).getOccupiedCoordinates();

        //the position for the bullet.
        Position positionBullet = position;
        double xCoord = positionBullet.getX();
        double yCoord = positionBullet.getY();

        //moving up or down for the bullet
        if(direction == "up" || direction == "down"){

            //checks for collisions
            for(int i=0; i<occupiedCoordinates.size(); i++){

                if(yCoord++ == occupiedCoordinates.get(i).getY()){

                    return false;

                }

            }

        }

        //moving right or left for the bullet
        if(direction == "right" || direction == "left"){

            //checks for collisions
            for(int i=0; i<occupiedCoordinates.size(); i++){

                if(xCoord++ == occupiedCoordinates.get(i).getX()){

                    return false;

                }

            }

        }

        return true;

    }


}
