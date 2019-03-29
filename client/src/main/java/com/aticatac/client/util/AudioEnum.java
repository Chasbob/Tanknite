package com.aticatac.client.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import org.apache.log4j.Logger;

/**
 * The enum Audio enum.
 */
public enum AudioEnum {

  /**
   * Instance audio enum.
   */
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
    //music = false;
    //sound = false;
    this.logger = Logger.getLogger(getClass());
    this.logger.trace("boo");
    loadSound();

  }

  /**
   * Is sound boolean.
   *
   * @return boolean
   */
  public boolean isSound() {
    return sound;
  }

  /**
   * Is music boolean.
   *
   * @return boolean
   */
  public boolean isMusic() {
    return music;
  }

  /**
   * Sets sound.
   *
   * @param sound the sound
   */
  public void setSound(boolean sound) {

    this.sound = sound;

  }

  /**
   * Sets music.
   *
   * @param music the music
   */
  public void setMusic(boolean music) {

    this.music = music;

  }

  /**
   * Sets sound volume.
   *
   * @param volume the volume
   */
  public void setSoundVolume(float volume) {

    soundVolume = volume;

  }

  /**
   * Sets music volume.
   *
   * @param volume the volume
   */
  public void setMusicVolume(float volume) {

    musicVolume = volume;

  }

  /**
   * Gets sound volume.
   *
   * @return the sound volume
   */
  public float getSoundVolume() {
    return soundVolume;
  }

  /**
   * Gets music volume.
   *
   * @return the music volume
   */
  public float getMusicVolume() {
    return musicVolume;
  }

  /**
   * Gets main volume.
   *
   * @return the main volume
   */
  public float getMainVolume() {
    return mainVolume;
  }

  /**
   * Load sound.
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
   * Gets theme.
   *
   * @return theme
   */
  public Music getTheme() {

    if (music) {
      theme.setVolume(musicVolume);
      theme.play();
    }
    return theme;

  }

  /**
   * Gets main.
   *
   * @return main
   */
  public Music getMain() {

    if (music) {
      theme.setVolume(mainVolume);
      theme.play();
    }
    return theme;

  }

  /**
   * Stop main.
   *
   * @return
   */
  public void stopMain() {

    theme.stop();

  }

  /**
   * Gets shoot.
   *
   * @return shoot
   */
  public Music getShoot() {

    if (!shoot.isPlaying() && sound) {
      shoot.setVolume(soundVolume);
      shoot.play();
    }
    return shoot;
  }

  /**
   * Gets other tank shoot.
   *
   * @param volume the volume
   * @return other tank shoot
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
   * Gets tank move.
   *
   * @return tank move
   */
  public Music getTankMove() {

    if (!tankMove.isPlaying() && sound) {
      tankMove.setVolume(moveVolume);
      tankMove.play();
    }
    return tankMove;
  }

  /**
   * Gets power up.
   *
   * @return power up
   */
  public Music getPowerUp() {

    if (!powerUp.isPlaying() && sound) {
      powerUp.setVolume(soundVolume);
      powerUp.play();
    }
    return powerUp;
  }

  /**
   * Gets tank death.
   *
   * @return tank death
   */
  public Music getTankDeath() {

    if (!tankDeath.isPlaying() && sound) {
      tankDeath.setVolume(soundVolume);
      tankDeath.play();
    }
    return tankDeath;
  }


  /**
   * Gets button click.
   *
   * @return the button click
   */
  public Music getButtonClick() {

    if (!buttonClick.isPlaying() && sound) {
      buttonClick.setVolume(soundVolume);
      buttonClick.play();
    }
    return buttonClick;
  }
}

