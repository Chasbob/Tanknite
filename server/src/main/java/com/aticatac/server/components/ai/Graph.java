package com.aticatac.server.components.ai;

import com.aticatac.common.components.transform.Position;
import com.aticatac.common.objectsystem.GameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

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
    private HashMap<Position, SearchNode> nodes;

    /**
     * Creates a new graph by placing and connecting valid nodes.
     *
     * @param separation The distance between two connected nodes
     * @param width      The number of nodes wide
     * @param height     The number of nodes high
     * @param xOffset    The x position offset
     * @param yOffset    The y position offset
     */
    public Graph(int width, int height, int separation, double xOffset, double yOffset) {
        nodes = new HashMap<>();

        // Add nodes
        for (double i = 0 + xOffset; i < width*separation + xOffset; i += separation) {
            for (double j = 0 + yOffset; j < height*separation + yOffset; j += separation) {
                if (true /* TODO space is not a wall */)
                    nodes.put(new Position(i, j), new SearchNode(i, j));
            }
        }
        // Add connections
        for (SearchNode node : nodes.values()) {
            if (node.getY() < height*separation + yOffset - separation)
                node.addConnection(nodes.get(new Position(node.getX(), node.getY() + separation)));
            if (node.getY() > yOffset)
                node.addConnection(nodes.get(new Position(node.getX(), node.getY() - separation)));
            if (node.getX() < width*separation + xOffset - separation)
                node.addConnection(nodes.get(new Position(node.getX() + separation, node.getY())));
            if (node.getX() > xOffset)
                node.addConnection(nodes.get(new Position(node.getX() - separation, node.getY())));
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
        SearchNode nearestNode = null;
        double distanceToNearestNode = Double.MAX_VALUE;
        for (SearchNode node : nodes.values()) {
            double distance = Math.sqrt(Math.pow(node.getY() - position.getY(), 2) + Math.pow(node.getX() - position.getX(), 2));
            if (distance < distanceToNearestNode) {
                nearestNode = node;
                distanceToNearestNode = distance;
            }
        }
        return nearestNode;
    }

    /**
     * Gets all the SearchNodes in a specified range centered on a given position
     *
     * @param position  Center position to check from
     * @param range     Range of consideration
     * @return All the SearchNodes in range
     */
    public ArrayList<SearchNode> getNodesInRange(Position position, int range) {
        ArrayList<SearchNode> inRange = new ArrayList<SearchNode>();
        for (SearchNode node : nodes.values()) {
            if (Math.abs(node.getX() - position.getX()) <= range ||
                    Math.abs(node.getY() - position.getY()) <= range) {
                inRange.add(node);
            }
        }
        return inRange;
    }

}
