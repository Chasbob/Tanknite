package com.aticatac.client.util;

public class SoundManager {

  private boolean soundOn = true;

  /**
   *
   */
  public void onShooting(){

    if(soundOn) {
      AudioEnum.INSTANCE.getShoot().play();
    }

  }

  /**
   *
   */
  public void onTankMove(){

    if(soundOn) {
      AudioEnum.INSTANCE.getTankMove().play();
    }

  }

  /**
   *
   */
  public void onPowerUp(){

    if(soundOn) {
      AudioEnum.INSTANCE.getPowerUp().play();
    }

  }

  /**
   *
   */
  public void onTankDeath(){

    if(soundOn) {
      AudioEnum.INSTANCE.getTankDeath().play();
    }

  }

  /**
   *
   */
  public void onButtonClick(){

    if(soundOn) {
      AudioEnum.INSTANCE.getButtonClick().play();
    }

  }



}
