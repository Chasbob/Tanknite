package com.aticatac.server.components.ai;

import com.aticatac.common.components.transform.Position;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
// Things left TODO:
//  - not placing nodes at wrong places

/**
 * A graph is a collection of connected SearchNodes. Each node in the graph represents a point on the game map that a
 * computer controlled tank can move to.
 *
 * @author Dylan
 */
public class Graph {
  /**
   * A pathfinder that can generate a path in the graph
   */
  private final PathFinder pf = new PathFinder();
  /**
   * The nodes that make up the graph
   */
  private final HashMap<String, SearchNode> nodes;
  private final int separation;

  /**
   * Creates a new graph by placing and connecting valid nodes.
   *
   * @param separation The distance between two connected nodes
   * @param width      The number of nodes wide
   * @param height     The number of nodes high
   * @param xOffset    The x position offset
   * @param yOffset    The y position offset
   */
  public Graph(int width, int height, int separation, int xOffset, int yOffset, int[][] map) {
    // Set attributes
    nodes = new HashMap<>();
    this.separation = separation;
    // Add nodes
    int x, y;
    x = 0;
    for (int i = xOffset; i < width * separation + xOffset; i += separation) {
      y = 0;
      for (int j = yOffset; j < height * separation + yOffset; j += separation) {
        if (map[x][y] == 0 /* TODO space is not a wall */) {
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

  public Graph(int width, int height, int separation, int xOffset, int yOffset){
    // Set attributes
    nodes = new HashMap<>();
    this.separation = separation;
    // Add nodes
    for (int i = xOffset; i < width * separation + xOffset; i += separation) {
      for (int j = yOffset; j < height * separation + yOffset; j += separation) {
        nodes.put(i + "-" + j, new SearchNode(i, j));
      }
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
  private SearchNode getNearestNode(Position position) {
    SearchNode start = nodes.entrySet().iterator().next().getValue();
    LinkedList<SearchNode> closedSet = new LinkedList<>();
    LinkedList<SearchNode> openSet = new LinkedList<>();
    openSet.add(start);
    HashMap<SearchNode, Integer> g = new HashMap<>();
    g.put(start, 0);
    HashMap<SearchNode, Double> f = new HashMap<>();
    f.put(start, Math.sqrt(Math.pow(start.getY() - position.getY(),2) + Math.pow(start.getX() - position.getX(),2)));
    while (!openSet.isEmpty()) {
      SearchNode current = pf.getLowestFScoreNode(openSet, f);
      if (Math.sqrt(Math.pow(current.getY() - position.getY(),2) + Math.pow(current.getX() - position.getX(),2)) < separation) {
        return current;
      }
      openSet.remove(current);
      closedSet.add(current);
      for (SearchNode connectedNode : current.getConnectedNodes()) {
        if (closedSet.contains(connectedNode)) {
          continue;
        }
        int tempG = g.get(current) + (Math.abs(connectedNode.getX() - current.getX()) + Math.abs(connectedNode.getY() - current.getY()));
        if (!openSet.contains(connectedNode)) {
          openSet.add(connectedNode);
        } else if (tempG >= g.get(connectedNode)) {
          continue;
        }
        g.put(connectedNode, tempG);
        f.put(connectedNode, g.get(connectedNode) + Math.sqrt(Math.pow(connectedNode.getY() - position.getY(),2) + Math.pow(connectedNode.getX() - position.getX(),2)));
      }
    }
    return null;
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
}
