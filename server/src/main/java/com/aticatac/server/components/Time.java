package com.aticatac.server.components;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;

public class Time extends Component{

    //TODO consider what happens when

    /**The start time for the movement*/
    private long startTime;
    /**The current since the movement started*/
    private long currentTime;

    /**
     * Creates a new Time component with a parent
     * @param componentParent Parent of the component
     */
    public Time(GameObject componentParent){
        super(componentParent);

    }

    /**
     * Records the time for the start of the movement.
     */
    public void startMoving(){

        //Initial start time for the movement calculated from system
        startTime = System.nanoTime();

    }

    /**
     * Calculates the time difference since the start of the movement.
     * @return The time difference
     */
    public long timeDifference(){

        //calculates the current time when this is called
        currentTime = System.nanoTime();

        //calcualtes the difference between the start and current time
        long timeDifference = currentTime - startTime;

        //Sets the start time to the current so that the next time difference is correct.
        startTime = currentTime;

        return timeDifference;

    }



}
