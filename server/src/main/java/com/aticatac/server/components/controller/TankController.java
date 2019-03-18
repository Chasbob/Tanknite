//package com.aticatac.server.components.controller;
//
//import com.aticatac.common.model.CommandModel;
//import com.aticatac.server.components.Acceleration;
//import com.aticatac.server.components.Ammo;
//import com.aticatac.server.components.BulletDamage;
//import com.aticatac.server.components.Component;
//import com.aticatac.server.components.DataServer;
//import com.aticatac.server.components.Health;
//import com.aticatac.server.components.physics.Entity;
//import com.aticatac.server.components.physics.Physics;
//import com.aticatac.server.components.physics.PhysicsResponse;
//import com.aticatac.server.components.transform.Position;
//import com.aticatac.server.components.transform.Transform;
//import com.aticatac.server.objectsystem.GameObject;
//import com.aticatac.server.objectsystem.Entity;
//// components for server side make in server or import from common?
//// needs component of Physics
//
///**
// * The type TankController.
// */
//public class TankController extends Component {
//  public TankController() {
//    super();
//  }
//  Transform transform;
//
//  /**
//   * Instantiates a new Component.
//   *
//   * @param gameObject the component parent
//   */
//  public TankController(GameObject gameObject) {
//    super(gameObject);
//  }
//  //when sound plays check all tanks in that area and play noise to all those players
//
//  public void move(CommandModel command) {
//    move(command.getBearing());
//  }
//
//  public void move(int bearing) {
//
//    Position oldPosition = this.getGameObject().getComponent(Transform.class).getPosition();
//    PhysicsResponse physicsData = this.getGameObject().getComponent(Physics.class).move(bearing);
//    if (!oldPosition.equals(physicsData.getPosition())) {
//      this.getGameObject().getComponent(Transform.class).setPosition(physicsData.getPosition().getX(), physicsData.getPosition().getY());
//      DataServer.INSTANCE.setCoordinates(physicsData.getPosition(), this.getGameObject().getEntity());
//      this.getGameObject().getComponent(Transform.class).setRotation(0);
//      powerUpCheck(oldPosition, physicsData, physicsData.getPosition());
//    }
//  }
//
//  private void powerUpCheck(Position oldPosition, PhysicsResponse physicsData, Position newPosition) {
//    DataServer.INSTANCE.setCoordinates(newPosition, this.getGameObject().getEntity(), oldPosition);
//    if (physicsData.getEntity().getType() == Entity.EntityType.AMMO_POWERUP) {
//      pickUpAmmo();
//      // destroy powerup object too
//    }
//    if (physicsData.getEntity().getType() == Entity.EntityType.HEALTH_POWERUP) {
//      pickUpHealth();
//    }
//    if (physicsData.getEntity().getType() == Entity.EntityType.SPEED_POWERUP) {
//      pickUpSpeed();
//    }
//    if (physicsData.getEntity().getType() == Entity.EntityType.BULLET_POWERUP) {
//      pickUpDamage();
//    }
//  }
//
//  /**
//   * Shoot boolean.
//   *
//   * @return the boolean
//   */
//// call method when space bar pressed
//  public void shoot(CommandModel model) {
//    int currentAmmo = this.getGameObject().getComponent(Ammo.class).getAmmo();
//    if (currentAmmo > 0) {
//      this.getGameObject().getComponent(TurretController.class).shoot(model);
//    }
//  }
//
//  /**
//   * Is shot.
//   */
//  public void isShot(int damage) {
//    int currentHealth = this.getGameObject().getComponent(Health.class).getHealth();
//    int newHealth = currentHealth - damage;
//    this.getGameObject().getComponent(Health.class).setHealth(newHealth);
//    if (newHealth > 0 && newHealth <= 10) {
//      dying();
//    } else if (newHealth <= 0) {
//      die();
//    }
//  }
//
//  public void dying() {
////        // can no longer move and will die in 20 seconds
//    //Physics will check the health and prevent it moving if less than 10
////        //delay
////        die();
//  }
//
//  /**
//   * Die.
//   */
//  public void die() {
//    //remove it from data coordinates
//    DataServer.INSTANCE.deleteCoordinates(this.getGameObject().getComponent(Transform.class).getPosition());
//    //add a powerup into the data that says that this is a power up with location.
//    //DataServer.INITIALISE.setOccupiedCoordinates("ammopowerup", this.getGameObject.getComponent(Transform.class).GetPosition());
//    //TODO make this thread safe
//    DataServer.INSTANCE.setPlayerCount(DataServer.INSTANCE.getPlayerCount() - 1);
//  }
//
//  /**
//   * Pick up health.
//   */
//  public void pickUpHealth() {
//    int currentHealth = this.getGameObject().getComponent(Health.class).getHealth();
//    int newHealth = currentHealth + 10;
//    if (newHealth > this.getGameObject().getComponent(Health.class).getMaxHealth()) {
//      this.getGameObject().getComponent(Health.class).setHealth(this.getGameObject().getComponent(Health.class).getMaxHealth());
//    } else {
//      this.getGameObject().getComponent(Health.class).setHealth(newHealth);
//    }
//    // only gain health up to maximum
//  }
//
//  /**
//   * Pick up ammo.
//   */
//  public void pickUpAmmo() {
//    int currentAmmo = this.getGameObject().getComponent(Ammo.class).getAmmo();
//    this.getGameObject().getComponent(Ammo.class).setAmmo(currentAmmo + 10);
//  }
//
//  /**
//   * Pick up speed.
//   */
//  public void pickUpSpeed() {
//    this.getGameObject().getComponent(Acceleration.class).setPowerUpExists(true);
//    //make this a thread which waits for certain time then
//    // this.getGameObject().getComponent(Acceleration.class).setPowerUpExists(false);
//  }
//
//  /**
//   * Pick up damage.
//   */
//  public void pickUpDamage() {
//    this.getGameObject().getComponent(BulletDamage.class).setPowerUpExists(true);
//    //make this a thread which waits for certain time then
//    // this.getGameObject().getComponent(BulletDamage.class).setPowerUpExists(false);
//  }
//}