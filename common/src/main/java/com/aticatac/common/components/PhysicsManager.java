package com.aticatac.common.components;

import com.aticatac.common.objectsystem.GameObject;

public class PhysicsManager extends Component {

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

    //TODO Tank Acceleration
    //this method will be

    //TODO Bullet interactions

    //array of whether it can move or not and then the two coordinates

    //Tank.getComponent() tank.findObject("tank")
    //where tank is the name of the thing we are looking for.
    //
    



}
