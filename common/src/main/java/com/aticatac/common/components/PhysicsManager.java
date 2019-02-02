package com.aticatac.common.components;

import com.aticatac.common.objectsystem.GameObject;

public class PhysicsManager extends Component {

    private int tankMass = 10;
    private int thrust = 10;
    private int gravity = 10;
    private int frictionCoefficient = 10;
    private int acceleration;

    public PhysicsManager(GameObject parent) {
        super(parent);
    }


    //TODO Tank move/check collision
    public int[] move(){

        //Must do nulls for getting positions etc.
        //Tank.getTransform();
        // Transform.getPosition(x);
        //Don't need to set the new position only get them, logic will set them

        //Will be getting the positions of everything from a components off something at the top of the tree.

        //false return 0, XCOORD, YCOORD
        int[] movement = {0};
        //true return 1, xccord, ycoord
        //int[] movement = {1,x,y};

        return movement;
    }

    //TODO Bullet interactions

    
    private void setAcceleration(){

        if(Tank.getComponent(SpeedPowerUp.class) == null){

            acceleration = (gravity*(frictionCoefficient + tankMass) + thrust)/ tankMass;

        }

        else{

            acceleration = (gravity*(Tank.getComponent(SpeedPowerUp.class).getFrictionCoefficient() + tankMass) + thrust)/ tankMass;

        }

    }


}
