package com.aticatac.server.components;

import com.aticatac.common.components.transform.Position;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author Claire Fletcher
 */
public enum DataServer {


  INSTANCE;

  /**
   * Map of the occupied coordinates on map
   */
  private HashMap<Position, String> occupiedCoordinates = new HashMap<>();

  /**
   * Map of the game
   */
  private String[][] map = new String[60][60];


  /**
   *
   */
  DataServer() {

    //initialises the map
    try {
      map = convertTMXFileToIntArray();
    } catch (FileNotFoundException e) {
      System.out.println("Yo where'd the file go");
    }

    //adds the map to occupied coordinates
    for (int i = 0; i < 60; i++) {
      for (int j = 0; j < 60; j++) {

        if ((map[i][j]).equals("2")) {

          createCollisionBox(i, j);

        }

      }

    }

  }


  /**
   * @return
   * @throws FileNotFoundException
   */
  private String[][] convertTMXFileToIntArray() throws FileNotFoundException {

    //reads in the file
    Scanner s = new Scanner(getClass().getResourceAsStream("/maps/map.tmx"));

    //reads the file line by line
    // map starts at line 71
    for (int i = 0; i < 70; i++) {
      s.nextLine();
    }

    //converts the file into the array
    for (int i = 0; i < 60; i++) {

      ArrayList<String> line = new ArrayList<>(Arrays.asList(s.nextLine().split(",")));
      Collections.reverse(line);
      String[] lineArray = new String[60];
      map[i] = line.toArray(lineArray);

    }

    return map;
  }

  /**
   * Sets the
   *
   * @return
   */
  public HashMap<Position, String> getOccupiedCoordinates() {

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
  //only called when the objects are first put into the map.
  public void setCoordinates(Position newPosition, String type) {

    occupiedCoordinates.put(newPosition, type);


  }

  /**
   * @param key
   */
  public void deleteCoordinates(Position key) {

    occupiedCoordinates.remove(key);
  }


  /**
   * @param mapX
   * @param mapY
   */
  private void createCollisionBox(int mapX, int mapY) {

    int mapPositionX = (32*(mapX))+28;
    int mapPositionY = (32*(mapY))+28;

    //adds centre of the element to the occupied
    Position centrePosition = new Position(mapPositionX, mapPositionY);
    setCoordinates(centrePosition, "wall");

    int lowerY = mapPositionX - 14;
    int lowerX = mapPositionY - 14;

    int higherY = mapPositionX + 14;
    int higherX = mapPositionY + 14;

    //positions for bottom of box
    for (int x = 0; x < 29; x++) {

      Position position = new Position(lowerX + x, lowerY);
      setCoordinates(position, "wall");

    }

    //positions for left of box
    for (int y = 0; y < 29; y++) {

      Position position = new Position(lowerX, lowerY + y);
      setCoordinates(position, "wall");

    }

    //positions for top of box
    for (int x = 0; x < 29; x++) {

      Position position = new Position(lowerX + x, higherY);
      setCoordinates(position, "wall");

    }

    //positions for right of box
    for (int y = 0; y < 29; y++) {

      Position position = new Position(higherX, lowerY + y);
      setCoordinates(position, "wall");

    }

  }


}
