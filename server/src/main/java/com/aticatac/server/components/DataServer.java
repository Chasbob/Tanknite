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

    /**Map of the occupied coordinates on map*/
    private BidiMap<Position, String> occupiedCoordinates= new DualHashBidiMap<>();


    /**Map of the tiles*/
    //each tile is 32x32
    int[][] map = new int[60][60];


    /**
     *
     */
    DataServer(){
        

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
