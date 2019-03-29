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
   * Creates the DataServer
   * Initialises the map into the occupied coordinates
   * Walls are now objects that can be collided with.
   */
  DataServer() {
    //Wall entities to be added to the map
    wall = new Entity(EntityType.WALL);
    //initialises the map
    map = convertTMXFileToArray();

    //adds the map to occupied coordinates using file reader
    for (int x = 0; x < 60; x++) {
      for (int y = 0; y < 60; y++) {
        //Walls are 2 in the map file
        if (!(map[x][y]).equals("0")) {
          //sets collision box within the entity
          wall.setPosition(x * 32 + 16, y * 32 + 16);
          addEntity(wall);
        }

      }
    }
  }

  /**
   * Gets the size of the occupied Coordinates map
   *
   * @return size of occupied coordinates map
   */
  public int size() {
    return occupiedCoordinates.size();
  }

  /**
   * Gets the map
   *
   * @return the map as a 2d array of strings
   */
  public String[][] getMap() {
    return map;
  }

  /**
   * Converts a TMXfile to an array
   * Converts the map file to an array
   * @return the map as a 2d array of strings
   */
  private String[][] convertTMXFileToArray() {
    String[][] map = new String[60][60];
    TiledMap tiledMap = new TmxMapLoader().load("maps/mapData/map.tmx");
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
   * Gets the occupied coordinates map.
   *
   * @return occupied coordinates map.
   */
  public ConcurrentHashMap<Position, Entity> getOccupiedCoordinates() {
    return occupiedCoordinates;
  }


  /**
   * Sets the coordinates for the object in the map
   *
   * @param newPosition the new position
   * @param type        the type
   */
  public void setCoordinates(Position newPosition, Entity type) {
    occupiedCoordinates.put(newPosition, type);
  }

  /**
   * Delete coordinates from the map of occupied coordinates
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


  /**
   * Remove box from data.
   *
   * @param box the box
   */
  public void removeBoxFromData(HashSet<Position> box) {
    for (Position p :
        box) {
      deleteCoordinates(p);
    }
  }

  /**
   * Occupied boolean.
   *
   * @param p the p
   * @return the boolean
   */
  public boolean occupied(Position p) {
    return occupiedCoordinates.containsKey(p);
  }

  /**
   * Gets entity.
   *
   * @param p the p
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

  /**
   * Add the entity to the occupied coordinates map
   *
   * @param entity the entity that is being added
   */
  public void addEntity(Entity entity) {
    addBoxToData(entity.getCollisionBox().getBox(), entity);
  }


  /**
   * Checks if something contains the given box
   *
   * @param box the box to be compared
   * @return true if box does exist, false if not.
   */
  public boolean containsBox(CollisionBox box) {
    //checks each position of the box
    for (Position p : box.getBox()) {
      //if the occupied contains the position return true
      if (occupiedCoordinates.containsKey(p)) {
        return true;
      }
    }
    return false;
  }
}
