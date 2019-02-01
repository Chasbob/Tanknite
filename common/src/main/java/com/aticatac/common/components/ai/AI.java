package com.aticatac.common.components.ai;

import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.Component;
import com.aticatac.common.components.Health;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.common.model.Command;
import java.util.Queue;

public class AI extends Component {

    private enum State {
        searching,
        attacking,
        fleeing,
        obtaining
    }

    private static final PathFinder pf = new PathFinder();

    private State state;
    private GameObject tank;

    public AI(GameObject parent) {
        super(parent);
        this.state = State.searching;
        this.tank = parent;
    }

    public Command getCommand() {
        checkStateChange();
        return performStateAction();
    }

    private void checkStateChange() {
        int searchingUtility = getSearchingUtility();
        int attackingUtility = getAttackingUtility();
        int fleeingUtility = getFleeingUtility();
        int obtainingUtility = getObtainingUtility();



    }

    private int getSearchingUtility() {
        return 50;
    }

    private int getAttackingUtility() {
        /*
        if (enemy in range) {
            return 80;
        }
        */
        return 0;
    }

    private int getFleeingUtility() {
        int health = tank.getComponent(Health.class).getHealth();
        /*
        if (no enemies in range){
            return 0;
        }
        if (health <= 30 && nearestEnemyHealth > health){
            return 100;
        }
        if (health <= 30 && nearestEnemyHealth < health){
            return 10;
        }
        */
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
        return Command.DOWN;
    }

    private Command performAttackingAction() {
        /*
        GameObject enemy;
        if (checkLineOfSightToEnemy(enemy)) {
            // orient turret in right position then
            return Command.SHOOT;
        }
        else {
            //Queue<Command> path = pf.getPathToLocation(tank.getLocation(), targetedEnemy);
            //executePath(path);
            return Command.DOWN;
        }
        */
        return Command.DOWN;
    }

    private Command performFleeingAction() {
        // prioritize removing line of sight to any enemies when running away
        return Command.DOWN;
    }

    private Command performObtainingAction() {
        // travel a path to a nearby power up
        return Command.DOWN;
    }

    private boolean checkLineOfSightToEnemy(GameObject enemy) {
        Position tankPos = tank.getComponent(Transform.class).GetPosition();
        Position enemyPos = enemy.getComponent(Transform.class).GetPosition();

        // can a direct line be drawn from tankPos to enemyPos?

        return 1 > 2;
    }

}