package com.aticatac.server.components.controller;

import com.aticatac.common.components.Component;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.components.DataServer;
import com.aticatac.server.gamemanager.Manager;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PowerUpController extends Component{

  public PowerUpController(GameObject gameobject) {
    super(gameobject);

  }

  public void createPowerUps(){

    while (true) {

      int randomTime = new Random().nextInt(15);

      //delay for randomTime
      try {
        Thread.sleep(randomTime * 1000);
      }
      catch (InterruptedException ex){
        Thread.currentThread().interrupt();
      }

      int powerUpId = new Random().nextInt(4);
      switch (powerUpId) {
        case 0:
//          while (DataServer.INSTANCE.getOccupiedCoordinates().containsKey(position)) {
//            position = new Position(ThreadLocalRandom.current().nextInt(Manager.INSTANCE.getMin(), Manager.INSTANCE.getMax() + 1),
//                ThreadLocalRandom.current().nextInt(Manager.INSTANCE.getMin(), Manager.INSTANCE.getMax() + 1));
//          } // TODO: set as occupied position in DataServer
          break;

        case 1:
          // create ammo
          break;

        case 2:
          // create bullet damage
          break;

        case 3:
          // create speed
          break;
      }
    }



  }



  //created as component for map before game starts
  // TODO: Have power ups actually be created as gameObjects
  //after set time, picks random powerup, creates object of it and decides co ordinates of it on the map

}
