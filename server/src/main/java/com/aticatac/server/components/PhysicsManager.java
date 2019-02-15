//package com.aticatac.server.components;
//
//import com.aticatac.common.components.Component;
//import com.aticatac.common.components.SpeedPowerUp;
//import com.aticatac.common.objectsystem.GameObject;
//import com.aticatac.common.components.transform.*;
//
//public class PhysicsManager extends Component {
//
//    private int tankMass = 10;
//    private int thrust = 10;
//    private int gravity = 10;
//    private int frictionCoefficient = 10;
//    private int acceleration;
//
//    public PhysicsManager(GameObject parent) {
//        super(parent);
//    }
//    /**
//     * Checks if the object can move and returns if it can and the new/current position.
//     * @return
//     */
//    public Position move(){
//
//        //the position for this tank.
////        Position position = Tank.getComponent(Transform.class).getPosition();
//        //Positions of everything
//        Position[] occupiedCoordinates = tank.findObject("String").getComponent(Server.class).getOccupiedCoordinates;
//
//        //TODO figure out the new positions.
//        Position newPosition;
//
//        //checks for collisions
//        for(int i=0; i<occupiedCoordinates.length; i++){
//
//            //Checks if new position is already occupied
//            //Checks the occupied coordinate isnt the current position (TODO is this needed)
//            if(newPosition == occupiedCoordinates[i] && occupiedCoordinates[i] != position){
//
//                //meaning it has collided with something.
//                return position;
//            }
//
//        }
//
//        //No collision occurred
//        return newPosition;
//    }
//
//
//    //TODO Bullet interactions
//
//
//    /**
//     * Sets acceleration.
//     */
//    private void setAcceleration(){
//
//        if(Tank.getComponent(SpeedPowerUp.class) == null){
//
//            acceleration = (gravity*(frictionCoefficient + tankMass) + thrust)/ tankMass;
//
//        }
//
//        else{
//
//            acceleration = (gravity*(Tank.getComponent(SpeedPowerUp.class).getFrictionCoefficient() + tankMass) + thrust)/ tankMass;
//
//        }
//
//    }
//
//
//}

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
    private static int gravity = 10;

    /**The mass of this object*/
    private int objectMass = 10;
    /**The thrust for this tank*/
    private int thrust = 10;
    /**The frictionCoefficient for this tank*/
    private int frictionCoefficient = 10;
    /**The acceleration for this tank*/
    private int acceleration;

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

        //TODO figure out the new positions.

        //new proposed positions
        double newX = 0;
        double newY = 0;
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
    public Position moveForwards(){

        return move("forwards");

    }

    /**
     * Checks if object can move backwards and returns new position
     * @return Position of the object
     */
    public Position moveBackwards(){

        return move("backwards");

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

    
    //TODO Bullet interactions


    /**
     * Sets acceleration for the object.
     */
    private void setAcceleration(){

        if(componentParent.getComponent(SpeedPowerUp.class) == null){

            acceleration = (gravity*(frictionCoefficient + objectMass) + thrust)/ objectMass;

        }

        else{

            acceleration = (gravity*(componentParent.getComponent(SpeedPowerUp.class).getFrictionCoefficient() + objectMass) + thrust)/ objectMass;

        }

    }


}
