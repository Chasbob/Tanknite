package com.aticatac.server.components.controller;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.Physics;

/**
 * The type BulletController.
 */
public class BulletController extends Component {
  private int damage = 10; // or just have special case when shooting with powerup?
  private boolean collided = false;

  /**
   * Instantiates a new Component.
   *
   * @param gameObject the component parent
   */
  public BulletController(GameObject gameObject) {
    super(gameObject);
  }

  /**
   * Move forwards.
   */
  public void moveForwards() {
    while (true) {
      //Position oldPosition = this.getGameObject().getComponent(applyTransform.class).getPosition();
      Object physicsData[] = this.getGameObject().getComponent(Physics.class).bulletMove(this.getGameObject().getComponent(Transform.class).getRotation());
      Position newPosition = (Position) physicsData[1];
      int collisionType = (Integer) physicsData[0];
      //0 nothing, 1 is a wall, 2 is a tank
      if (collisionType != 0) {
        if (collisionType == 1) {
          //Destroy(this);
        } else {
          //collidedTankName = this.getGameObject().getComponent(ServerData.class).getOccupiedCoordinatesTank().getKey(newPosition);// get tank using new position from serverData
          //collidedTank.isShot();
          //Destroy(this);
          collided(); // work out whether collided with wall or tank etc
        }
      }   // set occupied co ordinates for server data
      else {
        this.getGameObject().getComponent(Transform.class).setPosition(newPosition.getX(), newPosition.getY());
      }

            /*
            switch (currentDirection) {
                case 'N':
                    if (this.getComponent(Physics.class).up()) {
                        this.getComponent(applyTransform.class).setPosition(currentXCoord, currentYCoord + 1);
                    } else collided();
                case 'S':
                    if (this.getComponent(Physics.class).down()) {
                        this.getComponent(applyTransform.class).setPosition(currentXCoord, currentYCoord - 1);
                    } else collided();
                case 'E':
                    if (this.getComponent(Physics.class).right()) {
                        this.getComponent(applyTransform.class).setPosition(currentXCoord + 1, currentYCoord);
                    } else collided();
                case 'W':
                    if (this.getComponent(Physics.class).left()) {
                        this.getComponent(applyTransform.class).setPosition(currentXCoord - 1, currentYCoord);
                    } else collided();
            }
            */
    }
  }
  // physics handles bullet collision
  // make a method for bullet to disappear

  public void collided() {
    collided = true;
  }

  public boolean getCurrentCollided() {
    return collided;
  }
}
