package com.gdx.game;

import com.gdx.screens.MainMenuScreen;


public class GDXGame extends com.badlogic.gdx.Game {

    @Override
    public void create() {
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }
}
