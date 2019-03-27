package com.aticatac.client.screens;

import com.aticatac.client.util.*;
import com.aticatac.common.model.Command;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.objectsystem.Container;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

/**
 * The type Game screen.
 */
public class GameScreen extends AbstractScreen {
  private final SpriteBatch healthBarBatch;
  private final SpriteBatch tanks;
  private final SpriteBatch tanksMiniMap;
  private final SpriteBatch minimapBackGround;
  private final int maxX;
  private final int maxY;
  private Update update;
  Table popUpTable;
  VerticalGroup verticalGroup;
  private Table alertTable;
  private Table killLogTable;
  private Label ammoValue;
  private Label killCount;
  private Label playerCount;
  private float health;
  private TiledMap map;
  private OrthogonalTiledMapRenderer renderer;
  private Camera camera;
  private MinimapViewport minimapViewport;
  private Label fpsValue;
  private Label tankXY;
  private Texture tankTexture;
  private Label direction;
  private Container player;
  private HudUpdate hudUpdate;
  private boolean tractionHealth;
  boolean tractionPopUp;

  /**
   * Instantiates a new Game screen.
   */
  GameScreen() {
    super();
    maxX = 1920;
    maxY = 1920;
    try {
      player = new Container();
      ammoValue = Styles.INSTANCE.createLabel("");
      killCount = Styles.INSTANCE.createLabel(" 0 ");
      playerCount = Styles.INSTANCE.createLabel(" 1 ");
      fpsValue = Styles.INSTANCE.createLabel("");
      tankXY = Styles.INSTANCE.createLabel("");
      direction = Styles.INSTANCE.createLabel("");
      map = new TmxMapLoader().load("maps/map.tmx");
      tankTexture = new Texture("img/tank.png");
      tractionHealth = true;
      tractionPopUp = true;
      renderer = new OrthogonalTiledMapRenderer(map);
      minimapViewport = new MinimapViewport(0.2f, 0.025f, new OrthographicCamera());
      minimapViewport.setWorldSize(maxX, maxY);
      this.camera = new Camera(maxX, maxY, 640, 640);
      Gdx.input.setInputProcessor(this);
    } catch (Exception e) {
      e.printStackTrace();

    }
    healthBarBatch = new SpriteBatch();
    tanks = new SpriteBatch();
    tanksMiniMap = new SpriteBatch();
    minimapBackGround = new SpriteBatch();
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);
    this.camera.getViewport().update(width, height);
    this.minimapViewport.update(width, height, true);
  }

  @Override
  public void buildStage() {
    //create root table
    rootTable = new Table();
    rootTable.setFillParent(true);
    addActor(rootTable);
    Gdx.graphics.setVSync(true);
    //create table and labels for player count and kills - TOP LEFT
    super.addToRoot(createHudTopLeft());
    //create table for kill feed - BOTTOM LEFT
    super.addToRoot(createHudBottomLeft());
    //create table for ammo - BOTTOM RIGHT
    super.addToRoot(createHudBottomRight());
    //create alert table - BOTTOM MIDDLE
    super.addToRoot(createHudAlertTable());
    hudUpdate = new HudUpdate(killLogTable, ammoValue, playerCount, killCount);
  }

  private Table createHudTopLeft() {
    Table topLeftTable = new Table();
    topLeftTable.top().left();
    topLeftTable.defaults().padTop(10).padLeft(10).left();
    Table aliveTable = new Table();
    Table aliveLabelTable = new Table();
    Styles.INSTANCE
      .addTableColour(aliveLabelTable, Color.GRAY);
    Label aliveLabel = Styles.INSTANCE.createLabel("Alive");
    aliveLabelTable.add(aliveLabel);
    Table playerCountTable = new Table();
    playerCountTable.add(playerCount).center();
    Styles.INSTANCE
      .addTableColour(playerCountTable, new Color(0f, 0f, 0f, 0.5f));
    aliveTable.add(playerCountTable);
    aliveTable.add(aliveLabelTable);
    Table killTable = new Table();
    Table killLableTable = new Table();
    Styles.INSTANCE
      .addTableColour(killLableTable, Color.GRAY);
    Label killLabel = Styles.INSTANCE.createLabel("Killed");
    killLableTable.add(killLabel);
    Table killCountTable = new Table();
    Styles.INSTANCE
      .addTableColour(killCountTable, new Color(0f, 0f, 0f, 0.5f));
    killCountTable.add(killCount);
    killTable.add(killCountTable);
    killTable.add(killLableTable);
    topLeftTable.add(aliveTable);
    topLeftTable.add(killTable);
    return topLeftTable;
  }

  private Table createHudBottomLeft() {
    killLogTable = new Table();
    killLogTable.bottom().left();
    killLogTable.defaults().padTop(10).padLeft(10).padBottom(20).left();
    Label tempKill = Styles.INSTANCE.createLabel("");
    killLogTable.add(tempKill);
    return killLogTable;
  }

  private Table createHudBottomRight() {
    Table bottomRightTable = new Table();
    bottomRightTable.bottom().right();
    bottomRightTable.defaults().padRight(10).padTop(10).padBottom(20).left();
    Table ammoTable = new Table();
    Table ammoValueTable = new Table();
    Styles.INSTANCE
      .addTableColour(ammoValueTable, new Color(0f, 0f, 0f, 0.5f));
    ammoValueTable.add(ammoValue);
    Label ammoLabel = Styles.INSTANCE.createLabel("Ammo");
    Table ammoLabelTable = new Table();
    Styles.INSTANCE
      .addTableColour(ammoLabelTable, Color.GRAY);
    ammoLabelTable.add(ammoLabel);
    ammoTable.add(ammoValueTable);
    ammoTable.add(ammoLabelTable);
    bottomRightTable.add(ammoTable);
    return bottomRightTable;
  }

  private Table createHudPopUp() {
    verticalGroup = new VerticalGroup();
    verticalGroup.space(20);
    popUpTable.add(verticalGroup).padLeft(50).padRight(50).padTop(20).padBottom(20);
    //create resume button
    TextButton resumeButton = Styles.INSTANCE.createButton("Resume");
    resumeButton.addListener(ListenerFactory.newListenerEvent(() -> {
      tractionPopUp = true;
      popUpTable.setVisible(false);
      return false;
    }));
    verticalGroup.addActor(resumeButton);
    //create settings button
    TextButton settingsButton = Styles.INSTANCE.createButton("Settings");
    settingsButton.addListener(ListenerFactory.newListenerEvent(() -> {
      createSettingsChildren();
      return false;
    }));
    verticalGroup.addActor(settingsButton);
    //create quit button go back to the main menu and disconnect form server
    TextButton quitButton = Styles.INSTANCE.createBackButton("Quit");
    quitButton.addListener(ListenerFactory.newChangeScreenEvent(MainMenuScreen.class));
    quitButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Data.INSTANCE.quit();
      refresh();
      return true;
    }));
    verticalGroup.addActor(quitButton);
    Styles.INSTANCE
      .addTableColour(popUpTable, new Color(0f, 0f, 0f, 0.5f));
    return popUpTable;
  }

  private void createSettingsChildren() {
    verticalGroup.clear();
    Settings.createSettings();
    //create back button
    TextButton backButton = Styles.INSTANCE.createButton("Back");
    backButton.addListener(ListenerFactory.newListenerEvent(() -> {
      popUpTable.reset();
      createHudPopUp();
      return false;
    }));
    verticalGroup.addActor(backButton);
  }

  private Table createHudAlertTable() {
    alertTable = new Table();
    alertTable.bottom();
    alertTable.defaults().padBottom(60);
    Label alertLabel = Styles.INSTANCE.createLabel("TRACTION DISABLED");
    alertTable.add(alertLabel);
    alertTable.setVisible(false);
    return alertTable;
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    backgroundInput();
    this.fpsValue.setText(Gdx.graphics.getFramesPerSecond());
    Update newUpdate = Data.INSTANCE.nextUpdate();
    if (newUpdate != null) {
      update = newUpdate;
      player = update.getMe(Data.INSTANCE.getID());
    }
    if (player != null) {
      this.camera.setPosititon(maxX - player.getX(), maxY - player.getY());
      this.tankXY.setText(Math.round(maxX - player.getX()) + ", " + Math.round(maxY - player.getY()));
      if (player.getR() == 0) {
        this.direction.setText("UP");
      } else if (player.getR() == 90) {
        this.direction.setText("RIGHT");
      } else if (player.getR() == 180) {
        this.direction.setText("DOWN");
      } else if (player.getR() == 270) {
        this.direction.setText("LEFT");
      }
    }
    //main viewport
    camera.getViewport().apply();
    renderer.setView(this.camera.getCamera());
    renderer.render();
    //health bar
    healthBar();
    //tanks
    tanks.setProjectionMatrix(this.camera.getCamera().combined);
    tanks.setColor(Data.INSTANCE.getTankColour());
    tanks.begin();
    renderTanks(tanks);
    //mini viewport
    minimapViewport.apply();
    minimap();
    tanksMiniMap.begin();
    tanksMiniMap.setProjectionMatrix(minimapViewport.getCamera().combined);
    tanksMiniMap.setColor(Color.CYAN);
    if (newUpdate != null) {
      renderContainer(update.getMe(Data.INSTANCE.getID()), tanksMiniMap);
    }
    tanksMiniMap.end();
    if (update != null && update.getMe(Data.INSTANCE.getID()) != null) {
      hudUpdate.update(update);
    }
    //hud viewport
    act(delta);
    getViewport().apply();
    draw();
  }

  private void renderTanks(SpriteBatch tanks) {
    if (update != null) {
      for (int i = 0; i < update.getPlayers().values().size(); i++) {
        Container updater = update.getI(i);
        renderContainer(updater, tanks);
      }
    }
    tanks.end();
  }

  private void minimap() {
    minimapBackGround.begin();
    minimapBackGround.setColor(Color.CORAL);
    minimapBackGround.draw(Styles.INSTANCE.getBlank(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    minimapBackGround.setColor(Color.DARK_GRAY);
    minimapBackGround.draw(Styles.INSTANCE.getBlank(), 10, 10, Gdx.graphics.getWidth() - 20, Gdx.graphics.getHeight() - 20);
    minimapBackGround.end();
  }

  private void healthBar() {
    healthBarBatch.begin();
    health = hudUpdate.getHealth();
    healthBarBatch.setColor(new Color(0f, 0f, 0f, 0.25f));
    healthBarBatch.draw(Styles.INSTANCE.getBlank(), Gdx.graphics.getWidth() / 2f - (0.5f * (Gdx.graphics.getWidth() / 4f)), 20, Gdx.graphics.getWidth() / 4f, 15);
    if (health > 0.6f) {
      healthBarBatch.setColor(Color.GREEN);
    } else if (health <= 0.6f && health > 0.2f) {
      healthBarBatch.setColor(Color.ORANGE);
    } else {
      healthBarBatch.setColor(Color.RED);
    }
    healthBarBatch.draw(Styles.INSTANCE.getBlank(), Gdx.graphics.getWidth() / 2f - (0.5f * (Gdx.graphics.getWidth() / 4f)), 20, Gdx.graphics.getWidth() / 4f * health, 15);
    if (health <= 0.1f) {
      tractionHealth = false;
      alertTable.setVisible(true);
    } else {
      tractionHealth = true;
      alertTable.setVisible(false);
    }
    healthBarBatch.setColor(Color.WHITE);
    healthBarBatch.end();
  }

  private void renderContainer(Container c, SpriteBatch batch) {
    if (c.getId().equals("")) {
      this.logger.trace(c.getId() + ": " + c.getX() + ", " + c.getY());
    }
    batch.draw(tankTexture, maxX - c.getX(), maxY - c.getY());
  }

  private int getBearing() {
    Vector3 mouseMapPos3 = camera.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    Vector2 mouseMapPos = new Vector2(mouseMapPos3.x, mouseMapPos3.y);
    Vector2 tankVec = new Vector2(player.getX(), player.getY());
    Vector2 mouseRel = new Vector2(mouseMapPos.x - maxX + tankVec.x, mouseMapPos.y - maxY + tankVec.y);
    return Math.round(mouseRel.angle());
  }


  private void backgroundInput() {
    if (tractionPopUp) {
      if (tractionHealth) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
          PopUp.createPopUp(false);
          //show the pop up table
          popUpTable.setVisible(true);
          tractionPopUp = false;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
          Data.INSTANCE.sendCommand(Command.LEFT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
          Data.INSTANCE.sendCommand(Command.RIGHT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
          Data.INSTANCE.sendCommand(Command.UP);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
          Data.INSTANCE.sendCommand(Command.DOWN);
        }
      }
      if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
        Data.INSTANCE.sendCommand(Command.SHOOT, getBearing());
      }
    } else {
      if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
        //close the pop up
        popUpTable.setVisible(false);
        tractionPopUp = true;
      }
    }
  }

  @Override
  public void dispose() {
    super.dispose();
    map.dispose();
    renderer.dispose();
    tanks.dispose();
    tanksMiniMap.dispose();
    healthBarBatch.dispose();
  }

  @Override
  public void refresh() {
    ammoValue = Styles.INSTANCE.createLabel("");
    killCount = Styles.INSTANCE.createLabel("");
    playerCount = Styles.INSTANCE.createLabel("");
    tankXY = Styles.INSTANCE.createLabel("");
    direction = Styles.INSTANCE.createLabel("");
    popUpTable.setVisible(false);
    alertTable.setVisible(false);
    tractionHealth = true;
  }
}
