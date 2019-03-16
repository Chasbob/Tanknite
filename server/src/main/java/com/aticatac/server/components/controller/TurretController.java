//package com.aticatac.server.components.controller;
//
//import com.aticatac.common.exceptions.ComponentExistsException;
//import com.aticatac.common.exceptions.InvalidClassInstance;
//import com.aticatac.common.model.CommandModel;
//import com.aticatac.server.components.Ammo;
//import com.aticatac.server.components.BulletCollisionBox;
//import com.aticatac.server.components.BulletDamage;
//import com.aticatac.server.components.Component;
//import com.aticatac.server.components.Damage;
//import com.aticatac.server.components.physics.Entity;
//import com.aticatac.server.objectsystem.GameObject;
//import com.aticatac.server.objectsystem.IO.outputs.TurretOutput;
//import com.aticatac.server.objectsystem.interfaces.Tickable;
//import com.aticatac.server.objectsystem.interfaces.inputs.Input;
//import com.aticatac.server.prefabs.BulletObject;
//import java.util.ArrayList;
//import org.apache.log4j.Level;
//
//public class TurretController extends Component implements Tickable {
//  private final ArrayList<BulletObject> bullets;
//
//  /**
//   * Instantiates a new Component.
//   *
//   * @param gameObject the component parent
//   */
//  public TurretController(GameObject gameObject) {
//    super(gameObject);
//    bullets = new ArrayList<>();
//    this.logger.setLevel(Level.INFO);
//  }
//
//  public void shoot(final CommandModel model) {
//    shoot(model.getBearing());
//  }
//
//  public void shoot(final int bearing) {
//    this.logger.trace("Shooting...");
//    if (getGameObject().getComponent(Ammo.class).hasAmmo()) {
//      getGameObject().getComponent(Ammo.class).use();
//      //TODO shooting
//      //create a bullet object
//      try {
//        BulletObject bullet = new BulletObject("bullet");
//        bullet.setTransform(getGameObject().getTransform());
//        bullet.getComponent(BulletCollisionBox.class).update();
//        bullet.setRotation(bearing);
//        bullets.add(bullet);
//        if (this.getGameObject().getComponent(BulletDamage.class).getPowerUpExists()) {
//          bullet.getComponent(Damage.class).setDamage(20);
//        }
//        //moves forwards until collision happens then it will be destroyed.
//        //constantly check if collided() method is returning true
//      } catch (InvalidClassInstance | ComponentExistsException e) {
//        this.logger.error(e);
//        this.logger.error("Failed to create bullet.");
//      }
//    }
//  }
//
//  @Override
//  public TurretOutput tick() {
//    this.logger.trace("Ticking...");
//    this.logger.trace(bullets.size() + " bullets");
//    TurretOutput output = new TurretOutput();
//    for (BulletObject bullet : bullets) {
//      Entity collision = bullet.move();
//      this.logger.info(collision);
//      switch (collision.getType()) {
//        case NONE:
//          continue;
////        case TANK:
////          getGameObject().findObject(collision.getName()).getComponent(Health.class).hit(bullet.getComponent(Damage.class).getDamage());
////          getGameObject().findObject(collision.getName()).destroy();
//        default:
//          output.addCollision(collision);
//          bullet.destroy();
//      }
//    }
//    return output;
//  }
//
//  @Override
//  public void addFrame(final Input frame) {
//  }
//
//  @Override
//  public void addAndConsume(final Input frame) {
//  }
//}
