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
  /**
   * Physical attributes of the graph
   */
  private final int xStart;
  private final int xEnd;
  private final int yStart;
  private final int yEnd;
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
  public Graph(int width, int height, int separation, int xOffset, int yOffset) {
    // Set attributes
    nodes = new HashMap<>();
    this.separation = separation;
    xStart = xOffset;
    xEnd = width * separation + xOffset;
    yStart = yOffset;
    yEnd = height * separation + yOffset;
    // Add nodes
    for (int i = xStart; i < xEnd; i += separation) {
      for (int j = yStart; j < yEnd; j += separation) {
        if (true /* TODO space is not a wall */) {
          nodes.put(i + "-" + j, new SearchNode(i, j));
        }
      }
    }
    // Add connections
    for (SearchNode node : nodes.values()) {
      if (node.getY() < yEnd - separation) {
        node.addConnection(nodes.get(node.getX() + "-" + (node.getY() + separation)));
      }
      if (node.getY() > yStart) {
        node.addConnection(nodes.get(node.getX() + "-" + (node.getY() - separation)));
      }
      if (node.getX() < xEnd - separation) {
        node.addConnection(nodes.get((node.getX() + separation) + "-" + node.getY()));
      }
      if (node.getX() > xStart) {
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
  public Queue<SearchNode> getPathToLocation(Position from, Position to) {
    return pf.getPathToLocation(getNearestNode(from), getNearestNode(to));
  }

  /**
   * Gets the node in the graph that has the nearest position to a given position.
   *
   * @param position The position to get nearest node from
   * @return The nearest node to the given position
   */
  public SearchNode getNearestNode(Position position) {
    //hahaha
    SearchNode start = nodes.get(xStart + "-" + yStart);
    LinkedList<SearchNode> closedSet = new LinkedList<SearchNode>();
    LinkedList<SearchNode> openSet = new LinkedList<SearchNode>();
    openSet.add(start);
    HashMap cameFrom = new HashMap<SearchNode, SearchNode>();
    HashMap<SearchNode, Integer> g = new HashMap<SearchNode, Integer>();
    g.put(start, 0);
    HashMap<SearchNode, Double> f = new HashMap<SearchNode, Double>();
    f.put(start, Math.pow(start.getY() - position.getY(), 2) + Math.pow(start.getX() - position.getX(), 2));
    while (!openSet.isEmpty()) {
      SearchNode current = pf.getLowestFScoreNode(openSet, f);
      if (Math.sqrt(Math.pow(current.getY() - position.getY(), 2) + Math.pow(current.getX() - position.getX(), 2)) < separation) {
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
        cameFrom.put(connectedNode, current);
        g.put(connectedNode, tempG);
        f.put(connectedNode, g.get(connectedNode) + Math.sqrt(Math.pow(connectedNode.getY() - position.getY(), 2) + Math.pow(connectedNode.getX() - position.getX(), 2)));
      }
    }
    return null;
//        SearchNode nearestNode = null;
//        double distanceToNearestNode = Double.MAX_VALUE;
//        for (SearchNode node : nodes.values()) {
//            double distance = Math.sqrt(Math.pow(node.getY() - position.getY(), 2) + Math.pow(node.getX() - position.getX(), 2));
//            if (distance < distanceToNearestNode) {
//                nearestNode = node;
//                distanceToNearestNode = distance;
//            }
//            if (distanceToNearestNode < separation)
//                return nearestNode;
//        }
//        return nearestNode;
  }

  /**
   * Gets all the SearchNodes in a specified range centered on a given position
   *
   * @param position Center position to check from
   * @param range    Range of consideration
   * @return All the SearchNodes in range
   */
  public ArrayList<SearchNode> getNodesInRange(Position position, int range) {
    ArrayList<SearchNode> inRange = new ArrayList<>();
    SearchNode nodeAt = getNearestNode(position);
    for (int i = nodeAt.getX() - (range - (range % separation)); i < nodeAt.getX() + (range - (range % separation)); i += separation) {
      for (int j = nodeAt.getY() - (range - (range % separation)); j < nodeAt.getY() + (range - (range % separation)); j += separation) {
        if (i >= xStart && i < xEnd && j >= yStart && j < yEnd && !(nodeAt.getX() == i && nodeAt.getY() == j) && nodes.containsKey(i + "-" + j)) {
          inRange.add(nodes.get(i + "-" + j));
        }
      }
    }
    return inRange;
  }
}
