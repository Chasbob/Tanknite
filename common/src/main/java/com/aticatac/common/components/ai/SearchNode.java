package com.aticatac.common.components.ai;

import com.aticatac.common.components.transform.Position;

import java.util.ArrayList;

/**
 * A SearchNode is a node in the graph representing the game's map.
 * Path finding using A* search is performed on a graph of SearchNodes.
 *
 * @author Dylan
 */
public class SearchNode {
    /** The position of this SearchNode */
    private Position position;
    /** All the SearchNodes connected to this SearchNode */
    private ArrayList<SearchNode> connectedNodes;

    /**
     * Creates a new SearchNode at (x, y).
     *
     * @param x X co-ordinate position of this SearchNode
     * @param y Y co-ordinate position of this SearchNode
     */
    public SearchNode(int x, int y) {
        setPosition(new Position(x, y));
        connectedNodes = new ArrayList<SearchNode>();
    }

    /**
     * Gets the position of this SearchNode.
     *
     * @return Position of this SearchNode
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the position of this SearchNode.
     *
     * @param position New position of this SearchNode
     */
    public void setPosition(Position position) {
        this.position = position;
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