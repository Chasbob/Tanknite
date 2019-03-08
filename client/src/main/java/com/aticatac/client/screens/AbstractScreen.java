package com.aticatac.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import org.apache.log4j.Logger;

/**
 * The type Abstract screen.
 */
abstract class AbstractScreen extends Stage implements Screen {
  /**
   * The Logger.
   */
  protected final Logger logger;
  private Class prevScreen;

  /**
   * Instantiates a new Abstract screen.
   */
  AbstractScreen() {
    super(new StretchViewport(640, 640));
    //this.prevScreen = (Class<T>) MainMenuScreen.class;
    logger = Logger.getLogger(getClass());
  }

  /**
   * Build stage.
   */
//Subclasses must load actors in this method
  public abstract void buildStage();

  @Override
  public void show() {
    Gdx.input.setInputProcessor(this);
  }

  @Override
  public void render(float delta) {
    // Clear screen todo?
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
