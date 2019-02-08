package com.aticatac.common.components.ai;

import java.util.Random;
import com.aticatac.common.model.Command;


public class TestAStarTheSequel {

    public static void main(String[] args) {
        int seperation = 1;
        Graph graph = new Graph(seperation, 10, 10);

        Random rand = new Random();
        SearchNode node1 = graph.getNodes().get(rand.nextInt(100));
        SearchNode node2 = graph.getNodes().get(rand.nextInt(100));

        System.out.println("Start: " + node1.getPosition().x + " | " +node1.getPosition().y);
        System.out.println("Goal: " + node2.getPosition().x + " | " +node2.getPosition().y);

        PathFinder pf = new PathFinder(seperation);

        for (Command c : pf.getPathToLocation(node1, node2)) {
            System.out.println(c.toString());
        }
    }
}
