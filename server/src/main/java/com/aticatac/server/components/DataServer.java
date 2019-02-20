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

    private BidiMap<String, Position> occupiedCoordinates= new DualHashBidiMap<>();
    DataServer(){ }

    /**
     * Sets the
     * @return
     */
    public BidiMap<String, Position> getOccupiedCoordinates(){

        return occupiedCoordinates;

    }

    /**
     * Sets the coordinates for the object in the map
     * @param type
     * @param position
     */
    public void setOccupiedCoordinates(String type, Position position) {
        this.occupiedCoordinates.replace(type, position);

    }

}
