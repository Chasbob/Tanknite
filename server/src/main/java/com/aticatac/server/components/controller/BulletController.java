//package com.aticatac.server.components.controller;
//
//import com.aticatac.server.components.Component;
//import com.aticatac.server.components.physics.Entity;
//import com.aticatac.server.components.physics.Physics;
//import com.aticatac.server.components.physics.PhysicsResponse;
//import com.aticatac.server.components.transform.Position;
//import com.aticatac.server.components.transform.Transform;
//import com.aticatac.server.objectsystem.GameObject;
//
///**
// * The type BulletController.
// */
//public class BulletController extends Component {
//  private int damage = 10; // or just have special case when shooting with powerup?
//  private boolean collided = false;
//
//  /**
//   * Instantiates a new Component.
//   *
//   * @param gameObject the component parent
//   */
//  public BulletController(GameObject gameObject) {
//    super(gameObject);
//  }
//
//  public Entity move() {
//    this.logger.trace(getGameObject().getTransform().getRotation());
//    Position oldPosition = this.getGameObject().getComponent(Transform.class).getPosition();
//    PhysicsResponse physicsData = this.getGameObject().getComponent(Physics.class).move(this.getGameObject().getComponent(Transform.class).getRotation());
////        this.logger.info(physicsData);
//    //0 nothing, 1 is a wall, 2 is a tank
//    if (physicsData.getEntity().getType() == EntityType.WALL) {
//      this.logger.warn("Bullet hit a wall!");
//    } else if (!physicsData.entity.getName().equals(gameObject.getName())) {
//      this.logger.trace(physicsData);
//      getGameObject().getComponent(Transform.class).setPosition(physicsData.getPosition());
//    } else {
//      this.logger.trace(physicsData);
//      getGameObject().getComponent(Transform.class).setPosition(physicsData.getPosition());
//    }
//    return physicsData.getEntity();
////        if (physicsData.getEntity().getType() != EntityType.NONE) {
////          if (physicsData.getEntity().getType() != EntityType.TANK) {
////            GameObject.destroy(getGameObject());
////          }
////
//  }
//}
