package com.aticatac.server.components;

import com.aticatac.common.components.transform.Position;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

/**
 * @author Claire Fletcher
 */
public enum DataServer {
  INSTANCE;
  private BidiMap<Position, String> occupiedCoordinates = new DualHashBidiMap<>();

  DataServer() {
    //TODO Remove this when the map is added but for now add in coordinates for edges of map
  }

  /**
   * Sets the
   *
   * @return
   */
  public BidiMap<Position, String> getOccupiedCoordinates() {
    return occupiedCoordinates;
  }

  /**
   * Sets the coordinates for the object in the map
   *
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
   *
   * @param type
   * @param newPosition
   */
  //only called when the tanks are first put into the map.
  public void setCoordinates(Position newPosition, String type) {
    occupiedCoordinates.put(newPosition, type);
  }
}
