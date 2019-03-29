package com.aticatac.client.server.ai;

import com.aticatac.client.server.Position;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

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
    HashMap<SearchNode, Integer> f = new HashMap<>();
    f.put(start, getCostEstimate(start, goal));
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
        int tempG = g.get(current) + getCostEstimate(connectedNode, current); // cost estimate = real distance
        if (!openSet.contains(connectedNode)) {
          openSet.add(connectedNode);
        } else if (tempG >= g.get(connectedNode)) {
          continue;
        }
        cameFrom.put(connectedNode, current);
        g.put(connectedNode, tempG);
        f.put(connectedNode, g.get(connectedNode) + getCostEstimate(connectedNode, goal));
      }
    }
    return new LinkedList<>();
  }

  /**
   * Reconstructs the final path when the goal node is reached.
   *
   * @param cameFrom A mapping from node to node, defining for a node which node was previous
   * @param current  The current node
   *
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
   *
   * @return The node with the lowest f score from the set of open nodes
   */
  private SearchNode getLowestFScoreNode(LinkedList<SearchNode> openSet, HashMap<SearchNode, Integer> f) {
    int lowestScore = Integer.MAX_VALUE;
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
   * Returns a cost estimate (manhattan distance) for travelling from one node to another. Used as a heuristic for A* ('h').
   *
   * @param from Node to travel from
   * @param to   Node to travel to
   *
   * @return Cost estimate to travel from the one node to the other
   */
  private int getCostEstimate(SearchNode from, SearchNode to) {
    return Math.abs(from.getX() - to.getX()) + Math.abs(from.getY() - to.getY());
  }
}
