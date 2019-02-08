package com.aticatac.common.components.ai;

import com.aticatac.common.components.transform.Position;
import java.util.ArrayList;

public class Graph {
    private ArrayList<SearchNode> nodes;

    public Graph(int seperation, int width, int height) {
        constructGraph(seperation, width, height);
    }

    public void constructGraph(int seperation, int numberHorizontal, int numberVertical) {
        nodes = new ArrayList<SearchNode>();

        // Add nodes
        // TODO: don't place a node if position is invalid
        for (int i = 0; i < numberHorizontal*seperation; i += seperation) {
            for (int j = 0; j < numberVertical*seperation; j += seperation) {
                nodes.add(new SearchNode(i, j));
            }
        }
        // Add connections
        for (SearchNode node : nodes) {
            for (SearchNode otherNode : nodes) {
                if (Math.sqrt(Math.pow(node.getPosition().y - otherNode.getPosition().y, 2) + Math.pow(node.getPosition().x - otherNode.getPosition().x, 2)) == seperation) {
                    node.addConnection(otherNode);
                }
            }
        }
    }

    public SearchNode getNearestNode(Position position) {
        SearchNode nearestNode = null;
        double distanceToNearestNode = Double.MAX_VALUE;
        for (SearchNode node : nodes) {
            double distance = Math.sqrt(Math.pow(node.getPosition().y - position.y, 2) + Math.pow(node.getPosition().x - position.x, 2));
            if (distance < distanceToNearestNode) {
                nearestNode = node;
                distanceToNearestNode = distance;
            }
        }
        return nearestNode;
    }
}
