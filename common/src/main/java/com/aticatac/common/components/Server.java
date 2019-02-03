package com.aticatac.common.components;

import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.common.components.transform.Position;
import java.util.HashMap;

import java.util.HashMap;

public class Server extends Component {

    //server will have all of the updated and synchronised data
    //Whenever a game object is created, position changed, or any of below changed, must also update this.

    //TODO provide the identifier that will be needed so server knows what info
    //currently just needs to know the occupied coordinates not by what
    private Position[] occupiedCoordinates;

    //TODO ask charlie best structure for the data (leave for now)
    //could just have array of the objects? Get data from componenets using getters?
    private HashMap<String, Integer> health = new HashMap<String, Integer>();
    private HashMap<String, Integer> ammo = new HashMap<String, Integer>();
    private HashMap<String, String> powerUp = new HashMap<String, String>();
    //What data type will direction be?
    private HashMap<String, Integer> direction = new HashMap<String, Integer>();

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
    public int[][] getOccupiedCoordinates() {
        return occupiedCoordinates;
    }


}