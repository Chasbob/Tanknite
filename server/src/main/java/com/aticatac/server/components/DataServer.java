package com.aticatac.server.components;

import com.aticatac.common.components.transform.Position;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;


/**
 *
 *
 * @author Claire Fletcher
 */

public enum DataServer {

    INSTANCE;

    //TODO add power ups into this and the walls
    //ammo, health, damage, speed, <-for the power ups

    /***/
    private BidiMap<Position, String> occupiedCoordinates= new DualHashBidiMap<>();

    /**
     *
     */
    DataServer(){


        //for loop that creates a position then sets it as a wall:
        for(int i=0; i<1921; i++){

            Position position = new Position(i, 0);

            setCoordinates(position, "wall");

        }


        for(int j=0; j<1921; j++){

            Position position = new Position(0, j);

            setCoordinates(position, "wall");

        }


    }


    /**
     * Sets the
     * @return
     */
    public BidiMap<Position, String> getOccupiedCoordinates(){

        return occupiedCoordinates;

    }

    /**
     * Sets the coordinates for the object in the map
     * @param type
     * @param newPosition
     */
    public void setCoordinates(Position newPosition, String type, Position oldPosition) {

        //if old position is not in there then don't remove
        if (!(occupiedCoordinates.containsKey(oldPosition))) {
            occupiedCoordinates.put(newPosition, type);
        } else {

            occupiedCoordinates.remove(oldPosition);
            occupiedCoordinates.put(newPosition, type);

        }
    }

    /**
     * Sets the coordinates for the object in the map
     * @param type
     * @param newPosition
     */
    //only called when the objects are first put into the map.
    public void setCoordinates(Position newPosition, String type) {

        occupiedCoordinates.put(newPosition, type);


    }

    /**
     *
     * @param key
     */
    public void deleteCoordinates(Position key){

        occupiedCoordinates.remove(key);
    }

}
