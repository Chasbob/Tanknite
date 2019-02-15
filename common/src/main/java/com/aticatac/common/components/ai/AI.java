package com.aticatac.common.components.ai;

import com.aticatac.common.components.Ammo;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.Component;
import com.aticatac.common.components.Health;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.common.model.Command;
import javafx.geometry.Pos;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

public class AI extends Component {

    private enum State {
        SEARCHING,
        ATTACKING,
        FLEEING,
        OBTAINING
    }

    private final static int VIEW_RANGE = 6; // some value equivalent to the actual view range that a player would have

    private final GameObject tank;
    private final Graph graph;

    private State state;
    private ArrayList<GameObject> enemiesInRange;
    private Position tankPos;
    private int tankHealth;
    private int tankAmmo;

    public AI(GameObject parent, Graph graph) {
        super(parent);
        this.state = State.SEARCHING;
        this.tank = parent;
        this.graph = graph;
    }

    // ArrayList||Queue<Command> instead?
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

    // There's probably a more elegant way to do this
    private State getStateChange() {
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

    private int getSearchingUtility() {
        return 50;
    }

    private int getAttackingUtility() {
        if (tankAmmo == 0) {
            // Can't attack
            return 0;
        }
        if (!enemiesInRange.isEmpty()) {
            // An enemy is in range
            return 80;
        }
        return 0;
    }

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
            return 100;
        }
        if (tankHealth <= 30 && closestEnemyHealth < tankHealth){
            // More health than enemy
            return 10;
        }

        return 0;
    }

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

    private Command performSearchingAction() {
        Position goal = getClearPosition(); // there should be a clear position given we are in the searching state
        if (!(goal == null)) {
            Queue<Command> path = graph.getPathToLocation(tankPos, goal);
            if (!path.isEmpty()) {
                return path.poll();
            }
        }
        return Command.DOWN;
    }

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

    private Command performObtainingAction() {
        // TODO Get position of power-up to collect and travel there
        Position powerupLocation = new Position(1,2);

        Queue<Command> path = graph.getPathToLocation(tankPos, powerupLocation);
        if (path.isEmpty()) {
            return Command.DOWN;
        }
        return path.poll();
    }

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

    private boolean visibleToEnemy(Position from) {
        for (GameObject enemy : enemiesInRange) {
            Position enemyPos = enemy.getComponent(Transform.class).GetPosition();
            if (checkLineOfSightToPosition(from, enemyPos)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkLineOfSightToPosition(Position from, Position to) {

        // can a direct line be drawn?

        return 1 > 2;
    }

    private ArrayList<GameObject> getEnemiesInRange(Position position, int range) {
        ArrayList<GameObject> allEnemies = new ArrayList<GameObject>(); // TODO ***Get this info***
        ArrayList<GameObject> inRange = new ArrayList<GameObject>();

        for (GameObject enemy : allEnemies) {
            if (Math.abs(enemy.getComponent(Transform.class).GetPosition().x - tankPos.x) <= VIEW_RANGE ||
                    Math.abs(enemy.getComponent(Transform.class).GetPosition().y - tankPos.y) <= VIEW_RANGE) {
                inRange.add(enemy);
            }
        }
        return inRange;
    }

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