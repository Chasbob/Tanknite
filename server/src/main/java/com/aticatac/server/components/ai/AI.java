package com.aticatac.server.components.ai;

import com.aticatac.common.components.Ammo;
import com.aticatac.common.components.Component;
import com.aticatac.common.components.Health;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.model.Command;
import com.aticatac.common.objectsystem.GameObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
// Things left TODO:
//  - line of sight
//  - powerup stuff
//  - getting information, all enemies, all powerups

/**
 * AI.
 *
 * @author Dylan
 */
public class AI extends Component {
  private final static int VIEW_RANGE = 500; // some value equivalent to the actual view range that a player would have
  private final GameObject tank;
  private final Graph graph;
  private final double aggression; // (0.5 to 1.5) higher = more likely to attack less likely to flee
  private final double collectiveness; // (0.5 to 1.5) higher = more likely to collect powerup
  private State state;
  private State prevState;
  private Queue<SearchNode> searchPath; // current path being executed
  private ArrayList<GameObject> enemiesInRange;
  private ArrayList<GameObject> powerupsInRange;
  //private Something idealPowerup;
  private Position tankPos;
  private int tankHealth;
  private int tankAmmo;
  private int aimAngle;
  private boolean aimed;

  /**
   * Instantiates a new Ai.
   *
   * @param parent the parent
   */
  public AI(GameObject parent) {
    super(parent);
    this.tank = parent;
    this.graph = new Graph(10, 10, 10, 0, 0);
    this.state = State.SEARCHING;
    this.prevState = State.SEARCHING;
    this.searchPath = new LinkedList<>();
    this.aggression = (double) Math.round((0.5 + Math.random()) * 10) / 10;
    this.collectiveness = (double) Math.round((0.5 + Math.random()) * 10) / 10;
    this.aimAngle = 0; // or whichever direction the tank faces at start
    this.aimed = false;
  }

  /**
   * Returns a decision to control the tank.
   *
   * @return A decision
   */
  public Decision getDecision() {
    // Update information
    tankPos = tank.getComponent(Transform.class).getPosition();
    tankHealth = tank.getComponent(Health.class).getHealth();
    tankAmmo = tank.getComponent(Ammo.class).getAmmo();
    enemiesInRange = getEnemiesInRange(tankPos, VIEW_RANGE);
    powerupsInRange = getPowerupsInRange(tankPos);
    // Change aim angle
    aimed = false;
    int angleChange = getAngleChange();
    aimAngle += angleChange;
    // Poll search path if close enough to node
    double threshold = 3;
    if (!searchPath.isEmpty()) {
      if (Math.abs(tankPos.getX() - searchPath.peek().getX()) < threshold && Math.abs(tankPos.getY() - searchPath.peek().getY()) < threshold) {
        searchPath.poll();
      }
    }
    // Check for a state change
    state = getStateChange();
    // Return a decision
    return new Decision(performStateAction(), angleChange);
  }

  /**
   * Returns the state with the highest utility score.
   *
   * @return The state with the highest utility score
   */
  private State getStateChange() {
    prevState = state;
    // Get utility score for each state
    int searchingUtility = getSearchingUtility();
    int attackingUtility = getAttackingUtility();
    int fleeingUtility = getFleeingUtility();
    int obtainingUtility = getObtainingUtility();
    // Return state with highest utility
    int maxUtility = Math.max(Math.max(searchingUtility, attackingUtility), Math.max(fleeingUtility, obtainingUtility));
    if (maxUtility == searchingUtility) {
      return State.SEARCHING;
    }
    if (maxUtility == attackingUtility) {
      return State.ATTACKING;
    }
    if (maxUtility == fleeingUtility) {
      return State.FLEEING;
    }
    if (maxUtility == obtainingUtility) {
      return State.OBTAINING;
    }
    return State.SEARCHING;
  }

  /**
   * Gets the utility score for the SEARCHING state.
   *
   * @return The utility score for the SEARCHING state
   */
  private int getSearchingUtility() {
    return 30;
  }

  /**
   * Gets the utility score for the ATTACKING state.
   *
   * @return The utility score for the ATTACKING state
   */
  private int getAttackingUtility() {
    if (tankAmmo == 0) {
      // Can't attack
      return 0;
    }
    if (!enemiesInRange.isEmpty()) {
      // An enemy is in range
      return (int) Math.round(80 * aggression);
    }
    return 0;
  }

  /**
   * Gets the utility score for the FLEEING state.
   *
   * @return The utility score for the FLEEING state
   */
  private int getFleeingUtility() {
    if (enemiesInRange.isEmpty() || tankHealth <= 10) {
      // Nothing to flee from || can't move -> can't flee
      return 0;
    }
    if (tankAmmo == 0) {
      // Enemies near and tank has no ammo
      return 90;
    }
    int closestEnemyHealth = getClosestEnemy().getComponent(Health.class).getHealth();
    if (tankHealth <= 30 && closestEnemyHealth > tankHealth) {
      if (getClearPositions().isEmpty()) {
        // Low health and nowhere to run
        return 0;
      }
      // Low health and can run
      return (int) Math.round(100 / aggression);
    }
    if (tankHealth <= 30 && closestEnemyHealth < tankHealth) {
      // More health than enemy
      return (int) Math.round(10 * aggression);
    }
    return 0;
  }

  /**
   * Gets the utility score for the OBTAINING state.
   *
   * @return The utility score for the OBTAINING state
   */
  private int getObtainingUtility() {
    if (tankHealth <= 10) {
      // can't move -> can't obtain
      return 0;
    }
        /*
        if (tankAmmo <= 5 && ammo powerup is in powerupsInRange){
            idealPowerup = ammo powerup;
            return (int)Math.round(100 * collectiveness);
        }
        if (tankHealth <= 30 && health power up is in powerupsInRange){
            idealPowerup = health powerup;
            return (int)Math.round(100 * collectiveness);
        }
        if (Damage powerup in powerupsInRange) {
            return (int)Math.round(80 * collectiveness)
        }
        if (ANY power up near) {
            return (int)Math.round(50 * collectiveness)
        }
        */
    return 0;
  }

  /**
   * Gets a command from the current state.
   *
   * @return A command from the current state
   */
  private Command performStateAction() {
    switch (state) {
      case SEARCHING:
        return performSearchingAction();
      case ATTACKING:
        return performAttackingAction();
      case FLEEING:
        return performFleeingAction();
      case OBTAINING:
        return performObtainingAction();
    }
    return performSearchingAction();
  }

  /**
   * Gets a command from the SEARCHING state.
   * <p>
   * Travels a path to a clear position on the map.
   *
   * @return A command from the SEARCHING state
   */
  private Command performSearchingAction() {
    // Keep going along the same path if still searching
    if (prevState == State.SEARCHING && !searchPath.isEmpty()) {
      return commandToPerform(searchPath.peek());
    }
    // Make new path if transitioned to searching state or previous path was completed
    Position goal = getRandomClearPosition(); // there should always be a clear position given we are in the searching state
    if (!(goal == null)) {
      searchPath = graph.getPathToLocation(tankPos, goal);
      if (!searchPath.isEmpty()) {
        return commandToPerform(searchPath.peek());
      }
    }
    return Command.DOWN;
  }

  /**
   * Gets a command from the ATTACKING state.
   * <p>
   * If there is a line of sight to the closest enemy then aim towards it then shoots, Else travels a path to the
   * enemy.
   *
   * @return A command from the ATTACKING state
   */
  private Command performAttackingAction() {
    // target closest enemy
    Position nearestEnemy = getClosestEnemy().getComponent(Transform.class).getPosition();
    // if aimed at enemy and line of sight clear: shoot
    // else travel a path to the enemy
    if (checkLineOfSightToPosition(tankPos, nearestEnemy) && aimed) {
      return Command.SHOOT;
    } else {
      Queue<SearchNode> pathToEnemy = graph.getPathToLocation(tankPos, nearestEnemy);
      if (pathToEnemy.isEmpty()) {
        return null;
      }
      // Is a path from tank -> power-up -> enemy viable?
      if (!powerupsInRange.isEmpty()) {
        Position nearPowerup = getClosestPowerup().getTransform().getPosition();
        Queue<SearchNode> tankToPowerup = graph.getPathToLocation(tankPos, nearPowerup);
        Queue<SearchNode> powerupToEnemy = graph.getPathToLocation(nearPowerup, nearestEnemy);
        Queue<SearchNode> tankToPowerupToEnemy = new LinkedList<>();
        tankToPowerupToEnemy.addAll(tankToPowerup);
        tankToPowerupToEnemy.addAll(powerupToEnemy);
        // Viable if path is 1.5x normal path
        if ((tankToPowerupToEnemy.size() / pathToEnemy.size()) < 1.5) {
          pathToEnemy = tankToPowerupToEnemy;
        }
      }
      return commandToPerform(pathToEnemy.peek());
    }
  }

  /**
   * Gets a command from the FLEEING state.
   * <p>
   * Travels a path to a clear position on the map.
   *
   * @return A command from the FLEEING state
   */
  private Command performFleeingAction() {
    // Pick a position in range of the agent that is clear of enemies and travel there
    Position goal = getClosestClearPosition();
    if (!(goal == null)) {
      Queue<SearchNode> path = graph.getPathToLocation(tankPos, goal);
      if (!path.isEmpty()) {
        return commandToPerform(path.peek());
      }
    }
    return Command.DOWN;
  }

  /**
   * Gets a command from the OBTAINING state.
   * <p>
   * Travels a path to a power-up on the map.
   *
   * @return A command from the OBTAINING state
   */
  private Command performObtainingAction() {
    // TODO Get position of (ideal if in range else closest) power-up to collect and travel there
    // Keep going along the same path if still obtaining
    if (prevState == State.OBTAINING && !searchPath.isEmpty()) {
      return commandToPerform(searchPath.peek());
    }
    Position powerupLocation = getClosestPowerup().getTransform().getPosition();
    // Get ideal power-up if can, else carry on with closest
    GameObject idealPowerup = getIdealPowerup();
    if (idealPowerup != null) {
      powerupLocation = idealPowerup.getTransform().getPosition();
    }
    searchPath = graph.getPathToLocation(tankPos, powerupLocation);
    if (searchPath.isEmpty()) {
      return Command.DOWN;
    }
    return commandToPerform(searchPath.peek());
  }

  /**
   * Returns the correct command to travel to a node.
   *
   * @param node The node to travel to
   * @return A command that executes the path
   */
  private Command commandToPerform(SearchNode node) {
    // THESE MIGHT BE WRONG
    if (tankPos.getX() < node.getX()) {
      return Command.RIGHT;
    } else if (tankPos.getX() > node.getX()) {
      return Command.LEFT;
    } else if (tankPos.getY() < node.getY()) {
      return Command.UP;
    } else if (tankPos.getY() > node.getY()) {
      return Command.DOWN;
    }
    return null;
  }

  /**
   * Finds all positions in range of the tank clear of enemies.
   *
   * @return All positions clear of enemies
   */
  private ArrayList<Position> getClearPositions() {
    ArrayList<Position> clearPositions = new ArrayList<Position>();
    ArrayList<SearchNode> nodes = graph.getNodesInRange(tankPos, VIEW_RANGE);
    for (SearchNode node : nodes) {
      if (getEnemiesInRange(node, VIEW_RANGE / 4).isEmpty()) {
        clearPositions.add(node);
      }
    }
    return clearPositions;
  }

  /**
   * Finds a random position in range of the tank clear of enemies.
   *
   * @return A random position clear of enemies
   */
  private Position getRandomClearPosition() {
    ArrayList<Position> clearPositions = getClearPositions();
    Random rand = new Random();
    return clearPositions.get(rand.nextInt(clearPositions.size()));
  }

  /**
   * Finds the closest position in range of the tank clear of enemies.
   *
   * @return The closest position clear of enemies
   */
  private Position getClosestClearPosition() {
    ArrayList<Position> clearPositions = getClearPositions();
    Position closestClearPosition = null;
    double distanceToClosestPosition = Double.MAX_VALUE;
    for (Position clearPosition : clearPositions) {
      double distanceToTank = Math.pow(clearPosition.getY() - tankPos.getY(), 2) + Math.pow(clearPosition.getX() - tankPos.getX(), 2);
      if (distanceToTank < distanceToClosestPosition) {
        closestClearPosition = clearPosition;
        distanceToClosestPosition = distanceToTank;
      }
    }
    return closestClearPosition;
  }

  /**
   * Checks if there exists a line of sight between two points on the map.
   *
   * @param from Start position
   * @param to   End position
   * @return True if there is a line of sight between
   */
  private boolean checkLineOfSightToPosition(Position from, Position to) {
    // Currently broken
    // TODO: change to any angle line of sight
    Queue<SearchNode> path = graph.getPathToLocation(from, to);
    SearchNode first = path.peek();
    while (!path.isEmpty()) {
      if (path.poll() != first) {
        return false;
      }
    }
    return true;
  }

  /**
   * Gets a list of enemies that are in a given range from a given position.
   *
   * @param position The center position to check from
   * @param range    The range
   * @return A list of enemies in range of the position
   */
  private ArrayList<GameObject> getEnemiesInRange(Position position, int range) {
    return getGameObjectsInRange(position, range, /*All enemies in game right now*/new ArrayList<GameObject>()); // TODO: GeT tHiS iNfO
  }

  /**
   * Gets the closest enemy to the tank.
   *
   * @return The closest enemy to the tank
   */
  private GameObject getClosestEnemy() {
    return getClosestObject(enemiesInRange);
  }

  /**
   * Gets a list of power-ups that are in a given range from a given position.
   *
   * @param position The center position to check from
   * @return A list of power-up in range of the position
   */
  private ArrayList<GameObject> getPowerupsInRange(Position position) {
    return getGameObjectsInRange(position, VIEW_RANGE, /*All power-ups in game right now*/new ArrayList<GameObject>()); // TODO: get this INFO BOI
  }

  /**
   * Gets the closest power-up to the tank.
   *
   * @return The closest power-up to the tank
   */
  private GameObject getClosestPowerup() {
    return getClosestObject(powerupsInRange);
  }

  /**
   * Gets the closest ideal power-up to the tank.
   *
   * @return The closest ideal power-up to the tank
   */
  private GameObject getIdealPowerup() {
    ArrayList<GameObject> idealInRange = new ArrayList<GameObject>();
    for (GameObject powerup : powerupsInRange) {
            /*
            if (powerup == idealPowerup) {
                idealInRange.add(powerup);
            }
            */
    }
    return getClosestObject(idealInRange);
  }

  /**
   * Gets all of the specified GameObjects in range of the tank.
   *
   * @param position   Center position to check from
   * @param range      Range of consideration
   * @param allObjects GameObjects to consider
   * @return All specified GameObjects in range
   */
  private ArrayList<GameObject> getGameObjectsInRange(Position position, int range, ArrayList<GameObject> allObjects) {
    ArrayList<GameObject> inRange = new ArrayList<GameObject>();
    for (GameObject enemy : allObjects) {
      if (Math.abs(enemy.getTransform().getX() - position.getX()) <= range ||
      Math.abs(enemy.getTransform().getY() - position.getY()) <= range) {
        inRange.add(enemy);
      }
    }
    return inRange;
  }

  /**
   * Gets the closest specified GameObject to the tank.
   *
   * @param objectsInRange GameObjects in range to consider
   * @return The closest specified GameObject
   */
  private GameObject getClosestObject(ArrayList<GameObject> objectsInRange) {
    GameObject closestObject = null;
    double distanceToClosestObject = Double.MAX_VALUE;
    for (GameObject object : objectsInRange) {
      double distanceToTank = Math.pow(object.getComponent(Transform.class).getY() - tankPos.getY(), 2) + Math.pow(object.getComponent(Transform.class).getX() - tankPos.getX(), 2);
      if (distanceToTank < distanceToClosestObject) {
        closestObject = object;
        distanceToClosestObject = distanceToTank;
      }
    }
    return closestObject;
  }

  /**
   * Calculates an angle change to apply to the aiming of the turret.
   * Change is proportional to error.
   *
   * @return An angle change
   */
  private int getAngleChange() {
    // No enemy to aim at
    if (enemiesInRange.isEmpty()) {
      return 0;
    }
    Position target = getClosestEnemy().getTransform().getPosition();
    double angle = Math.atan2(target.getX() - tankPos.getX(), tankPos.getY() - target.getY());
    if (angle < 0) {
      angle += (Math.PI * 2);
    }
    int targetAngle = (int) Math.round(Math.toDegrees(angle));
    int change = (Math.abs(targetAngle - aimAngle) / 2) + 1;
    if (Math.abs(((aimAngle + change) % 360) - targetAngle) < Math.abs(((aimAngle - change) % 360) - targetAngle)) {
      return change;
    }
    if (Math.abs(((aimAngle + change) % 360) - targetAngle) > Math.abs(((aimAngle - change) % 360) - targetAngle)) {
      return -change;
    }
    // No change needed -> on target
    aimed = true;
    return 0;
  }

  /**
   * The set of states the AI tank can be in.
   */
  private enum State {
    /**
     * Searching state.
     */
    SEARCHING,
    /**
     * Attacking state.
     */
    ATTACKING,
    /**
     * Fleeing state.
     */
    FLEEING,
    /**
     * Obtaining state.
     */
    OBTAINING
  }
}