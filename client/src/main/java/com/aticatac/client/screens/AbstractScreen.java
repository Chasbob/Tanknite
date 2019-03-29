package com.aticatac.client.screens;

import com.aticatac.client.util.ListenerFactory;
import com.aticatac.client.util.MenuTable;
import com.aticatac.client.util.Styles;
import com.aticatac.common.mappers.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.google.common.eventbus.Subscribe;
import org.apache.log4j.Logger;

import static com.aticatac.client.bus.EventBusFactory.eventBus;

/**
 * The type Abstract screen.
 */
public class AbstractScreen extends Stage implements Screen {
  /**
   * The Logger.
   */
  protected final Logger logger;
  /**
   * The Root table.
   */
  Table rootTable;
  /**
   * The Back button.
   */
  TextButton backButton;
  /**
   * The Player.
   */
  protected Player player;

  /**
   * Instantiates a new Abstract screen.
   */
  AbstractScreen() {
    super(new StretchViewport(640, 640));
    eventBus.register(this);
    logger = Logger.getLogger(getClass());
    rootTable = new Table();
  }

  @Subscribe
  private void playerUpdate(Player player) {
    this.logger.info("update player");
    this.player = player;
  }

  /**
   * Build stage.
   */
  public void buildStage() {
    Cursor customCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("img/gun-pointer.png")), 16, 16);
    Gdx.graphics.setCursor(customCursor);
    //create root table
    rootTable.setFillParent(true);
    Styles.INSTANCE.addTableColour(rootTable, Color.valueOf("363636"));
    addActor(rootTable);
    //create table to store back button
    Table backTable = new Table();
    backTable.setFillParent(true);
    rootTable.addActor(backTable);
    backTable.bottom();
    //create back button
    backButton = Styles.INSTANCE.createBackButton("BACK");
    backTable.add(backButton).bottom().padBottom(10);
    backButton.addListener(ListenerFactory.newListenerEvent(() -> {
      refresh();
      Screens.INSTANCE.showScreen(MainMenuScreen.class);
      return false;
    }));
  }

  /**
   * Add to root.
   *
   * @param table the table
   */
  void addToRoot(Table table) {
    table.setFillParent(true);
    rootTable.addActor(table);
  }

  /**
   * Refresh.
   */
  public void refresh() {

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

  /**
   * Apply index int.
   *
   * @param group       the group
   * @param index       the index
   * @param downOrRight the down or right
   * @return the int
   */
  int applyIndex(Group group, int index, boolean downOrRight) {
    if (downOrRight) {
      if (index == group.getChildren().size - 1) {
        index = 0;
      } else {
        index++;
      }
    } else {
      if (index == 0) {
        index = group.getChildren().size - 1;
      } else {
        index--;
      }
    }
    return index;
  }
}
