package com.aticatac.server.components;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.common.components.transform.Position;
import java.util.HashMap;
import java.util.ArrayList;

public class Server extends Component {

    //server will have all of the updated and synchronised data
    //Whenever a game object is created, position changed, or any of below changed, must also update this.

    //TODO provide the identifier that will be needed so server knows what info
    //currently just needs to know the occupied coordinates not by what
    //Make this an array list
    private ArrayList<Position> occupiedCoordinates = new ArrayList<>();

    //TODO Use test to check if these are good for sending
    //could just have array of the objects? Get data from components using getters?
    private HashMap<String, Integer> health = new HashMap<>();
    private HashMap<String, Integer> ammo = new HashMap<>();
    private HashMap<String, String> powerUp = new HashMap<>();
    //What data type will direction be?
    private HashMap<String, Integer> direction = new HashMap<>();

    /**
     * Constructor for component: Server
     * @param parent
     */
    public Server(GameObject parent) {
        super(parent);
    }

    /**
     * Gets the currently occupied coordinates
     * @return
     */
    public ArrayList<Position> getOccupiedCoordinates() {
        return occupiedCoordinates;
    }

    /**
     * Sets the currently occupied coordinates
     * @param oldCoords
     * @param newCoords
     */
    public void setOccupiedCoordinates( Position oldCoords, Position newCoords){

        //looks for the old ones and replaces with new

    }
}