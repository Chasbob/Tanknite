package com.aticatac.server.components.ai;

import com.aticatac.common.components.transform.Position;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A PathFinder constructs a path from a start node to a goal node using A* search
 *
 * @author Dylan
 */
class PathFinder {
  /*
    Adapted from: https://en.wikipedia.org/wiki/A*_search_algorithm#Pseudocode

    g score = the cost of the path from the start node to the current node
    f score = g + an estimate of the cost from the current node to the goal node
  */
  /**
   * Uses A* search to find a path from one node to another.
   *
   * @param start The node to start from
   * @param goal  The node to end at
   * @return A queue of commands that define a path from the start node to the goal node
   */
  LinkedList<SearchNode> getPathToLocation(SearchNode start, SearchNode goal) {
    LinkedList<SearchNode> closedSet = new LinkedList<>();
    LinkedList<SearchNode> openSet = new LinkedList<>();
    openSet.add(start);
    HashMap<SearchNode, SearchNode> cameFrom = new HashMap<>();
    HashMap<SearchNode, Integer> g = new HashMap<>();
    g.put(start, 0);
    HashMap<SearchNode, Double> f = new HashMap<>();
    f.put(start, manhattanDistance(start, goal));
    while (!openSet.isEmpty()) {
      SearchNode current = getLowestFScoreNode(openSet, f);
      if (current.equals(goal)) {
        return reconstructPath(cameFrom, current);
      }
      openSet.remove(current);
      closedSet.add(current);
      for (SearchNode connectedNode : current.getConnectedNodes()) {
        if (closedSet.contains(connectedNode)) {
          continue;
        }
        int tempG = g.get(current) + (int)manhattanDistance(connectedNode, current);
        if (!openSet.contains(connectedNode)) {
          openSet.add(connectedNode);
        } else if (tempG >= g.get(connectedNode)) {
          continue;
        }
        cameFrom.put(connectedNode, current);
        g.put(connectedNode, tempG);
        f.put(connectedNode, g.get(connectedNode) + manhattanDistance(connectedNode, goal));
      }
    }
    return null;
  }

  /**
   * Reconstructs the final path when the goal node is reached.
   *
   * @param cameFrom A mapping from node to node, defining for a node which node was previous
   * @param current  The current node
   * @return The final path from the start to goal node
   */
  private LinkedList<SearchNode> reconstructPath(HashMap<SearchNode, SearchNode> cameFrom, SearchNode current) {
    LinkedList<SearchNode> totalPath = new LinkedList<>();
    totalPath.add(current);
    while (cameFrom.containsKey(current)) {
      current = cameFrom.get(current);
      totalPath.add(current);
    }
    Collections.reverse(totalPath);
    return totalPath;
  }

  /**
   * Returns the node in the open set with the lowest f score
   *
   * @param openSet The set of open nodes
   * @param f       A mapping from search nodes to f score
   * @return The node with the lowest f score from the set of open nodes
   */
  SearchNode getLowestFScoreNode(LinkedList<SearchNode> openSet, HashMap<SearchNode, Double> f) {
    double lowestScore = Double.MAX_VALUE;
    SearchNode lowestScoreNode = openSet.get(0);
    for (SearchNode node : openSet) {
      if (f.get(node) < lowestScore) {
        lowestScore = f.get(node);
        lowestScoreNode = node;
      }
    }
    return lowestScoreNode;
  }

  /**
   * Calculates euclidean distance between two points. Used as a heuristic for A* search.
   *
   * @param from Start position
   * @param to   End position
   * @return Euclidean distance between two points
   */
  double euclideanDistance(Position from, Position to) {
    return Math.sqrt(Math.pow(from.getY() - to.getY(), 2) + Math.pow(from.getX() - to.getX(), 2));
  }

  /**
   * Calculates manhattan distance between two points.
   *
   * @param from Start position
   * @param to   End position
   * @return Manhattan distance between two points
   */
  double manhattanDistance(Position from, Position to) {
    return Math.abs(from.getX() - to.getX()) + Math.abs(from.getY() - to.getY());
  }
}
