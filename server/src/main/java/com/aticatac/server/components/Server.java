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

public class Server extends Component {

    //TODO provide the identifier that will be needed so server knows what info
    /**ArrayList of the currently occupied coordinates*/
    private ArrayList<Position> occupiedCoordinates = new ArrayList<Position>();

    //Where the string is the name of the gameObject the value refers to
    /**HashMap to store the health and the object the health relates to*/
    private HashMap<String, Integer> health = new HashMap<>();
    /**HashMap to store the ammo and the object the ammo relates to*/
    private HashMap<String, Integer> ammo = new HashMap<>();
    /**HashMap to store the power ups and the object the power ups relate to*/
    private HashMap<String, String> powerUp = new HashMap<>();
    //What data type will direction be?
    /**HashMap to store the direction and the object the direction relates to*/
    private HashMap<String, Integer> direction = new HashMap<>();

    /**
     * Creates a new Server Component with a parent.
     * @param componentParent The parent of the Server.
     */
    public Server(GameObject componentParent) {
        super(componentParent);
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
   // public void setOccupiedCoordinates(Position oldCoords, Position newCoords){

   // }


    //TODO get health

    /**
     * Gets the health for a game object
     * @return Health of the object
     */
    public int getHealth(){


    }

    //TODO set health

    /**
     * Sets the health of the game object
     */
    public void setHealth(String name, int health){

    }

    //TODO get ammo

    /**
     * Gets the ammo for a game object
     * @return Ammo for the object
     */
    public int getAmmo(){

    }

    //TODO set ammo

    /**
     * Sets the ammo for a game object
     */
    public void setAmmo(){

    }

    //TODO get power up

    /**
     * Get the powerups for a game object
     * @return Powerups for a game object
     */
    public String getPowerUp(){

    }

    //TODO set power up

    /**
     * Set the power up for an object
     */
    public void setPowerUp(){

    }

    //TODO get direction

    /**
     * Get the direction of a game object
     * @return Direction of an object
     */
    public int getDirection(){


    }

    //TODO set direction

    /**
     * Sets the direction of a game object
     */
    public void setDirection(){

    }

}