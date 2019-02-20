package com.aticatac.server.components;

import com.aticatac.common.components.transform.Position;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

public enum Data {

    INSTANCE;

    private BidiMap<String, Position> occupiedCoordinates= new DualHashBidiMap();
    Data(){

        //anything that needs setting up.

    }

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
    public void setCoordinates(String type, Position position) {
        this.occupiedCoordinates.replace(type, position);

    }

}
