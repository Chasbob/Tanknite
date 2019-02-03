package com.aticatac.common.components;

import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.common.components.transform.*;

public class PhysicsManager extends Component {

    private int tankMass = 10;
    private int thrust = 10;
    private int gravity = 10;
    private int frictionCoefficient = 10;
    private int acceleration;

    public PhysicsManager(GameObject parent) {
        super(parent);
    }

    /**
     * Checks if the object can move and returns if it can and the new/current position.
     * @return
     */
    public Position move(){

        //the position for this tank.
        Position position = Tank.getComponent(Transform.class).getPosition();
        //Positions of everything
        Position[] occupiedCoordinates = tank.findObject("String").getComponent(Server.class).getOccupiedCoordinates;

        //TODO figure out the new positions.
        Position newPosition;

        //checks for collisions
        for(int i=0; i<occupiedCoordinates.length; i++){

            //Checks if new position is already occupied
            //Checks the occupied coordinate isnt the current position (TODO is this needed)
            if(newPosition == occupiedCoordinates[i] && occupiedCoordinates[i] != position){

                //meaning it has collided with something.
                return position;
            }

        }

        //No collision occurred
        return newPosition;
    }

    
    //TODO Bullet interactions


    /**
     * Sets acceleration.
     */
    private void setAcceleration(){

        if(Tank.getComponent(SpeedPowerUp.class) == null){

            acceleration = (gravity*(frictionCoefficient + tankMass) + thrust)/ tankMass;

        }

        else{

            acceleration = (gravity*(Tank.getComponent(SpeedPowerUp.class).getFrictionCoefficient() + tankMass) + thrust)/ tankMass;

        }

    }


}
