package com.aticatac.client.server.ai;

import com.aticatac.client.server.Position;
import com.aticatac.client.server.objectsystem.DataServer;
import com.aticatac.common.model.Command;
import com.aticatac.common.objectsystem.EntityType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

/**
 * The AI.
 *
 * @author Dylan
 */
//@SuppressWarnings("ALL")
public class AI {
  private final static int VIEW_RANGE = 32*12; // some value equivalent to the actual view range that a player would have
  private final static Set<SearchNode> occupiedNodes = new HashSet<>();
  private final static Graph graph = new Graph();
  private final PathFinder pathFinder;
  private final double aggression; // (0.8 to 1.2), higher = more likely to chase less likely to flee
  private final double collective; // (0.8 to 1.2), higher = more likely to collect a powerup
  private State state;
  private State prevState;
  private LinkedList<SearchNode> searchPath;
  private Set<Position> recentlyVisitedNodes;
  private ArrayList<Command> commandHistory;
  private ArrayList<PlayerState> enemiesInRange;
  private ArrayList<PowerUpState> powerupsInRange;
  private EntityType idealPowerup;
  private Position tankPos;
  private int tankHealth;
  private int tankAmmo;
  private int aimAngle;
  private Decision.ShootType shooting;
  private AIInput currentInput;

  /**
   * Instantiates a new Ai.
   */
  public AI() {
    this.pathFinder = new PathFinder();
    this.state = State.WANDERING;
    this.searchPath = new LinkedList<>();
    this.recentlyVisitedNodes = new HashSet<>();
    this.commandHistory = new ArrayList<>();
    Random rand = new Random();
    this.aggression = (double)(rand.nextInt(4) + 8) / 10;
    this.collective = (double)(rand.nextInt(4) + 8) / 10;
    this.aimAngle = 0;
  }

  /**
   * Returns a decision to control the tank.
   *
   * @param input the input
   * @return A decision
   */
  public Decision getDecision(AIInput input) {
    // Update tank information
    currentInput = input;
    updateInformation();
    // Check if can shoot at an enemy
    shooting = canShoot();
    // Check for a state change
    state = getStateChange();
    // Create a movement command
    Command command = performStateAction();
    if (!commandHistory.contains(command))
      commandHistory.clear();
    commandHistory.add(command);
    // Return a Decision
    return new Decision(command, aimAngle, shooting);
  }

  /**
   * Updates all of the tanks's information.
   */
  private void updateInformation() {
    tankPos = currentInput.getMe().getPosition();
    tankHealth = currentInput.getMe().getHealth();
    tankAmmo = currentInput.getAmmo();
    enemiesInRange = getEnemiesInRange(tankPos);
    powerupsInRange = getPowerUpsInRange(tankPos, currentInput.getPowerups());
    // Poll search path if close enough to node
    double threshold = 8;
    if (!searchPath.isEmpty()) {
      if (Math.abs(tankPos.getX() - searchPath.peek().getX()) < threshold &&
          Math.abs(tankPos.getY() - searchPath.peek().getY()) < threshold) {
        SearchNode visited = searchPath.poll();
        occupiedNodes.remove(visited);
        recentlyVisitedNodes.addAll(visited.getSubGraph(5));
      }
    }
  }
//----------------------------------------------------STATES------------------------------------------------------------

  /**
   * Returns the state with the highest utility score.
   *
   * @return The state with the highest utility score
   */
  private State getStateChange() {
    prevState = state;
    // Get utility score for each state
    int wanderingUtility = getWanderingUtility();
    int chasingUtility = getChasingUtility();
    int fleeingUtility = getFleeingUtility();
    int obtainingUtility = getObtainingUtility();
//    System.out.println("S: " + wanderingUtility + " | A: " + chasingUtility + " | F: " + fleeingUtility + " | O: " + obtainingUtility);
    // Return state with highest utility
    int maxUtility = Math.max(Math.max(wanderingUtility, chasingUtility), Math.max(fleeingUtility, obtainingUtility));
    if (maxUtility == wanderingUtility) {
      return State.WANDERING;
    }
    if (maxUtility == chasingUtility) {
      return State.CHASING;
    }
    if (maxUtility == fleeingUtility) {
      return State.FLEEING;
    }
    if (maxUtility == obtainingUtility) {
      return State.OBTAINING;
    }
    return State.WANDERING;
  }

  /**
   * Gets the utility score for the WANDERING state.
   *
   * @return The utility score for the WANDERING state
   */
  private int getWanderingUtility() {
    return 30;
  }

  /**
   * Gets the utility score for the CHASING state.
   *
   * @return The utility score for the CHASING state
   */
  private int getChasingUtility() {
    PlayerState closestEnemy = getClosestEnemy(enemiesInRange);
    if (tankAmmo == 0 || (closestEnemy != null && Math.sqrt(Math.pow(tankPos.getX() - closestEnemy.getX(),2) + Math.pow(tankPos.getY() - closestEnemy.getY(),2)) < 64)) {
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
    int closestEnemyHealth = getTargetedEnemy().getHealth();
    if (tankHealth <= 30 && closestEnemyHealth > tankHealth) {
      Position clearPos = getClearPosition();
      if (Math.sqrt(Math.pow(clearPos.getY() - tankPos.getY(), 2) + Math.pow(clearPos.getX() - tankPos.getX(), 2)) < 128) {
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
    if (powerupsInRange.stream().map(PowerUpState::getType).anyMatch(EntityType.AMMO_POWERUP::equals)) {
      idealPowerup = EntityType.AMMO_POWERUP;
      return (int) Math.round((100 - tankAmmo) * collective);
    }
    if (powerupsInRange.stream().map(PowerUpState::getType).anyMatch(EntityType.HEALTH_POWERUP::equals)) {
      idealPowerup = EntityType.HEALTH_POWERUP;
      return (int) Math.round((100 - tankHealth) * collective);
    }
    if (powerupsInRange.stream().map(PowerUpState::getType).anyMatch(EntityType.DAMAGE_POWERUP::equals)) {
      return (int) Math.round(80 * collective);
    }
    if (!powerupsInRange.isEmpty()) {
      return (int) Math.round(50 * collective);
    }
    return 0;
  }
//----------------------------------------------------ACTIONS-----------------------------------------------------------

  /**
   * Gets a command from the current state.
   *
   * @return A command from the current state
   */
  private Command performStateAction() {
    // Do searching action if stuck
    if (commandHistory.size() > 30) {
      return performWanderingAction();
    }
    // Keep going along same path if state has not changed
    if (prevState == state && !searchPath.isEmpty()) {
      return commandToPerform(searchPath.peek());
    }
    switch (state) {
      case WANDERING:
        return performWanderingAction();
      case CHASING:
        return performChasingAction();
      case FLEEING:
        return performFleeingAction();
      case OBTAINING:
        return performObtainingAction();
    }
    return performWanderingAction();
  }

  /**
   * Gets a command from the WANDERING state.
   * <p>
   * Travels a path to a clear position on the map.
   *
   * @return A command from the WANDERING state
   */
  private Command performWanderingAction() {
    Position goal = getRandomPosition();
    searchPath = getPath(tankPos, goal);
    return commandToPerform(searchPath.peek());
  }

  /**
   * Gets a command from the CHASING state.
   * <p>
   * Travels a path to stay in range of an enemy.
   *
   * @return A command from the CHASING state
   */
  private Command performChasingAction() {
    PlayerState target = getTargetedEnemy();
    if (target == null) {
      return Command.DEFAULT;
    }
    if (Math.sqrt(Math.pow(tankPos.getX()-target.getX(),2) + Math.pow(tankPos.getY()-target.getY(), 2)) < 96 && checkLineOfSightToPosition(tankPos, target.getPosition())) {
      return performWanderingAction();
    }
    searchPath = getPath(tankPos, target.getPosition());
    while (searchPath.size() > 4) {
      searchPath.removeLast();
    }
    return commandToPerform(searchPath.peek());
  }

  /**
   * Gets a command from the FLEEING state.
   * <p>
   * Travels a path to a clear position on the map.
   *
   * @return A command from the FLEEING state
   */
  private Command performFleeingAction() {
    Position goal = getClearPosition();
    searchPath = getPath(tankPos, goal);
    while (searchPath.size() > 5) {
      searchPath.removeLast();
    }
    return commandToPerform(searchPath.peek());
  }

  /**
   * Gets a command from the OBTAINING state.
   * <p>
   * Travels a path to a power-up on the map.
   *
   * @return A command from the OBTAINING state
   */
  private Command performObtainingAction() {
    Position powerupLocation = getClosestPowerUp(currentInput.getPowerups()).getPosition();
    // Get ideal power-up if can, else carry on with closest
    PowerUpState idealPowerup = getIdealPowerup();
    if (idealPowerup != null) {
      powerupLocation = idealPowerup.getPosition();
    }
    searchPath = getPath(tankPos, powerupLocation);
    if (searchPath.isEmpty()) {
      return Command.DOWN;
    }
    return commandToPerform(searchPath.peek());
  }

  /**
   * Gets a path from one position to another.
   *
   * @param from Start position
   * @param to   Goal position
   *
   * @return A path from one position to another
   */
  private LinkedList<SearchNode> getPath(Position from, Position to) {
    return pathFinder.getPathToLocation(graph.getNearestNode(from), graph.getNearestNode(to));
  }

  /**
   * Returns the correct command to travel to a node.
   *
   * @param node The node to travel to
   *
   * @return A command that executes the path
   */
  private Command commandToPerform(SearchNode node) {
    if (node == null) return Command.DEFAULT;
    int threshold = 4;
    if (tankPos.getX() > node.getX() && Math.abs(tankPos.getX() - node.getX()) > threshold) {
      return Command.RIGHT;
    } else if (tankPos.getX() < node.getX() && Math.abs(tankPos.getX() - node.getX()) > threshold) {
      return Command.LEFT;
    } else if (tankPos.getY() > node.getY() && Math.abs(tankPos.getY() - node.getY()) > threshold) {
      return Command.UP;
    } else if (tankPos.getY() < node.getY() && Math.abs(tankPos.getY() - node.getY()) > threshold) {
      return Command.DOWN;
    }
    return Command.DEFAULT;
  }
//-------------------------------------------------POSITIONS------------------------------------------------------------

  /**
   * Finds all positions in range of the tank clear of enemies.
   *
   * @return All positions clear of enemies
   */
  private ArrayList<Position> getEmptyPositions(int range) {
    ArrayList<Position> emptyPositions = new ArrayList<>();
    ArrayList<Position> nodes = new ArrayList<>(graph.getNodesInRange(tankPos, range));
    // Occupied nodes are not empty
    for (Position node : nodes) {
      if (!occupiedNodes.contains(node)) {
        emptyPositions.add(node);
      }
    }
    // Enemy and near-enemy positions are not empty
    for (PlayerState enemy : enemiesInRange) {
      emptyPositions.removeAll(graph.getNearestNode(enemy.getPosition()).getSubGraph(2));
    }
    if (emptyPositions.isEmpty()) {
      return nodes;
    }
    return emptyPositions;
  }

  /**
   * Finds a random position in range of the tank clear of enemies. Used in the default WANDERING state.
   *
   * @return A random position clear of enemies
   */
  private Position getRandomPosition() {
    ArrayList<Position> clearPositions = getEmptyPositions(VIEW_RANGE);
    ArrayList<Position> newClearPositions = new ArrayList<>();
    for (Position position : clearPositions) {
      if (!recentlyVisitedNodes.contains(position)) {
        newClearPositions.add(position);
      }
    }
    if (!newClearPositions.isEmpty()) {
      clearPositions = newClearPositions;
    } else {
      recentlyVisitedNodes.clear();
    }
    Random rand = new Random();
    return clearPositions.get(rand.nextInt(clearPositions.size()));
  }

  /**
   * Finds a position in range of the tank clear of enemies.
   *
   * @return A position clear of enemies
   */
  private Position getClearPosition() {
    Random rand = new Random();
    ArrayList<Position> clearPositions = new ArrayList<>();
    for (Position position : getEmptyPositions(VIEW_RANGE / 2)) {
      boolean clear = true;
      for (PlayerState enemy : enemiesInRange) {
        if (Math.sqrt(Math.pow(position.getY() - enemy.getY(), 2) + Math.pow(position.getX() - enemy.getX(), 2)) < 128) {
          clear = false;
        }
      }
      if (clear) {
        clearPositions.add(position);
      }
    }
    if (clearPositions.isEmpty()) {
      return getRandomPosition();
    }
    return clearPositions.get(rand.nextInt(clearPositions.size()));
  }

  /**
   * Checks if there exists a line of sight between two points on the map.
   *
   * @param from Start position
   * @param to   End position
   *
   * @return True if there is a line of sight between
   */
  private boolean checkLineOfSightToPosition(Position from, Position to) {
    // being this close and not having a line of sight is impossible
    if (Math.sqrt(Math.pow(from.getX() - to.getX(),2) + Math.pow(from.getY() - to.getY(),2)) <= 32) {
      return true;
    }
    String[][] map = DataServer.INSTANCE.getMap();
    ArrayList<Position> linePoints = new ArrayList<>();
    Position start = new Position(Math.round((float) from.getX() / 32), Math.round((float) from.getY() / 32));
    Position end = new Position(Math.round((float) to.getX() / 32), Math.round((float) to.getY() / 32));
    int steps = Math.max(Math.abs(start.getX() - end.getX()), Math.abs(start.getY() - end.getY()));
    for (int i = 0; i <= steps; i++) {
      float point;
      if (i == 0) {
        point = 0;
      } else {
        point = (float) i / steps;
      }
      linePoints.add(new Position(Math.round(start.getX() + point * (end.getX() - start.getX())), Math.round(start.getY() + point * (end.getY() - start.getY()))));
    }
    for (Position point : linePoints) {
      if (point.getX() >= 0 && point.getX() < map.length && point.getY() >= 0 && point.getY() < map.length) {
        if (map[point.getX()][point.getY()].equals("2")) {
          return false;
        }
      }
    }
    return true;
  }
//---------------------------------------------------ENEMIES------------------------------------------------------------

  /**
   * Gets a list of enemies that are in a given range from a given position.
   *
   * @param position The center position to check from
   * @return A list of enemies in range of the position
   */
  private ArrayList<PlayerState> getEnemiesInRange(Position position) {
    ArrayList<PlayerState> inRange = new ArrayList<>(currentInput.getPlayers());
    inRange.remove(getClosestEnemy(currentInput.getPlayers())); // remove self...
    for (PlayerState enemy : currentInput.getPlayers()) {
      int dX = Math.abs(position.getX() - enemy.getPosition().getX());
      int dY = Math.abs(position.getY() - enemy.getPosition().getY());
      if (dX > VIEW_RANGE || dY > VIEW_RANGE && enemy.getHealth() > 0) {
        inRange.remove(enemy);
      }
    }
    return inRange;
  }

  /**
   * Gets a list of enemies that have a line of sight to the tank.
   *
   * @return A list of enemies in sight
   */
  private ArrayList<PlayerState> getEnemiesInSight() {
    ArrayList<PlayerState> enemiesInSight = new ArrayList<>();
    for (PlayerState enemy : enemiesInRange) {
      if (checkLineOfSightToPosition(tankPos, enemy.getPosition())) {
        enemiesInSight.add(enemy);
      }
    }
    return enemiesInSight;
  }

  /**
   * Targets an enemy to attack.
   * <p>
   * If there are enemies with a line of sight to the tank, the closest one is targeted. Otherwise the closest enemy in
   * range is targeted.
   *
   * @return The targeted enemy
   */
  private PlayerState getTargetedEnemy() {
    // Prioritize targeting enemies in sight
    ArrayList<PlayerState> enemiesInSight = getEnemiesInSight();
    // Target closest in sight
    if (!enemiesInSight.isEmpty()) {
      return getClosestEnemy(enemiesInSight);
    }
    // If none in sight, target closest in range
    return getClosestEnemy(enemiesInRange);
  }

  /**
   * Gets the closest specified enemy to the tank.
   *
   * @param enemies Enemies to consider
   *
   * @return The closest enemy
   */
  private PlayerState getClosestEnemy(ArrayList<PlayerState> enemies) {
    PlayerState closestObject = null;
    double distanceToClosestObject = Double.MAX_VALUE;
    for (PlayerState object : enemies) {
      // no need for sqrt
      double distanceToTank = Math.pow(object.getY() - tankPos.getY(), 2)
          + Math.pow(object.getX() - tankPos.getX(), 2);
      if (distanceToTank < distanceToClosestObject) {
        closestObject = object;
        distanceToClosestObject = distanceToTank;
      }
    }
    return closestObject;
  }
//--------------------------------------------------POWER-UPS-----------------------------------------------------------

  /**
   * Gets the closest ideal power-up to the tank.
   *
   * @return The closest ideal power-up to the tank
   */
  private PowerUpState getIdealPowerup() {
    ArrayList<PowerUpState> idealInRange = new ArrayList<>();
    for (PowerUpState powerup : powerupsInRange) {
      if (powerup.type == idealPowerup) {
        idealInRange.add(powerup);
      }
    }
    return getClosestPowerUp(idealInRange);
  }

  /**
   * Gets all of the power-ups in range of the tank.
   *
   * @param position Center position to check from
   * @param powerUps Power-ups to consider
   *
   * @return All specified GameObjects in range
   */
  private ArrayList<PowerUpState> getPowerUpsInRange(Position position, Collection<PowerUpState> powerUps) {
    ArrayList<PowerUpState> inRange = new ArrayList<>();
    for (PowerUpState enemy : powerUps) {
      if (Math.abs(enemy.getX() - position.getX()) <= VIEW_RANGE &&
          Math.abs(enemy.getY() - position.getY()) <= VIEW_RANGE) {
        inRange.add(enemy);
      }
    }
    return inRange;
  }

  /**
   * Gets the closest power-up to the tank.
   *
   * @param powerUps Power-ups to consider
   *
   * @return The closest power-up
   */
  private PowerUpState getClosestPowerUp(ArrayList<PowerUpState> powerUps) {
    PowerUpState closestObject = null;
    double distanceToClosestObject = Double.MAX_VALUE;
    for (PowerUpState object : powerUps) {
      // no need for sqrt
      double distanceToTank = Math.pow(object.getY() - tankPos.getY(), 2)
          + Math.pow(object.getX() - tankPos.getX(), 2);
      if (distanceToTank < distanceToClosestObject) {
        closestObject = object;
        distanceToClosestObject = distanceToTank;
      }
    }
    return closestObject;
  }
//--------------------------------------------------AIMING--------------------------------------------------------------

  /**
   * Checks if the tank can shoot at an enemy
   *
   * @return If the tank can shoot at an enemy
   */
  private Decision.ShootType canShoot() {
    if (!(tankAmmo <= 0 || enemiesInRange.isEmpty())) {
      Position target = getTargetedEnemy().getPosition();
      if (target != null) {
        aimAngle = getNewAimAngle(target);
        if (getEnemiesInSight().size() > 1 && Math.random() > 0.5 && currentInput.getSprayAmmo() > 0) {
          return Decision.ShootType.SPRAY;
        }
        if (checkLineOfSightToPosition(tankPos, target) && Math.abs(aimAngle - getAngleToPosition(target)) < 5) {
          if (currentInput.getFreezeAmmo() > 0 && Math.random() > 0.75) {
            return Decision.ShootType.FREEZE;
          }
          return Decision.ShootType.NORMAL;
        }
      }
    }
    // Do "random" aiming if not shooting
    if (!searchPath.isEmpty() && state != State.CHASING) {
      aimAngle = getNewAimAngle(searchPath.peekLast());
    }
    return Decision.ShootType.NONE;
  }

  /**
   * Calculates an angle to aim at given a target
   *
   * @return An aim angle
   */
  private int getNewAimAngle(Position target) {
    if (target == null) {
      return aimAngle;
    }
    double targetAngle = getAngleToPosition(target);
    double change = Math.abs(targetAngle - aimAngle) / 8;
    if (Math.abs(changeAngle(aimAngle, change) - targetAngle) < Math.abs(changeAngle(aimAngle, -change) - targetAngle)) {
      return (int) Math.round(changeAngle(aimAngle, change));
    }
    if (Math.abs(changeAngle(aimAngle, change) - targetAngle) > Math.abs(changeAngle(aimAngle, -change) - targetAngle)) {
      return (int) Math.round(changeAngle(aimAngle, -change));
    }
    // No change needed -> on target
    return aimAngle;
  }

  /**
   * Calculates the angle between the tank and a given position.
   *
   * @param target The position
   * @return An angle between the tank and a position
   */
  private double getAngleToPosition(Position target) {
    if (target == null) {
      return 0;
    }
    Position targetCenter = new Position(target.getX() + 16, target.getY() + 16);
    double angle = Math.atan2(targetCenter.getX() - tankPos.getX(), tankPos.getY() - targetCenter.getY());
    if (angle < 0) {
      angle += (Math.PI * 2);
    }
    return (Math.toDegrees(angle) + 90) % 360;
  }

  /**
   * Applies a given change to a given angle.
   * @param angle  Angle to change
   * @param change Amount to change by
   * @return Resultant angle
   */
  private double changeAngle(double angle, double change) {
    return (angle + (change % 360) + 360) % 360;
  }

  /**
   * The set of behavior states the AI tank can be in.
   */
  private enum State {
    /**
     * Wandering state.
     */
    WANDERING,
    /**
     * Chasing state.
     */
    CHASING,
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