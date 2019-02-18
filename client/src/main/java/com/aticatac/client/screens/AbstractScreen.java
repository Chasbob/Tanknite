package com.aticatac.client.screens;

import com.aticatac.client.util.ScreenEnum;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * The type Abstract screen.
 */
public abstract class AbstractScreen extends Stage implements Screen {
    private ScreenEnum prevScreen;

    /**
     * Instantiates a new Abstract screen.
     */
    AbstractScreen() {
        super(new StretchViewport(640, 640));
        this.prevScreen = ScreenEnum.NONE;
    }

    /**
     * Gets prev screen.
     *
     * @return the prev screen
     */
    public ScreenEnum getPrevScreen() {
        return prevScreen;
    }

    /**
     * Sets prev screen.
     *
     * @param prevScreen the prev screen
     */
    public void setPrevScreen(ScreenEnum prevScreen) {
        this.prevScreen = prevScreen;
    }

    /**
     * Build stage.
     */
// Subclasses must load actors in this method
    public abstract void buildStage();

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Calling to Stage methods
        super.act(delta);
        super.draw();
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}
