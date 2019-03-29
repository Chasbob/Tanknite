package com.aticatac.client.server.ai;

import com.aticatac.client.server.Position;
import com.aticatac.client.server.objectsystem.DataServer;
import java.util.ArrayList;
import java.util.HashMap;

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
   * Creates a new graph by placing and connecting valid nodes.
   */
  Graph() {
    nodes = new HashMap<>();
    String[][] map = DataServer.INSTANCE.getMap();
    removePositionsNextToWalls(map);
    for (int i = 0; i < 60; i++) {
      for (int j = 0; j < 60; j++) {
        System.out.print(map[i][j]);
      }
      System.out.println();
    }
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
   * @return The nearest node to the given position
   */
  SearchNode getNearestNode(Position position) {
    if (nodes.containsKey(32 * Math.round((float) position.getX() / 32) + "-" + 32 * Math.round((float) position.getY() / 32))) {
      return nodes.get(32 * Math.round((float) position.getX() / 32) + "-" + 32 * Math.round((float) position.getY() / 32));
    }
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
   * Removes positions adjacent to walls in the map array.
   *
   * @param map The map to change
   */
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
