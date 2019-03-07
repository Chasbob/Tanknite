package com.aticatac.client.util;

public class MusicManager {

  public boolean musicOn = true;

  /**
   *
   */
  public void onGameLoad(){

    if(musicOn) {
      AudioEnum.INSTANCE.getTheme().play();
    }

  }

  /**
   *
   */
  public void onGameStart(){

    if(musicOn) {
      AudioEnum.INSTANCE.getBackgroundMusic().play();
    }

  }






}
