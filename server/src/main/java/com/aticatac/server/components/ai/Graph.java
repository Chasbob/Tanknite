package com.aticatac.server.components.ai;

import com.aticatac.common.components.transform.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
// Things left TODO:
//  - not placing nodes at wrong places

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
  private final HashMap<String, SearchNode> nodes;
  private final int separation = 32;

  /**
   * Creates a new graph by placing and connecting valid nodes.
   *
   */
  public Graph() {
    // Set attributes
    nodes = new HashMap<>();
    // Add nodes
    String[][] map;
    try {
       map = convertTMXFileToIntArray();
    } catch (FileNotFoundException e) {
      System.out.println("Yo where'd the file go");
      return;
    }
    int x, y;
    x = 0;
    for (int i = separation; i < (map.length * separation) + separation; i += separation) {
      y = 0;
      for (int j = separation; j < (map.length * separation) + separation; j += separation) {
        if (map[x][y].equals("0")) {
          nodes.put(i + "-" + j, new SearchNode(i, j));
        }
        y++;
      }
      x++;
    }
    // Add connections
    for (SearchNode node : nodes.values()) {
      if (nodes.containsKey(node.getX() + "-" + (node.getY() + separation))) {
        node.addConnection(nodes.get(node.getX() + "-" + (node.getY() + separation)));
      }
      if (nodes.containsKey(node.getX() + "-" + (node.getY() - separation))) {
        node.addConnection(nodes.get(node.getX() + "-" + (node.getY() - separation)));
      }
      if (nodes.containsKey((node.getX() + separation) + "-" + node.getY())) {
        node.addConnection(nodes.get((node.getX() + separation) + "-" + node.getY()));
      }
      if (nodes.containsKey((node.getX() - separation) + "-" + node.getY())) {
        node.addConnection(nodes.get((node.getX() - separation) + "-" + node.getY()));
      }
    }
  }

  /**
   * Uses the pathfinder to generate queue of commands that define a path from one location to another.
   *
   * @param from Start position
   * @param to   Goal position
   * @return A queue of Commands that execute the path
   */
  Queue<SearchNode> getPathToLocation(Position from, Position to) {
    //return pf.postProcessPath(pf.getPathToLocation(getNearestNode(from), getNearestNode(to)), nodes);
    return pf.getPathToLocation(getNearestNode(from), getNearestNode(to));
  }

  /**
   * Gets the node in the graph that has the nearest position to a given position.
   *
   * @param position The position to get nearest node from
   * @return The nearest node to the given position
   */
  private SearchNode getNearestNode(Position position) {
    SearchNode start = nodes.entrySet().iterator().next().getValue();
    LinkedList<SearchNode> closedSet = new LinkedList<>();
    LinkedList<SearchNode> openSet = new LinkedList<>();
    openSet.add(start);
    HashMap<SearchNode, Integer> g = new HashMap<>();
    g.put(start, 0);
    HashMap<SearchNode, Double> f = new HashMap<>();
    f.put(start, pf.euclideanDistance(start, position));
    while (!openSet.isEmpty()) {
      SearchNode current = pf.getLowestFScoreNode(openSet, f);
      if (pf.euclideanDistance(current, position) < separation) {
        return current;
      }
      openSet.remove(current);
      closedSet.add(current);
      for (SearchNode connectedNode : current.getConnectedNodes()) {
        if (closedSet.contains(connectedNode)) {
          continue;
        }
        int tempG = g.get(current) + pf.manhattanDistance(connectedNode, current);
        if (!openSet.contains(connectedNode)) {
          openSet.add(connectedNode);
        } else if (tempG >= g.get(connectedNode)) {
          continue;
        }
        g.put(connectedNode, tempG);
        f.put(connectedNode, g.get(connectedNode) + pf.euclideanDistance(connectedNode, position));
      }
    }
    return null;
  }

  /**
   * Gets all the SearchNodes in a specified range centered on a given position
   *
   * @param position Center position to check from
   * @param range    Range of consideration
   * @return All the SearchNodes in range
   */
  ArrayList<SearchNode> getNodesInRange(Position position, int range) {
    ArrayList<SearchNode> inRange = new ArrayList<>();
    SearchNode nodeAt = getNearestNode(position);
    for (int i = nodeAt.getX() - (range - (range % separation)); i < nodeAt.getX() + (range - (range % separation)); i += separation) {
      for (int j = nodeAt.getY() - (range - (range % separation)); j < nodeAt.getY() + (range - (range % separation)); j += separation) {
        if (!(nodeAt.getX() == i && nodeAt.getY() == j) && nodes.containsKey(i + "-" + j)) {
          inRange.add(nodes.get(i + "-" + j));
        }
      }
    }
    return inRange;
  }

  private String[][] convertTMXFileToIntArray() throws FileNotFoundException {
    Scanner s = new Scanner(new File("C:\\Users\\dylan\\IdeaProjects\\aticatac\\client\\src\\main\\resources\\maps\\map.tmx"));
    for (int i = 0; i < 70; i++) // map starts at line 71
      s.nextLine();
    String[][] map = new String[60][60];
    for (int i = 0; i < 60; i++) {
      map[i] = s.nextLine().split(",");
    }
    rotateMatrix(map);
    removeNearWallPositions(map);
    return map;
  }

  private static void removeNearWallPositions(String[][] map) {
    for (int x = 0; x < map.length; x++) {
      for (int y = 0; y < map.length; y++) {
        if (map[x][y].equals("0")) {
          for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
              if (i >= 0 && i < map.length && j >= 0 && j < map.length && map[i][j].equals("2")) {
                map[x][y] = "1";
              }
            }
          }
        }
      }
    }
  }

  private static void rotateMatrix(String[][] matrix){
    if(matrix == null)
      return;
    if(matrix.length != matrix[0].length)//INVALID INPUT
      return;
    getTranspose(matrix);
    rorateAlongMidRow(matrix);
  }

  private static void getTranspose(String[][] matrix) {
    for(int i = 0; i < matrix.length; i++){
      for(int j = i+1; j < matrix.length ; j++){
        String temp = matrix[i][j];
        matrix[i][j] = matrix[j][i];
        matrix[j][i] = temp;
      }
    }
  }

  private static void rorateAlongMidRow(String[][] matrix) {
    int len = matrix.length ;
    for(int i = 0; i < len/2; i++){
      for(int j = 0;j < len; j++){
        String temp = matrix[i][j];
        matrix[i][j] = matrix[len-1 -i][j];
        matrix[len -1 -i][j] = temp;
      }
    }
  }
}
