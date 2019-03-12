package com.aticatac.server.components.ai;

import com.aticatac.common.components.transform.Position;
import java.util.ArrayList;

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
    connectedNodes = new ArrayList<SearchNode>();
  }

  /**
   * Gets a list of SearchNodes that this SearchNode is connected to.
   *
   * @return An ArrayList of the connected SearchNodes
   */
  public ArrayList<SearchNode> getConnectedNodes() {
    return connectedNodes;
  }

  /**
   * Adds a connection to another SearchNode.
   *
   * @param node The SearchNode to connect to
   */
  public void addConnection(SearchNode node) {
    connectedNodes.add(node);
  }
}