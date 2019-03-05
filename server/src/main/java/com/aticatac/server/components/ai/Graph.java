package com.aticatac.server.components.ai;

import com.aticatac.common.components.transform.Position;
import com.aticatac.common.model.Command;

import java.util.ArrayList;
import java.util.Queue;

/**
 * A graph is a collection of connected SearchNodes. Each node in the graph represents a point on the game map that a
 * computer controlled tank can move to.
 *
 * @author Dylan
 */
class Graph {
    /**
     * A pathfinder that can generate a path in the graph
     */
    private final PathFinder pf;
    private final int width;
    private final int height;
    private final int separation;
    /**
     * The nodes that make up the graph
     */
    private ArrayList<SearchNode> nodes;

    /**
     * Creates a new graph by placing and connecting valid nodes.
     *
     * @param separation The distance between two connected nodes
     * @param width      The number of nodes wide
     * @param height     The number of nodes high
     */
    Graph(int width, int height, int separation) {
        this.width = width;
        this.height = height;
        this.separation = separation;
        nodes = new ArrayList<SearchNode>();
        pf = new PathFinder(separation);

        // Add nodes
        for (int i = 0; i < width*separation; i += separation) {
            for (int j = 0; j < height*separation; j += separation) {
                nodes.add(new SearchNode(i, j));
            }
        }
        // Add connections
        // don't make connections if connection is invalid (the node thing might be enough though)
        for (SearchNode node : nodes) {
            for (SearchNode otherNode : nodes) {
                if (Math.sqrt(Math.pow(node.getY() - otherNode.getY(), 2) + Math.pow(node.getX() - otherNode.getX(), 2)) == separation) {
                    node.addConnection(otherNode);
                }
            }
        }
    }
    // Makes a graph with excluded nodes
    /*
    public Graph(int width, int height, int separation, char[][] map) {
        this.width = width;
        this.height = height;
        this.separation = separation;
        nodes = new ArrayList<SearchNode>();

        // Add nodes
        for (int i = 0; i < width*separation; i += separation) {
            for (int j = 0; j < height*separation; j += separation) {
                if (map[i][j] != 'w') { // w for wall, or something
                    nodes.add(new SearchNode(i, j));
                }
            }
        }
        // Add connections
        // don't make connections if connection is invalid (the node thing might be enough though)
        for (SearchNode node : nodes) {
            for (SearchNode otherNode : nodes) {
                if (Math.sqrt(Math.pow(node.getY() - otherNode.getY(), 2) + Math.pow(node.getX() - otherNode.getX(), 2)) == separation) {
                    node.addConnection(otherNode);
                }
            }
        }
    }
    */

    /**
     * Uses the pathfinder to generate queue of commands that define a path from one location to another.
     *
     * @param from Start position
     * @param to   Goal position
     * @return A queue of Commands that execute the path
     */
    public Queue<Command> getPathToLocation(Position from, Position to) {
        return pf.getPathToLocation(getNearestNode(from), getNearestNode(to));
    }

    /**
     * Gets the node in the graph that has the nearest position to a given position.
     *
     * @param position The position to get nearest node from
     * @return The nearest node to the given position
     */
    private SearchNode getNearestNode(Position position) {
        SearchNode nearestNode = null;
        double distanceToNearestNode = Double.MAX_VALUE;
        for (SearchNode node : nodes) {
            double distance = Math.sqrt(Math.pow(node.getY() - position.getY(), 2) + Math.pow(node.getX() - position.getX(), 2));
            if (distance < distanceToNearestNode) {
                nearestNode = node;
                distanceToNearestNode = distance;
            }
        }
        return nearestNode;
    }

    /**
     * Gets the width of the graph.
     *
     * @return The width of the graph
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the graph.
     *
     * @return The height of the graph
     */
    public int getHeight() {
        return height;
    }
}
