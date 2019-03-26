package com.aticatac.client.server.objectsystem;

import com.aticatac.client.server.Position;
import com.aticatac.client.server.objectsystem.physics.CollisionBox;
import com.aticatac.common.objectsystem.EntityType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The enum Data server.
 *
 * @author Claire Fletcher
 */
public enum DataServer {
  /**
   * Instance data server.
   */
  INSTANCE;
  private final Entity wall;
  /**
   * Map of the occupied coordinates on map
   */
  private ConcurrentHashMap<Position, Entity> occupiedCoordinates = new ConcurrentHashMap<>();
  /**
   * Map of the game
   */
  private String[][] map;
  /**
   * Count of players in the game
   */
  private int playerCount;

  /**
   *
   */
  DataServer() {
    wall = new Entity(EntityType.WALL);
    //initialises the map
    map = convertTMXFileToArray();
    //adds the map to occupied coordinates
    for (int x = 0; x < 60; x++) {
      for (int y = 0; y < 60; y++) {
        if ((map[x][y]).equals("2")) {
          wall.setPosition(x * 32 + 16, y * 32 + 16);
          addEntity(wall);
//          createCollisionBox(y, x);
        }
      }
    }
  }

  public int size() {
    return occupiedCoordinates.size();
  }

  public String[][] getMap() {
    return map;
  }

  private String[][] convertTMXFileToArray() {
    String[][] map = new String[60][60];
    TiledMap tiledMap = new TmxMapLoader().load("maps/map.tmx");
    TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(1);
    for (int x = 0; x < layer.getWidth(); x++) {
      for (int y = 0; y < layer.getHeight(); y++) {
        try {
          map[layer.getWidth() - x - 1][layer.getHeight() - y - 1] = Integer.toString(layer.getCell(x, y).getTile().getId());
        } catch (NullPointerException e) {
          map[layer.getWidth() - x - 1][layer.getHeight() - y - 1] = "0";
        }
      }
    }
    return map;
  }

  /**
   * Sets the
   *
   * @return occupied coordinates
   */
  public ConcurrentHashMap<Position, Entity> getOccupiedCoordinates() {
    return occupiedCoordinates;
  }

  /**
   * Sets the coordinates for the object in the map
   *
   * @param newPosition the new position
   * @param type        the type
   * @param oldPosition the old position
   */
  public void setCoordinates(Position newPosition, Entity type, Position oldPosition) {
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
   * @param newPosition the new position
   * @param type        the type
   */
  //only called when the objects are first put into the map.
  public void setCoordinates(Position newPosition, Entity type) {
    occupiedCoordinates.put(newPosition, type);
  }

  /**
   * Delete coordinates.
   *
   * @param key the key
   */
  public void deleteCoordinates(Position key) {
    occupiedCoordinates.remove(key);
  }

  /**
   * Gets player count.
   *
   * @return player count
   */
  public int getPlayerCount() {
    return playerCount;
  }

  /**
   * Sets player count.
   *
   * @param count the count
   */
  public void setPlayerCount(int count) {
    playerCount = count;
  }

  private void createCollisionBox(int mapX, int mapY) {
    int mapPositionX = (32 * (mapX));
    int mapPositionY = (32 * (mapY));
    //adds centre of the element to the occupied
    Position centrePosition = new Position(mapPositionX, mapPositionY);
//    setCoordinates(centrePosition, wall);
    int lowerY = mapPositionX;
    int lowerX = mapPositionY;
    int higherY = mapPositionX + 32;
    int higherX = mapPositionY + 32;
    //positions for bottom of box
    for (int x = 0; x < 31; x++) {
      Position position = new Position(lowerX + x, lowerY);
      setCoordinates(position, wall);
    }
    //positions for left of box
    for (int y = 0; y < 31; y++) {
      Position position = new Position(lowerX, lowerY + y);
      setCoordinates(position, wall);
    }
    //positions for top of box
    for (int x = 0; x < 31; x++) {
      Position position = new Position(lowerX + x, higherY);
      setCoordinates(position, wall);
    }
    //positions for right of box
    for (int y = 0; y < 31; y++) {
      Position position = new Position(higherX, lowerY + y);
      setCoordinates(position, wall);
    }
  }

  /**
   * Remove box from data.
   *
   * @param box the box
   */
  public void removeBoxFromData(HashSet<Position> box) {
//    for (int i = 0; i < box.size(); i++) {
//      Position position = box.(i);
//      deleteCoordinates(position);
//    }
    for (Position p :
        box) {
      deleteCoordinates(p);
    }
  }

  /**
   * Occupied boolean.
   *
   * @param p the p
   *
   * @return the boolean
   */
  public boolean occupied(Position p) {
    return occupiedCoordinates.containsKey(p);
  }

  /**
   * Gets entity.
   *
   * @param p the p
   *
   * @return the entity
   */
  public Entity getEntity(Position p) {
    return occupiedCoordinates.getOrDefault(p, Entity.empty);
  }

  /**
   * Remove box from data.
   *
   * @param box the box
   */
  public void removeBoxFromData(CollisionBox box) {
    removeBoxFromData(box.getBox());
  }

  /**
   * Add box to data.
   *
   * @param box    the box
   * @param entity the object entity
   */
  public void addBoxToData(HashSet<Position> box, Entity entity) {
    for (Position position : box) {
      occupiedCoordinates.put(position, entity);
//      DataServer.INSTANCE.setCoordinates(position, entity);
    }
  }

  /**
   * Add box to data.
   *
   * @param box    the box
   * @param entity the entity
   */
  public void addBoxToData(CollisionBox box, Entity entity) {
    addBoxToData(box.getBox(), entity);
  }

  public void addEntity(Entity entity) {
    addBoxToData(entity.getCollisionBox().getBox(), entity);
  }

  public boolean containsBox(CollisionBox box) {
    for (Position p :
        box.getBox()) {
      if (occupiedCoordinates.containsKey(p)) {
        return true;
      }
    }
    return false;
  }
}
