package com.aticatac.common.components.ai;

import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.Component;
import com.aticatac.common.components.Health;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.common.model.Command;

import java.util.ArrayList;
import java.util.Queue;

public class AI extends Component {

    private enum State {
        searching,
        attacking,
        fleeing,
        obtaining
    }

    private PathFinder pf;

    private State state;
    private GameObject tank;

    private Graph graph;
    private ArrayList<GameObject> enemiesInRange;

    public AI(GameObject parent, Graph graph, int seperation) {
        super(parent);
        this.state = State.searching;
        this.tank = parent;
        this.graph = graph;
        this.pf = new PathFinder(seperation);
    }

    public Command getCommand() {
        enemiesInRange = getEnemiesInRange();
        checkStateChange();
        return performStateAction();
    }

    private ArrayList<GameObject> getEnemiesInRange() {
        //TODO: get enemies in range
        return null;
    }

    private GameObject getClosestEnemy() {
        GameObject closestEnemy = null;
        double distanceToClosestEnemy = Double.MAX_VALUE;
        for (GameObject enemy : enemiesInRange) {
            double distanceToTank = Math.sqrt(Math.pow(enemy.getComponent(Transform.class).GetPosition().y - tank.getComponent(Transform.class).GetPosition().y, 2) + Math.pow(enemy.getComponent(Transform.class).GetPosition().x - tank.getComponent(Transform.class).GetPosition().x, 2));
            if (distanceToTank < distanceToClosestEnemy) {
                closestEnemy = enemy;
                distanceToClosestEnemy = distanceToTank;
            }
        }
        return closestEnemy;
    }

    private void checkStateChange() {
        int searchingUtility = getSearchingUtility();
        int attackingUtility = getAttackingUtility();
        int fleeingUtility = getFleeingUtility();
        int obtainingUtility = getObtainingUtility();

        int maxUtility = Math.max(Math.max(searchingUtility,attackingUtility),Math.max(fleeingUtility,obtainingUtility));

        if (maxUtility == searchingUtility) {
            state = State.searching;
        }
        else if (maxUtility == attackingUtility) {
            state = State.attacking;
        }
        else if (maxUtility == fleeingUtility) {
            state = State.fleeing;
        }
        else if (maxUtility == obtainingUtility) {
            state = State.obtaining;
        }

    }

    private int getSearchingUtility() {
        return 50;
    }

    private int getAttackingUtility() {
        if (!enemiesInRange.isEmpty()) {
            return 80;
        }
        return 0;
    }

    private int getFleeingUtility() {
        int health = tank.getComponent(Health.class).getHealth();

        if (enemiesInRange.isEmpty()){
            return 0;
        }

        int closestEnemyHealth = getClosestEnemy().getComponent(Health.class).getHealth();
        if (health <= 30 && closestEnemyHealth > health){
            return 100;
        }
        if (health <= 30 && closestEnemyHealth < health){
            return 10;
        }

        return 0;
    }

    private int getObtainingUtility() {
        /*
        if (power-up is near or something){
            return 70
        }
        */
        return 0;
    }

    private Command performStateAction() {
        switch (state) {
            case searching:
                return performSearchingAction();
            case attacking:
                return performAttackingAction();
            case fleeing:
                return performFleeingAction();
            case obtaining:
                return performObtainingAction();
        }
        return Command.DOWN;
    }

    private Command performSearchingAction() {
        Queue<Command> path = pf.getRandomPath();
        if (path.isEmpty()) {
            return Command.DOWN;
        }
        return path.poll();
    }

    private Command performAttackingAction() {
        if (checkLineOfSightToPosition(tank.getComponent(Transform.class).GetPosition(), getClosestEnemy().getComponent(Transform.class).GetPosition())) {
            // orient turret in right position then
            return Command.SHOOT;
        }
        else {
            Queue<Command> path = pf.getPathToLocation(graph.getNearestNode(tank.getComponent(Transform.class).GetPosition()), graph.getNearestNode(getClosestEnemy().getComponent(Transform.class).GetPosition()));
            if (path.isEmpty()) {
                return Command.DOWN;
            }
            return path.poll();
        }
    }

    private Command performFleeingAction() {
        // prioritize removing line of sight to any enemies when running away

        Position tankPos = tank.getComponent(Transform.class).GetPosition();
        if (visibleToEnemy(tankPos)) {
            Position upPos = new Position(tankPos.x, tankPos.y + 1);  // amount?
            if (!visibleToEnemy(upPos)) {
                return Command.UP;
            }
            Position downPos = new Position(tankPos.x, tankPos.y - 1);  // amount?
            if (!visibleToEnemy(downPos)) {
                return Command.DOWN;
            }
            Position leftPos = new Position(tankPos.x - 1, tankPos.y);  // amount?
            if (!visibleToEnemy(leftPos)) {
                return Command.LEFT;
            }
            Position rightPos = new Position(tankPos.x + 1, tankPos.y);  // amount?
            if (!visibleToEnemy(rightPos)) {
                return Command.RIGHT;
            }
        }
        else {

        }

        // Alternatively pick a position in range of the agent that is clear of enemies
        // actually this is probably the way to go

        /*

         */
        return Command.DOWN;
    }

    private Command performObtainingAction() {
        // travel a path to a nearby power up
        return Command.DOWN;
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

}