package com.aticatac.server.components.controller;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;

public class PowerUpController extends Component{

  public PowerUpController(GameObject gameobject) {
    super(gameobject);
  }


  //created as component for map before game starts
  //thread which spawns power ups based on random interval of time
  //after set time, picks random powerup, creates object of it and decides co ordinates of it on the map

}
