package com.aticatac.server.components.ai;

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

/**
 * Long ago, the four nations lived together in harmony. Then, everything changed when the Fire Nation attacked.
 * Only the AI component, master of all four elements, could stop them, but when the world needed it the most,
 * it vanished. A hundred years passed and my brother and I discovered the new AI component,and although its
 * airbending skills are great, it has a lot to learn before it's ready to save anyone. But I believe AI component
 * can save the world.
 *
 * @author Dylan
 */
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
    private final double aggression; // (0.5 to 1.5) higher = more likely to attack less likely to flee
    private final double collectiveness; // (0.5 to 1.5) higher = more likely to collect powerup

    private State state;
    private State prevState;
    private Queue<Command> searchPath;
    private ArrayList<GameObject> enemiesInRange;
    private ArrayList<GameObject> powerupsInRange;
    //private Something idealPowerup;
    private Position tankPos;
    private int tankHealth;
    private int tankAmmo;
    private int aimAngle;
    private boolean aimed;

    public AI(GameObject parent, Graph graph) {
        super(parent);
        this.tank = parent;
        this.graph = graph;
        this.state = State.SEARCHING;
        this.prevState = State.SEARCHING;
        this.searchPath = new LinkedList<>();

        this.aggression = (double)Math.round( (0.5 + Math.random()) * 10) / 10;
        this.collectiveness = (double)Math.round( (0.5 + Math.random()) * 10) / 10;

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
        powerupsInRange = getPowerupsInRange(tankPos, VIEW_RANGE);

        // Change aim angle
        aimed = false;
        int angleChange = getAngleChange();
        aimAngle += angleChange;

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
        if (enemiesInRange.isEmpty() || tankHealth <= 10){
            // Nothing to flee from || can't move -> can't flee
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
        if (tankHealth <= 10) {
            // can't move -> can't obtain
            return 0;
        }
        if (tankAmmo <= 5 && ammo powerup is in powerupsInRange){
            idealPowerup = ammo powerup;
            return (int)Math.round(100 * collectiveness);
        }
        if (tankHealth <= 30 && health power up near){
            idealPowerup = health powerup;
            return (int)Math.round(100 * collectiveness);
        }
        if (other power up near + some other condition idk)
            return (int)Math.round(80 * collectiveness)
        if (ANY power up near)
            return (int)Math.round(60 * collectiveness)
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
        if (checkLineOfSightToPosition(tankPos, getClosestEnemy().getComponent(Transform.class).getPosition()) && aimed) {
            return Command.SHOOT;
        }
        else {
            Queue<Command> path = graph.getPathToLocation(tankPos, getClosestEnemy().getComponent(Transform.class).getPosition());
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

        // Keep going along the same path if still obtaining
        if (prevState == State.OBTAINING && !searchPath.isEmpty()){
            return searchPath.poll();
        }

        // Position powerupLocation = powerUpsInRange.get ideal powerup type
        Position powerupLocation = new Position(1,2);

        searchPath = graph.getPathToLocation(tankPos, powerupLocation);
        if (searchPath.isEmpty()) {
            return Command.DOWN;
        }
        return searchPath.poll();
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
            Position enemyPos = enemy.getComponent(Transform.class).getPosition();
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
        return getGameObjectsInRange(position, range,  new ArrayList<GameObject>()); // TODO: GeT tHiS iNfO
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
     * @param range The range
     * @return A list of power-up in range of the position
     */
    private ArrayList<GameObject> getPowerupsInRange(Position position, int range) {
        return getGameObjectsInRange(position, range, new ArrayList<GameObject>()); // TODO: get this INFO BOI
    }

    /**
     * Gets the closest power-up to the tank.
     *
     * @return The closest power-up to the tank
     */
    private GameObject getClosestPowerup() {
        return getClosestObject(powerupsInRange);
    }

    private ArrayList<GameObject> getGameObjectsInRange(Position position, int range, ArrayList<GameObject> allObjects) {
        ArrayList<GameObject> inRange = new ArrayList<GameObject>();
        for (GameObject enemy : allObjects) {
            if (Math.abs(enemy.getComponent(Transform.class).getPosition().x - position.x) <= range ||
                    Math.abs(enemy.getComponent(Transform.class).getPosition().y - position.y) <= range) {
                inRange.add(enemy);
            }
        }
        return inRange;
    }

    private GameObject getClosestObject(ArrayList<GameObject> objectsInRange) {
        GameObject closestObject = null;
        double distanceToClosestObject = Double.MAX_VALUE;
        for (GameObject object : objectsInRange) {
            double distanceToTank = Math.sqrt(Math.pow(object.getComponent(Transform.class).getPosition().y - tankPos.y, 2) + Math.pow(object.getComponent(Transform.class).getPosition().x - tankPos.x, 2));
            if (distanceToTank < distanceToClosestObject) {
                closestObject = object;
                distanceToClosestObject = distanceToTank;
            }
        }
        return closestObject;
    }

    private int getAngleChange() {
        // No enemy to aim at
        if (enemiesInRange.isEmpty()) {
            return 0;
        }

        Position target = getClosestEnemy().getTransform().getPosition();

        double angle = Math.atan2(target.x - tankPos.x, tankPos.y - target.y);
        if (angle < 0)
            angle += (Math.PI * 2);
        int targetAngle = (int)Math.round(Math.toDegrees(angle));

        int change = Math.abs(targetAngle - aimAngle) / 2;

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

}