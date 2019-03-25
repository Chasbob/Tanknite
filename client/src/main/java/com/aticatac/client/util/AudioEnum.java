package com.aticatac.client.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import org.apache.log4j.Logger;

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
  private boolean sound = true;

  /***/
  private boolean music = true;
  /***/
  private float soundVolume = 1;
  /***/
  private float musicVolume = 0.5f;

  /**
   *
   */
  AudioEnum() {

    this.logger = Logger.getLogger(getClass());
    this.logger.trace("boo");
    loadSound();

  }

  public boolean isSound() {
    return sound;
  }

  public boolean isMusic() {
    return music;
  }

  /**
   * @param sound
   */
  public void setSound(boolean sound) {

    this.sound = sound;

  }

  /**
   * @param music
   */
  public void setMusic(boolean music) {

    this.music = music;

  }

  /**
   * @param volume
   */
  public void setSoundVolume(float volume) {

    soundVolume = volume;

  }

  /**
   *
   */
  public void loadSound() {

    shoot = Gdx.audio.newMusic(Gdx.files.internal("audio/Tank Firing-SoundBible.com-998264747.mp3"));

    tankMove = Gdx.audio.newMusic(Gdx.files.internal("audio/Tank-SoundBible.com-1359027625.mp3"));

    powerUp = Gdx.audio.newMusic(Gdx.files.internal("audio/Power-Up-KP-1879176533.mp3"));

    tankDeath = Gdx.audio.newMusic(Gdx.files.internal("audio/Bomb_Exploding-Sound_Explorer-68256487.mp3"));

    buttonClick = Gdx.audio.newMusic(Gdx.files.internal("audio/Gun_Cocking_Slow-Mike_Koenig-1019236976.mp3"));

    theme = Gdx.audio.newMusic(Gdx.files.internal("audio/Puzzle-Land.mp3"));

  }

  /**
   * @return
   */
  public Music getTheme() {

    if (music) {
      theme.setVolume(musicVolume);
      theme.play();
    }
    return theme;

  }

  /**
   * @return
   */
  public Music getBackgroundMusic() {

    if (music) {
      theme.setVolume(musicVolume);
      theme.setLooping(true);
      theme.play();
    }
    return theme;

  }

  /**
   * @return
   */
  public Music getShoot() {

    if (!shoot.isPlaying() && sound) {
      shoot.setVolume(soundVolume);
      shoot.play();
    }
    return shoot;
  }

  /**
   * @return
   */
  //can play this even if it is being played elsewhere
  public Music getOtherTankShoot(float volume) {

    if (sound) {
      shoot.setVolume(volume);
      shoot.play();
    }
    return shoot;
  }

  /**
   * @return
   */
  public Music getTankMove() {

    if (!tankMove.isPlaying() && sound) {
      tankMove.setVolume(soundVolume);
      tankMove.play();
    }
    return tankMove;
  }

  /**
   * @return
   */
  public Music getPowerUp() {

    if (!powerUp.isPlaying() && sound) {
      powerUp.setVolume(soundVolume);
      powerUp.play();
    }
    return powerUp;
  }

  /**
   * @return
   */
  public Music getTankDeath() {

    if (!tankDeath.isPlaying() && sound) {
      tankDeath.setVolume(soundVolume);
      tankDeath.play();
    }
    return tankDeath;
  }


  /**
   *
   */
  public Music getButtonClick() {

    System.out.println(sound);
    if (!buttonClick.isPlaying() && sound) {
      buttonClick.setVolume(soundVolume);
      buttonClick.play();
    }
    return buttonClick;
  }


}

