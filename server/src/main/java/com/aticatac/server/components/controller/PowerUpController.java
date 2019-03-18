//package com.aticatac.server.components.controller;
//
//import com.aticatac.common.components.Component;
//import com.aticatac.common.components.transform.Position;
//import com.aticatac.common.exceptions.ComponentExistsException;
//import com.aticatac.common.exceptions.InvalidClassInstance;
//import com.aticatac.common.objectsystem.GameObject;
//import com.aticatac.server.components.DataServer;
//import com.aticatac.server.gamemanager.Manager;
//import com.aticatac.server.powerupobjects.AmmoObject;
//import com.aticatac.server.powerupobjects.DamageObject;
//import com.aticatac.server.powerupobjects.HealthObject;
//import com.aticatac.server.powerupobjects.SpeedObject;
//
//import java.util.Random;
//
//public class PowerUpController extends Component{
//
//  public PowerUpController(GameObject gameobject) {
//    super(gameobject);
//
//  }
//
//  public void spawnPowerUps(){
//
//    new Thread(() -> {
//      while (true) {
//
//        int randomTime = new Random().nextInt(15);
//
//        //delay for randomTime
//        try {
//          Thread.sleep(randomTime * 1000);
//        } catch (InterruptedException ex) {
//          Thread.currentThread().interrupt();
//        }
//        int powerUpId = new Random().nextInt(4);
//
//        switch (powerUpId) {
//          case 0:
//            HealthObject healthPowerUp = null; // get name of powerUp
//            try {
//              healthPowerUp = new HealthObject("healthPowerUp", this.getGameObject());
//            } catch (InvalidClassInstance | ComponentExistsException invalidClassInstance) {
//              invalidClassInstance.printStackTrace();
//            }
//            break;
//
//          case 1:
//            AmmoObject ammoPowerUp = null; // get name of powerUp
//            try {
//              ammoPowerUp = new AmmoObject("ammoPowerUp", this.getGameObject());
//            } catch (InvalidClassInstance | ComponentExistsException invalidClassInstance) {
//              invalidClassInstance.printStackTrace();
//            }
//            break;
//
//          case 2:
//            DamageObject damagePowerUp = null; // get name of powerUp
//            try {
//              damagePowerUp = new DamageObject("damagePowerUp", this.getGameObject());
//            } catch (InvalidClassInstance | ComponentExistsException invalidClassInstance) {
//              invalidClassInstance.printStackTrace();
//            }
//            break;
//
//          case 3:
//            SpeedObject speedPowerUp = null; // get name of powerUp
//            try {
//              speedPowerUp = new SpeedObject("speedPowerUp", this.getGameObject());
//            } catch (InvalidClassInstance | ComponentExistsException invalidClassInstance) {
//              invalidClassInstance.printStackTrace();
//            }
//            break;
//        }
//      }
//    }
//    );
//
//
//
//  }
  // need to return powerUp?
//  public void createPowerUp (String powerUp){
//
////          while (DataServer.INSTANCE.getOccupiedCoordinates().containsKey(position)) {
////            position = new Position(ThreadLocalRandom.current().nextInt(Manager.INSTANCE.getMin(), Manager.INSTANCE.getMax() + 1),
////                ThreadLocalRandom.current().nextInt(Manager.INSTANCE.getMin(), Manager.INSTANCE.getMax() + 1));
////          } // TODO: check co ordinate is unoccupied and set as occupied position in DataServer
//
//
//  }



  //created as component for map before game starts, and have spawnPowerUps() method called
  // TODO: Have power ups actually be created as entities
  //after set time, picks random powerup, creates object of it and decides co ordinates of it on the map

//}
