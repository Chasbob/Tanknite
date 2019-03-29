package com.aticatac.client.server.ai;

import com.aticatac.client.server.Position;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A SearchNode is a node in the graph representing the game's map. Path finding using A* search is performed on a graph
 * of SearchNodes.
 *
 * @author Dylan
 */
class SearchNode extends Position {
  /**
   * All the SearchNodes connected to this SearchNode
   */
  private ArrayList<SearchNode> connectedNodes;

  /**
   * Creates a new SearchNode at (x, y).
   *
   * @param x X co-ordinate position of this SearchNode
   * @param y Y co-ordinate position of this SearchNode
   */
  SearchNode(int x, int y) {
    super(x, y);
    connectedNodes = new ArrayList<>();
  }

  /**
   * Gets a list of SearchNodes that this SearchNode is connected to.
   *
   * @return An ArrayList of the connected SearchNodes
   */
  ArrayList<SearchNode> getConnectedNodes() {
    return connectedNodes;
  }

  /**
   * Gets the nodes of a sub graph of specified depth.
   *
   * @param depth the sub graph depth
   * @return The nodes of a sub graph
   */
  Set<SearchNode> getSubGraph(int depth) {
    HashSet<SearchNode> subNodes = new HashSet<>();
    subNodes.add(this);
    if (depth == 1) {
      return subNodes;
    }
    for (SearchNode connectedNode : getConnectedNodes()) {
      subNodes.addAll(connectedNode.getSubGraph(depth - 1));
    }
    return subNodes;
  }

  /**
   * Adds a connection to another SearchNode.
   *
   * @param node The SearchNode to connect to
   */
  void addConnection(SearchNode node) {
    connectedNodes.add(node);
  }


}