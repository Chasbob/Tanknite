package com.aticatac.common.components.ai;

import com.aticatac.common.components.Ammo;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.Component;
import com.aticatac.common.components.Health;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.common.model.Command;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class AI extends Component {
    /**
     * The set of states the AI tank can be in.
     */
    private enum State {
        SEARCHING,
        ATTACKING,
        FLEEING,
        OBTAINING
    }

    private final static int VIEW_RANGE = 6; // some value equivalent to the actual view range that a player would have

    private final GameObject tank;
    private final Graph graph;
    private final double aggression;
    private final double collectiveness;

    private State state;
    private State prevState;
    private Queue<Command> searchPath;
    private ArrayList<GameObject> enemiesInRange;
    private Position tankPos;
    private int tankHealth;
    private int tankAmmo;

    public AI(GameObject parent, Graph graph) {
        super(parent);
        this.tank = parent;
        this.graph = graph;
        this.state = State.SEARCHING;
        this.prevState = State.SEARCHING;
        this.searchPath = new LinkedList<>();

        Random rand = new Random();
        this.aggression = 0.5 + rand.nextDouble();
        this.collectiveness = 0.5 + rand.nextDouble();
    }

    /**
     * Returns a command to control the tank.
     *
     * @return A command
     */
    public Command getCommand() {
        // Update information
        tankPos = tank.getComponent(Transform.class).GetPosition();
        tankHealth = tank.getComponent(Health.class).getHealth();
        tankAmmo = tank.getComponent(Ammo.class).getAmmo();
        enemiesInRange = getEnemiesInRange(tankPos, VIEW_RANGE);

        // Check for a state change
        state = getStateChange();

        // Perform the action(s) of the current state
        return performStateAction();
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
        int maxUtility = Math.max(Math.max(searchingUtility,attackingUtility),Math.max(fleeingUtility,obtainingUtility));

        if (maxUtility == searchingUtility) {
            return State.SEARCHING;
        }
        else if (maxUtility == attackingUtility) {
            return State.ATTACKING;
        }
        else if (maxUtility == fleeingUtility) {
            return State.FLEEING;
        }
        else if (maxUtility == obtainingUtility) {
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
        return 50;
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
            return (int)Math.round(80 * aggression);
        }
        return 0;
    }

    /**
     * Gets the utility score for the FLEEING state.
     *
     * @return The utility score for the FLEEING state
     */
    private int getFleeingUtility() {
        if (enemiesInRange.isEmpty()){
            // Nothing to flee from
            return 0;
        }
        if (tankAmmo == 0) {
            // Enemies near and tank has no ammo
            return 90;
        }

        int closestEnemyHealth = getClosestEnemy().getComponent(Health.class).getHealth();
        if (tankHealth <= 30 && closestEnemyHealth > tankHealth){
            if (getClearPosition() == null) {
                // Low health and nowhere to run
                return 0;
            }
            // Low health and can run
            return (int)Math.round(100 / aggression);
        }
        if (tankHealth <= 30 && closestEnemyHealth < tankHealth){
            // More health than enemy
            return (int)Math.round(10 * aggression);
        }

        return 0;
    }

    /**
     * Gets the utility score for the OBTAINING state.
     *
     * @return The utility score for the OBTAINING state
     */
    private int getObtainingUtility() {
        /*
        if (tankAmmo <= 5 && ammo power up near){
            return 100
        }
        if (tankHealth <= 30 && health power up near){
            return 100
        }
        if (other power up near + some other condition idk)
            return 80
        if (ANY power up near)
            return 60
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
     *
     * Travels a path to a clear position on the map.
     *
     * @return A command from the SEARCHING state
     */
    private Command performSearchingAction() {
        // Keep going along the same path if still searching
        if (prevState == State.SEARCHING && !searchPath.isEmpty()){
            return searchPath.poll();
        }

        // Make new path if transitioned to searching state or previous path was completed
        Position goal = getClearPosition(); // there should always be a clear position given we are in the searching state
        if (!(goal == null)) {
            searchPath = graph.getPathToLocation(tankPos, goal);
            if (!searchPath.isEmpty()) {
                return searchPath.poll();
            }
        }
        return Command.DOWN;
    }

    /**
     * Gets a command from the ATTACKING state.
     *
     * If there is a line of sight to the closest enemy then aim towards it then shoots,
     * Else travels a path to the enemy.
     *
     * @return A command from the ATTACKING state
     */
    private Command performAttackingAction() {
        if (checkLineOfSightToPosition(tankPos, getClosestEnemy().getComponent(Transform.class).GetPosition())) {
            // TODO aim
            return Command.SHOOT;
        }
        else {
            Queue<Command> path = graph.getPathToLocation(tankPos, getClosestEnemy().getComponent(Transform.class).GetPosition());
            if (path.isEmpty()) {
                return Command.DOWN;
            }
            return path.poll();
        }
    }

    /**
     * Gets a command from the FLEEING state.
     *
     * Travels a path to a clear position on the map.
     *
     * @return A command from the FLEEING state
     */
    private Command performFleeingAction() {
        // Pick a position in range of the agent that is clear of enemies and travel there
        Position goal = getClearPosition();
        if (!(goal == null)) {
            Queue<Command> path = graph.getPathToLocation(tankPos, goal);
            if (!path.isEmpty()) {
                return path.poll();
            }
        }
        return Command.DOWN;
    }

    /**
     * Gets a command from the OBTAINING state.
     *
     * Travels a path to a power-up on the map.
     *
     * @return A command from the OBTAINING state
     */
    private Command performObtainingAction() {
        // TODO Get position of power-up to collect and travel there
        Position powerupLocation = new Position(1,2);

        Queue<Command> path = graph.getPathToLocation(tankPos, powerupLocation);
        if (path.isEmpty()) {
            return Command.DOWN;
        }
        return path.poll();
    }

    /**
     * Finds a position in range of the tank clear of enemies.
     *
     * @return A position clear of enemies
     */
    private Position getClearPosition() {
        ArrayList<Position> clearPositions = new ArrayList<Position>();
        for (double i = tankPos.x - VIEW_RANGE; i < tankPos.x + VIEW_RANGE; i++) {
            for (double j = tankPos.y - VIEW_RANGE; j < tankPos.y + VIEW_RANGE; j++) {
                // A position outside the map is not valid
                if (i >= 0 && i < graph.getWidth() && j >= 0 && j < graph.getHeight()) {
                    Position openPos = new Position(i, j);
                    if (getEnemiesInRange(openPos, 4).isEmpty()) {
                        return openPos;
                    }
                }
            }
        }
        Random rand = new Random();
        return clearPositions.get(rand.nextInt(clearPositions.size()));
    }

    // Useful?
    private boolean visibleToEnemy(Position from) {
        for (GameObject enemy : enemiesInRange) {
            Position enemyPos = enemy.getComponent(Transform.class).GetPosition();
            if (checkLineOfSightToPosition(from, enemyPos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there exists a line of sight between two points on the map.
     *
     * @param from Start position
     * @param to End position
     * @return True if there is a line of sight between
     */
    private boolean checkLineOfSightToPosition(Position from, Position to) {
        // Current: if a path between the positions only contains the same command repeated, there is a line of sight
        // TODO: change to any angle line of sight
        Queue<Command> path = graph.getPathToLocation(from, to);
        Command first = path.peek();
        while(!path.isEmpty()) {
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
     * @param range The range
     * @return A list of enemies in range of the position
     */
    private ArrayList<GameObject> getEnemiesInRange(Position position, int range) {
        ArrayList<GameObject> allEnemies = new ArrayList<GameObject>(); // TODO ***Get this info***
        ArrayList<GameObject> inRange = new ArrayList<GameObject>();

        for (GameObject enemy : allEnemies) {
            if (Math.abs(enemy.getComponent(Transform.class).GetPosition().x - position.x) <= range ||
                    Math.abs(enemy.getComponent(Transform.class).GetPosition().y - position.y) <= range) {
                inRange.add(enemy);
            }
        }
        return inRange;
    }

    /**
     * Gets the closest enemy to the tank.
     *
     * @return The closest enemy to the tank
     */
    private GameObject getClosestEnemy() {
        GameObject closestEnemy = null;
        double distanceToClosestEnemy = Double.MAX_VALUE;
        for (GameObject enemy : enemiesInRange) {
            double distanceToTank = Math.sqrt(Math.pow(enemy.getComponent(Transform.class).GetPosition().y - tankPos.y, 2) + Math.pow(enemy.getComponent(Transform.class).GetPosition().x - tankPos.x, 2));
            if (distanceToTank < distanceToClosestEnemy) {
                closestEnemy = enemy;
                distanceToClosestEnemy = distanceToTank;
            }
        }
        return closestEnemy;
    }

}