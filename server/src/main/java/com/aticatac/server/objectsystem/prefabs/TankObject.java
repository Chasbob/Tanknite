//package com.aticatac.server.objectsystem.prefabs;
//
//import com.aticatac.common.exceptions.ComponentExistsException;
//import com.aticatac.common.exceptions.InvalidClassInstance;
//import com.aticatac.common.objectsystem.ObjectType;
//import com.aticatac.server.components.Acceleration;
//import com.aticatac.server.components.Ammo;
//import com.aticatac.server.components.BulletDamage;
//import com.aticatac.server.components.CollisionBox;
//import com.aticatac.server.components.Health;
//import com.aticatac.server.components.PlayerKilledCount;
//import com.aticatac.server.components.Time;
//import com.aticatac.server.ai.AI;
//import com.aticatac.server.components.controller.TankController;
//import com.aticatac.server.components.physics.Physics;
//import com.aticatac.server.transform.Position;
//import com.aticatac.server.transform.Transform;
//import com.aticatac.server.objectsystem.GameObject;
//
//public class TankObject extends GameObject {
//  //add in a parameter boolean which is ai true or false
//  //TODO add in the parameter changes everywhere
//  public TankObject(GameObject parent, String name, Position p, int health, int ammo, boolean isAI) throws InvalidClassInstance, ComponentExistsException {
//    super(name, parent, ObjectType.TANK);
//    this.getComponent(Transform.class).setPosition(p.getX(), p.getY());
//    // determine whether ai, put behavioural trees from ai if so
//    this.addComponent(Health.class).setHealth(health);
//    this.addComponent(Ammo.class).setAmmo(ammo);
//    this.addComponent(Physics.class);
//    this.addComponent(Time.class);
//    this.addComponent(TankController.class);
//    this.addComponent(Acceleration.class);
//    this.addComponent(BulletDamage.class);
//    this.addComponent(PlayerKilledCount.class);
//    this.addComponent(CollisionBox.class);
//    this.getComponent(CollisionBox.class).setCollisionBox(this.getComponent(Transform.class).getPosition());
//    this.getComponent(CollisionBox.class).addBoxToData(this.getComponent(CollisionBox.class).getCollisionBox(), name);
//    if (isAI) {
//      this.addComponent(AI.class);
//    }
//  }
//}
