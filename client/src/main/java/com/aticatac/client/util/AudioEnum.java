package com.aticatac.client.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import org.apache.log4j.Logger;

import java.lang.management.MemoryUsage;

/**
 * The enum Audio enum.
 */
public enum AudioEnum {

  INSTANCE;

  /***/
  private final Logger logger;

  /***/
  private Music shoot;
  /***/
  private Music tankMove;
  /***/
  private Music powerUp;
  /***/
  private Music tankDeath;
  /***/
  private Music buttonClick;
  /***/
  private Music theme;

  /***/
  private boolean soundOn = true;
  /***/
  private float soundVolume = 1;
  /***/
  private float musicVolume = 0.5f;

  /**
   *
   */
  AudioEnum(){

    this.logger = Logger.getLogger(getClass());
    this.logger.trace("boo");
    loadSound();

  }


  /**
   *
   * @param sound
   */
  public void setSoundOn(boolean sound){

    soundOn = sound;

  }

  /**
   *
   * @param volume
   */
  public void setSoundVolume(float volume){

    soundVolume = volume;

  }

  /**
   *
   */
  public void loadSound(){

    shoot = Gdx.audio.newMusic(Gdx.files.internal("audio/Tank Firing-SoundBible.com-998264747.wav"));

    tankMove = Gdx.audio.newMusic(Gdx.files.internal("audio/Tank-SoundBible.com-1359027625.mp3"));

    powerUp = Gdx.audio.newMusic(Gdx.files.internal("audio/Power-Up-KP-1879176533.mp3"));

    tankDeath = Gdx.audio.newMusic(Gdx.files.internal("audio/Bomb_Exploding-Sound_Explorer-68256487.mp3"));

    buttonClick = Gdx.audio.newMusic(Gdx.files.internal("audio/Gun_Cocking_Slow-Mike_Koenig-1019236976.mp3"));

    theme = Gdx.audio.newMusic(Gdx.files.internal("audio/Puzzle-Land.mp3"));

  }

  /**
   *
   * @return
   */
  public Music getTheme(){

    if(soundOn) {
      theme.setVolume(musicVolume);
      theme.play();
    }
    return theme;

  }

  /**
   *
   * @return
   */
  public Music getBackgroundMusic(){

    if(soundOn) {
      theme.setVolume(musicVolume);
      theme.setLooping(true);
      theme.play();
    }
    return theme;

  }

  /**
   *
   * @return
   */
  public Music getShoot() {

    if(!shoot.isPlaying() && soundOn) {
      shoot.setVolume(soundVolume);
      shoot.play();
    }
    return shoot;
  }

  /**
   *
   * @return
   */
  public Music getTankMove() {

    if(!tankMove.isPlaying() && soundOn) {
      tankMove.setVolume(soundVolume);
      tankMove.play();
    }
    return tankMove;
  }

  /**
   *
   * @return
   */
  public Music getPowerUp() {

    if(!powerUp.isPlaying() && soundOn) {
      powerUp.setVolume(soundVolume);
      powerUp.play();
    }
    return powerUp;
  }

  /**
   *
   * @return
   */
  public Music getTankDeath() {

    if(!tankDeath.isPlaying()&& soundOn) {
      tankDeath.setVolume(soundVolume);
      tankDeath.play();
    }
    return tankDeath;
  }


  /**
   *
   */
  public Music getButtonClick(){

    if(!buttonClick.isPlaying() && soundOn) {
      buttonClick.setVolume(soundVolume);
      buttonClick.play();
    }
    return buttonClick;
  }


}
