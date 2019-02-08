package com.aticatac.common.components.ai;

import com.aticatac.common.components.transform.Position;
import com.aticatac.common.model.Command;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A PathFinder constructs a path from a start node to a goal node using A* search
 *
 * @author Dylan
 */
public class PathFinder {
    /*
        Adapted from: https://en.wikipedia.org/wiki/A*_search_algorithm#Pseudocode

        g score = the cost of the path from the start node to the current node
        f score = g + an estimate of the cost from the current node to the goal node
     */

    private int seperation;

    public PathFinder(int seperation) {
        this.seperation = seperation;
    }

    /**
     * Uses A* search to find a path from one node to another.
     *
     * @param start The node to start from
     * @param goal The node to end at
     * @return A queue of commands that define a path from the start node to the goal node
     */
    public Queue<Command> getPathToLocation(SearchNode start, SearchNode goal) {

        LinkedList<SearchNode> closedSet = new LinkedList<SearchNode>();

        LinkedList<SearchNode> openSet = new LinkedList<SearchNode>();
        openSet.add(start);

        HashMap cameFrom = new HashMap<SearchNode, SearchNode>();

        HashMap<SearchNode, Integer> g = new HashMap<SearchNode, Integer>();
        g.put(start, 0);

        HashMap<SearchNode, Double> f = new HashMap<SearchNode, Double>();
        f.put(start, costEstimate(start, goal));

        while (!openSet.isEmpty()) {
            SearchNode current = getLowestFScoreNode(openSet, f);
            if (current.equals(goal)) {
                return convertToCommands(reconstructPath(cameFrom, current));
            }

            openSet.remove(current);
            closedSet.add(current);

            for (SearchNode connectedNode : current.getConnectedNodes()) {
                if (closedSet.contains(connectedNode)) {
                    continue;
                }

                int tempG = g.get(current) + 1;

                if (!openSet.contains(connectedNode)) {
                    openSet.add(connectedNode);
                }
                else if (tempG >= g.get(connectedNode)) {
                    continue;
                }

                cameFrom.put(connectedNode, current);
                g.put(connectedNode, tempG);
                f.put(connectedNode, g.get(connectedNode) + costEstimate(connectedNode, goal));
            }
        }
        return null;
    }

    /**
     * Converts a path of SearchNodes to a queue of Commands that execute the path
     *
     * @param path The path to convert
     * @return A queue of Commands
     */
    private Queue<Command> convertToCommands(LinkedList<SearchNode> path) {
        LinkedList<Command> steps = new LinkedList<Command>();
        for (int i = 1; i < path.size(); i++) {
            Position from = path.get(i - 1).getPosition();
            Position to = path.get(i).getPosition();

            // THESE MIGHT BE WRONG
            if ((from.x - to.x) == seperation) {
                steps.add(Command.RIGHT);
            }
            else if ((from.x - to.x) == -seperation) {
                steps.add(Command.LEFT);
            }
            else if ((from.y - to.y) == seperation) {
                steps.add(Command.UP);
            }
            else if ((from.y - to.y) == -seperation) {
                steps.add(Command.DOWN);
            }
        }
        return steps;
    }

    /**
     * Reconstructs the final path when the goal node is reached.
     *
     * @param cameFrom A mapping from node to node, defining for a node which node was previous
     * @param current The current node
     * @return The final path from the start to goal node
     */
    private LinkedList<SearchNode> reconstructPath(HashMap<SearchNode, SearchNode> cameFrom, SearchNode current) {
        LinkedList<SearchNode> totalPath = new LinkedList<SearchNode>();
        totalPath.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.add(current);
        }
        return totalPath;
    }

    /**
     * Returns the node in the open set with the lowest f score
     *
     * @param openSet The set of open nodes
     * @param f A mapping from search nodes to f score
     * @return The node with the lowest f score from the set of open nodes
     */
    private SearchNode getLowestFScoreNode(LinkedList<SearchNode> openSet, HashMap<SearchNode, Double> f) {
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
     * Calculates a cost estimate for travelling from one node to another using euclidean distance.
     * Used as a heuristic for A* search.
     *
     * @param from Node to start from
     * @param to Node to end on
     * @return An estimate for the cost of travelling from one node to another
     */
    private double costEstimate(SearchNode from, SearchNode to) {
        return Math.sqrt(Math.pow(from.getPosition().y - to.getPosition().y, 2) + Math.pow(from.getPosition().x - to.getPosition().x, 2));
    }

    public Queue<Command> getRandomPath() {
        Queue<Command> path = new LinkedList<Command>();

        // hmmm

        return path;
    }
}
