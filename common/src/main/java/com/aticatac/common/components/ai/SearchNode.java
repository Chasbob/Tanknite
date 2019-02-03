package com.aticatac.common.components.ai;

import com.aticatac.common.components.transform.Position;

import java.util.ArrayList;

public class SearchNode {
    private Position position;
    private ArrayList<SearchNode> connectedNodes;

    public SearchNode(int x, int y) {
        setPosition(new Position(x, y));
        connectedNodes = new ArrayList<SearchNode>();
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public ArrayList<SearchNode> getConnectedNodes() {
        return connectedNodes;
    }

    public void addConnection(SearchNode node) {
        connectedNodes.add(node);
    }
}