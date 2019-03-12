package com.aticatac.common.components;

import com.aticatac.common.components.transform.Position;
import com.aticatac.common.objectsystem.GameObject;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

/**
 * The server component will be used to store data that needs to be seen by the server and potentially passed to the
 * client. It will be the up to date synchronised data from all the GameObjects. It will be stored in this component so
 * the server does not need to check all the components for all objects each time.
 *
 * @author Claire Fletcher
 */
public class ServerData extends Component {
  //initial values
  /**
   * Initial Health Value
   */
  private static Integer healthInitial = 10;
  /**
   * Initial Ammo value
   */
  private static Integer ammoInitial = 10;
  /**
   * Initial direction value
   */
  private static Integer directionInitial = 0;
  /**
   * HashMap of the tank and it's current coordinates
   */
  private BidiMap<String, Position> occupiedCoordinatesTank = new DualHashBidiMap<>();
  /**
   * ArrayList of the currently occupied coordinates
   */
  private ArrayList<Position> occupiedCoordinates = new ArrayList<Position>();
  /**
   * HashMap to store the health and the object the health relates to
   */
  private HashMap<String, Integer> health = new HashMap<>();
  /**
   * HashMap to store the ammo and the object the ammo relates to
   */
  private HashMap<String, Integer> ammo = new HashMap<>();
  //What data type will direction be?
  //TODO New way of storing power ups as needs multiple for same

  /**
   * Creates a new Server Component with a parent.
   *
   * @param componentParent The parent of the Server.
   */
  public ServerData(GameObject componentParent) {
    super(componentParent);
  }

  /**
   * Initialises variables for a new Game Object
   *
   * @param name Name of the game object
   */
  public void initialiseValues(String name) {
    health.put(name, healthInitial);
    ammo.put(name, ammoInitial);
  }
}