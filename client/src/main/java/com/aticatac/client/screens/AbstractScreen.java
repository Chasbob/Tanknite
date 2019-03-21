package com.aticatac.client.screens;

import com.aticatac.client.util.ListenerFactory;
import com.aticatac.client.util.Styles;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import org.apache.log4j.Logger;

/**
 * The type Abstract screen.
 */
public class AbstractScreen extends Stage implements Screen {
  /**
   * The Logger.
   */
  protected final Logger logger;
  Table rootTable;

  /**
   * Instantiates a new Abstract screen.
   */
  AbstractScreen() {
    super(new ExtendViewport(640, 640));
    logger = Logger.getLogger(getClass());
  }

  /**
   * Build stage.
   */
  public void buildStage(){
    //create root table
    rootTable = new Table();
    rootTable.setFillParent(true);
    Styles.INSTANCE.addTableColour(rootTable, Color.valueOf("363636"));
    addActor(rootTable);
    //create table to store back button
    Table backTable = new Table();
    backTable.setFillParent(true);
    rootTable.addActor(backTable);
    backTable.bottom();
    //create back button
    TextButton backButton = Styles.INSTANCE.createBackButton("BACK");
    backTable.add(backButton).bottom().padBottom(10);
    backButton.addListener(ListenerFactory.newListenerEvent(() -> {
      refresh();
      Screens.INSTANCE.showScreen(MainMenuScreen.class);
      return false;
    }));
  }

  void addToRoot(Table table){
    table.setFillParent(true);
    rootTable.addActor(table);
  }

  Table createTopLabelTable(Table dataTable){
    addToRoot(dataTable);
    Table topLabelTable = new Table();
    topLabelTable.setFillParent(true);
    dataTable.addActor(topLabelTable);
    topLabelTable.top().padTop(50);
    return topLabelTable;
  }

  public void refresh(){

  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(this);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
