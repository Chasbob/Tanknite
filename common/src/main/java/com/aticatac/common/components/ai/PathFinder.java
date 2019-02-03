package com.aticatac.common.components.ai;

import com.aticatac.common.model.Command;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;


public class PathFinder {

    /*
        Uses A* search to construct a path from one start node to a goal node.
        Reference used: https://en.wikipedia.org/wiki/A*_search_algorithm#Pseudocode
     */

    private Queue<SearchNode> reconstructPath(HashMap<SearchNode, SearchNode> cameFrom, SearchNode current) {
        LinkedList<SearchNode> totalPath = new LinkedList<SearchNode>();
        totalPath.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.add(current);
        }
        return totalPath;
    }

    public Queue<SearchNode> getPathToLocation(SearchNode start, SearchNode goal) {

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
                return reconstructPath(cameFrom, current);
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

    private double costEstimate(SearchNode from, SearchNode to) {
        return Math.sqrt(Math.pow(from.getPosition().y - to.getPosition().y, 2) + Math.pow(from.getPosition().x - to.getPosition().x, 2));
    }

    public Queue<Command> getRandomPath() {
        Queue<Command> path = new LinkedList<Command>();



        return path;
    }
}
