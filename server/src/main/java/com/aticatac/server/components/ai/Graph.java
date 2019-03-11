package com.aticatac.server.components.ai;

import com.aticatac.common.components.transform.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Scanner;

/**
 * A graph is a collection of connected SearchNodes. Each node in the graph represents a point on the game map that a
 * computer controlled tank can move to.
 *
 * @author Dylan
 */
class Graph {
  private static final int separation = 32;
  /**
   * A pathfinder that can generate a path in the graph
   */
  private final PathFinder pf;
  /**
   * The nodes that make up the graph
   */
  private final HashMap<String, SearchNode> nodes;
  private final String[][] map;

  /**
   * Creates a new graph by placing and connecting valid nodes.
   */
  Graph() {
    pf = new PathFinder();
    nodes = new HashMap<>();
    // Add nodes
    map = convertTMXFileToIntArray();
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

  private static void removePositionsNextToWalls(String[][] map) {
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

  private static void rotateMap(String[][] map) {
    if (map == null) {
      return;
    }
    if (map.length != map[0].length)//INVALID INPUT
    {
      return;
    }
    getTranspose(map);
    rotateAlongMidRow(map);
  }

  private static void getTranspose(String[][] map) {
    for (int i = 0; i < map.length; i++) {
      for (int j = i + 1; j < map.length; j++) {
        String temp = map[i][j];
        map[i][j] = map[j][i];
        map[j][i] = temp;
      }
    }
  }

  private static void rotateAlongMidRow(String[][] map) {
    int len = map.length;
    for (int i = 0; i < len / 2; i++) {
      for (int j = 0; j < len; j++) {
        String temp = map[i][j];
        map[i][j] = map[len - 1 - i][j];
        map[len - 1 - i][j] = temp;
      }
    }
  }

  /**
   * Uses the pathfinder to generate queue of commands that define a path from one location to another.
   *
   * @param from Start position
   * @param to   Goal position
   * @return A queue of Commands that execute the path
   */
  Queue<SearchNode> getPathToLocation(Position from, Position to) {
    return pf.getPathToLocation(getNearestNode(from), getNearestNode(to));
  }

  /**
   * Gets the node in the graph that has the nearest position to a given position.
   *
   * @param position The position to get nearest node from
   * @return The nearest node to the given position
   */
  SearchNode getNearestNode(Position position) {
    SearchNode closestNode = null;
    double distanceToClosestNode = Double.MAX_VALUE;
    for (SearchNode node : nodes.values()) {
      double distanceToTank = pf.euclideanDistance(node, position);
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

  String[][] getMap() {
    return map;
  }

  private String[][] convertTMXFileToIntArray() {
    Scanner s = new Scanner(getClass().getResourceAsStream("/maps/map.tmx"));
    for (int i = 0; i < 70; i++) // map starts at line 71
      s.nextLine();
    String[][] map = new String[60][60];
    for (int i = 0; i < 60; i++) {
      map[i] = s.nextLine().split(",");
    }
    rotateMap(map);
    removePositionsNextToWalls(map);
    return map;
  }
}
