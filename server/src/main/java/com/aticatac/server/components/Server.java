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

    /**HashMap to store the health and the object the health relates to*/
    private HashMap<String, Integer> health = new HashMap<>();
    /**HashMap to store the ammo and the object the ammo relates to*/
    private HashMap<String, Integer> ammo = new HashMap<>();
    /**HashMap to store the powerups and the object the powerups relate to*/
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
   // public void setOccupiedCoordinates( Position oldCoords, Position newCoords){



   // }
}