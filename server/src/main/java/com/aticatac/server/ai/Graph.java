package com.aticatac.server.ai;

import com.aticatac.server.Position;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * A graph is a collection of connected SearchNodes. Each node in the graph represents a point on the game map that a
 * computer controlled tank can move to.
 *
 * @author Dylan
 */
class Graph {
  /**
   * Position difference between two connected nodes
   */
  private static final int separation = 32;
  /**
   * The nodes that make up the graph
   */
  private final HashMap<String, SearchNode> nodes;
  /**
   * String character representation of the game's map
   */
  private final String[][] map;

  /**
   * Creates a new graph by placing and connecting valid nodes.
   */
  Graph() {
    nodes = new HashMap<>();
    map = convertTMXFileToArray();
    // Add nodes
    int x, y;
    x = 0;
    for (int i = separation; i < (map.length * separation) + separation; i += separation) {
      y = 0;
      for (int j = separation; j < (map.length * separation) + separation; j += separation) {
        if (map[x][y].equals("0")) {
          nodes.put(i + "-" + j, new SearchNode(i, j));
        }
        y++;
      }
      x++;
    }
    // Add connections
    for (SearchNode node : nodes.values()) {
      if (nodes.containsKey(node.getX() + "-" + (node.getY() + separation))) {
        node.addConnection(nodes.get(node.getX() + "-" + (node.getY() + separation)));
      }
      if (nodes.containsKey(node.getX() + "-" + (node.getY() - separation))) {
        node.addConnection(nodes.get(node.getX() + "-" + (node.getY() - separation)));
      }
      if (nodes.containsKey((node.getX() + separation) + "-" + node.getY())) {
        node.addConnection(nodes.get((node.getX() + separation) + "-" + node.getY()));
      }
      if (nodes.containsKey((node.getX() - separation) + "-" + node.getY())) {
        node.addConnection(nodes.get((node.getX() - separation) + "-" + node.getY()));
      }
    }
  }

  /**
   * Gets the node in the graph that has the nearest position to a given position.
   *
   * @param position The position to get nearest node from
   *
   * @return The nearest node to the given position
   */
  SearchNode getNearestNode(Position position) {
    if (nodes.containsKey(32*Math.round((float)position.getX()/32)+"-"+32*Math.round((float)position.getY()/32)))
      return nodes.get(32*Math.round((float)position.getX()/32)+"-"+32*Math.round((float)position.getY()/32));
    SearchNode closestNode = null;
    double distanceToClosestNode = Double.MAX_VALUE;
    for (SearchNode node : nodes.values()) {
      double distanceToTank = Math.sqrt(Math.pow(node.getY() - position.getY(), 2) + Math.pow(node.getX() - position.getX(), 2));
      if (distanceToTank < distanceToClosestNode) {
        closestNode = node;
        distanceToClosestNode = distanceToTank;
      }
    }
    return closestNode;
  }

  /**
   * Gets all the SearchNodes in a specified range centered on a given position
   *
   * @param position Center position to check from
   * @param range    Range of consideration
   *
   * @return All the SearchNodes in range
   */
  ArrayList<SearchNode> getNodesInRange(Position position, int range) {
    ArrayList<SearchNode> inRange = new ArrayList<>();
    SearchNode nodeAt = getNearestNode(position);
    for (int i = nodeAt.getX() - (range - (range % separation)); i < nodeAt.getX() + (range - (range % separation)); i += separation) {
      for (int j = nodeAt.getY() - (range - (range % separation)); j < nodeAt.getY() + (range - (range % separation)); j += separation) {
        if (!(nodeAt.getX() == i && nodeAt.getY() == j) && nodes.containsKey(i + "-" + j)) {
          inRange.add(nodes.get(i + "-" + j));
        }
      }
    }
    return inRange;
  }

  /**
   * Gets a String character representation of the game's map
   *
   * @return A String character representation of the game's map
   */
  String[][] getMap() {
    return map;
  }

  /**
   * Reads the map.tmx file containing a representation of the game's map into a String array.
   *
   * @return A representation of the map as an array
   */
  private String[][] convertTMXFileToArray() {
    Scanner s = new Scanner(getClass().getResourceAsStream("/maps/map.tmx"));
    for (int i = 0; i < 70; i++) // map starts at line 71
      s.nextLine();
    String[][] map = new String[60][60];
    for (int i = 0; i < 60; i++) {
      map[i] = s.nextLine().split(",");
    }
    // Rotate map
    getTranspose(map);
    rotateAlongMidRow(map);
    // Add "1"s on positions adjacent to walls
    removePositionsNextToWalls(map);
    return map;
  }

  private void getTranspose(String[][] map) {
    for (int i = 0; i < map.length; i++) {
      for (int j = i + 1; j < map.length; j++) {
        String temp = map[i][j];
        map[i][j] = map[j][i];
        map[j][i] = temp;
      }
    }
  }

  private void rotateAlongMidRow(String[][] map) {
    for (int i = 0; i < map.length / 2; i++) {
      for (int j = 0; j < map.length; j++) {
        String temp = map[i][j];
        map[i][j] = map[map.length - 1 - i][j];
        map[map.length - 1 - i][j] = temp;
      }
    }
  }

  private void removePositionsNextToWalls(String[][] map) {
    for (int x = 0; x < map.length; x++) {
      for (int y = 0; y < map.length; y++) {
        if (map[x][y].equals("0")) {
          for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
              if (i >= 0 && i < map.length && j >= 0 && j < map.length && map[i][j].equals("2")) {
                map[x][y] = "1";
              }
            }
          }
        }
      }
    }
  }
}
