//package com.aticatac.server.prefabs;
//
//import com.aticatac.common.exceptions.ComponentExistsException;
//import com.aticatac.common.exceptions.InvalidClassInstance;
//import com.aticatac.common.objectsystem.ObjectType;
//import com.aticatac.server.components.BulletCollisionBox;
//import com.aticatac.server.components.Damage;
//import com.aticatac.server.components.Time;
//import com.aticatac.server.components.controller.BulletController;
//import com.aticatac.server.components.physics.Entity;
//import com.aticatac.server.components.physics.Physics;
//import com.aticatac.server.objectsystem.GameObject;
//import com.aticatac.server.objectsystem.interfaces.Moveable;
//
///**
// * The type Bullet object.
// */
//public class BulletObject extends GameObject implements Moveable {
//  /**
//   * Instantiates a new GameObject.
//   *
//   * @param name the name
//   *
//   * @throws InvalidClassInstance     the invalid class instance
//   * @throws ComponentExistsException the component exists exception
//   */
//  public BulletObject(String name) throws InvalidClassInstance, ComponentExistsException {
//    super(name, ObjectType.BULLET);
//    this.addComponent(Physics.class);
//    this.addComponent(Time.class);
//    this.addComponent(BulletController.class);
//    this.addComponent(Damage.class);
//    this.addComponent(BulletCollisionBox.class);
//  }
//
//  @Override
//  public Entity move() {
//   return getComponent(BulletController.class).move();
//  }
//}
