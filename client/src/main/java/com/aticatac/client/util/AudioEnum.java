package com.aticatac.client.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import org.apache.log4j.Logger;

public enum AudioEnum {

    INSTANCE;

    private final Logger logger;

    private Sound shoot;
    private Sound tankMove;
    private Sound powerUp;
    private Sound tankDeath;

    private Sound buttonClick;

    private Music theme;

    /**
     *
     */
    AudioEnum(){

        this.logger = Logger.getLogger(getClass());
        this.logger.trace("boo");

    }

    /**
     *
     */
    public void loadSound(){

        shoot = Gdx.audio.newSound(Gdx.files.internal("audio/Tank Firing-SoundBible.com-998264747.mp3"));

        tankMove = Gdx.audio.newSound(Gdx.files.internal("audio/Tank-SoundBible.com-1359027625.mp3"));

        powerUp = Gdx.audio.newSound(Gdx.files.internal("audio/Power-Up-KP-1879176533.mp3"));

        tankDeath = Gdx.audio.newSound(Gdx.files.internal("audio/Bomb_Exploding-Sound_Explorer-68256487.mp3"));

        buttonClick = Gdx.audio.newSound(Gdx.files.internal("audio/Gun_Cocking_Slow-Mike_Koenig-1019236976.mp3"));

        theme = Gdx.audio.newMusic(Gdx.files.internal("audio/Puzzle-Land.mp3"));

    }

    /**
     *
     * @return
     */
    public Music getTheme(){

        return theme;

    }

    /**
     *
     * @return
     */
    public Music getBackgroundMusic(){

        return theme;

    }

    /**
     *
     * @return
     */
    public Sound getShoot() {
        return shoot;
    }

    /**
     *
     * @return
     */
    public Sound getTankMove() {
        return tankMove;
    }

    /**
     *
     * @return
     */
    public Sound getPowerUp() {
        return powerUp;
    }

    /**
     *
     * @return
     */
    public Sound getTankDeath() {
        return tankDeath;
    }


    /**
     *
     */
    public Sound getButtonClick(){

        return buttonClick;
    }

    //theme and background music and collision music






}
