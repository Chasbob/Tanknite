package com.aticatac.server.components;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.common.components.transform.Position;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * The server component will be used to store data that needs to be seen by the server and potentially passed to the client.
 * It will be the up to date synchronised data from all the GameObjects.
 * It will be stored in this component so the server does not need to check all the components for all objects each time.
 */

public class ServerData extends Component {

    //initial values
    /**Initial Health Value*/
    private static Integer healthInitial = 10;
    /** Initial Ammo value*/
    private static Integer ammoInitial = 10;
    /**Initial direction value*/
    private static Integer directionInitial = 0;


    //TODO provide the identifier that will be needed so server knows what info
    /**ArrayList of the currently occupied coordinates*/
    private ArrayList<Position> occupiedCoordinates = new ArrayList<Position>();

    //Where the string is the name of the gameObject the value refers to
    /**HashMap to store the health and the object the health relates to*/
    private HashMap<String, Integer> health = new HashMap<>();
    /**HashMap to store the ammo and the object the ammo relates to*/
    private HashMap<String, Integer> ammo = new HashMap<>();
    //What data type will direction be?
    /**HashMap to store the direction and the object the direction relates to*/
    private HashMap<String, Integer> direction = new HashMap<>();


    //This one is not immediately initialised
    //TODO New way of storing power ups as needs multiple for same



    /**
     * Creates a new Server Component with a parent.
     * @param componentParent The parent of the Server.
     */
    public Server(GameObject componentParent) {
        super(componentParent);
    }



    /**
     * Initialises variables for a new Game Object
     * @param name Name of the game object
     */
    public void initialiseValues (String name){

        health.put(name, healthInitial);
        ammo.put(name, ammoInitial);
        direction.put(name, directionInitial);

    }



    /**
     * Gets the currently occupied coordinates
     * @return The occupied coordinates.
     */
    public ArrayList<Position> getOccupiedCoordinates() {

        return occupiedCoordinates;

    }

    /**
     * Sets the currently occupied coordinates
     * @param oldCoords The old coordinates that were occupied.
     * @param newCoords The new coordinates that are now occupied.
     */
    public void setOccupiedCoordinates(Position oldCoords, Position newCoords){

        occupiedCoordinates.set(occupiedCoordinates.indexOf(oldCoords), newCoords);

    }



    /**
     * Gets the health for a game object
     * @return Health of the object
     */
    public int getHealth(String name){

        return health.get(name);

    }

    /**
     * Sets the health of the game object
     */
    public void setHealth(String name, int health){

        health.replace(name, health);

    }



    /**
     * Gets the ammo for a game object
     * @return Ammo for the object
     */
    public int getAmmo(String name) {

        return ammo.get(name);

    }

    /**
     * Sets the ammo for a game object
     */
    public void setAmmo(String name, int ammo){

        ammo.replace(name, ammo);

    }



    /**
     * Get the direction of a game object
     * @return Direction of an object
     */
    public int getDirection(String name){

       return direction.get(name);

    }

    /**
     * Sets the direction of a game object
     */
    public void setDirection(String name, int direction){

        direction.replace(name, direction);

    }


    //TODO get power up


    //TODO set power up


}