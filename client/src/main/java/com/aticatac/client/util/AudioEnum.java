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
  private Music main;


  /***/
  private boolean sound = true;

  /***/
  private boolean music = true;
  /***/
  private float soundVolume = 0.6f;
  /***/
  private float moveVolume = 0.4f;
  /***/
  private float musicVolume = 0.4f;
  /***/
  private float mainVolume = 0.1f;

  /**
   *
   */
  AudioEnum() {

    this.logger = Logger.getLogger(getClass());
    this.logger.trace("boo");
    loadSound();

  }

  /**
   *
   * @return
   */
  public boolean isSound() {
    return sound;
  }

  /**
   *
   * @return
   */
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
   * @param volume
   */
  public void setMusicVolume(float volume) {

    musicVolume = volume;

  }

  public float getSoundVolume() {
    return soundVolume;
  }

  public float getMusicVolume() {
    return musicVolume;
  }

  public float getMainVolume() {
    return mainVolume;
  }

  /**
   *
   */
  public void loadSound() {

    shoot = Gdx.audio.newMusic(Gdx.files.internal("audio/Tank Firing-SoundBible.com-998264747.mp3"));

    tankMove = Gdx.audio.newMusic(Gdx.files.internal("audio/TankMovement-SoundBible.com-1359027625.mp3"));

    powerUp = Gdx.audio.newMusic(Gdx.files.internal("audio/Coin_Drop-Willem_Hunt-569197907-[AudioTrimmer.com].mp3"));

    tankDeath = Gdx.audio.newMusic(Gdx.files.internal("audio/Bomb_Exploding-Sound_Explorer-68256487.mp3"));

    buttonClick = Gdx.audio.newMusic(Gdx.files.internal("audio/ButtonClick.mp3"));

    theme = Gdx.audio.newMusic(Gdx.files.internal("audio/arcade-music-loop.wav"));

    main = Gdx.audio.newMusic(Gdx.files.internal("audio/Impulse.mp3"));

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
  public Music getMain() {

    if (music) {
      theme.setVolume(mainVolume);
      theme.play();
    }
    return theme;

  }

  /**
   * @return
   */
  public void stopMain() {

    main.stop();

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

    try {
      Music otherShoot = Gdx.audio.newMusic(Gdx.files.internal("audio/Tank Firing-SoundBible.com-998264747.wav"));
      if (sound) {
        otherShoot.setVolume(volume);
        otherShoot.play();
      }
      return otherShoot;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * @return
   */
  public Music getTankMove() {

    if (!tankMove.isPlaying() && sound) {
      tankMove.setVolume(moveVolume);
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

    if (!buttonClick.isPlaying() && sound) {
      buttonClick.setVolume(soundVolume);
      buttonClick.play();
    }
    return buttonClick;
  }


}

