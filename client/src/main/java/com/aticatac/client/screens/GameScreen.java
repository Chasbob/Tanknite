package com.aticatac.client.screens;

import com.aticatac.client.util.Camera;
import com.aticatac.client.util.Data;
import com.aticatac.client.util.Styles;
import com.aticatac.common.model.Command;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.objectsystem.Container;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.common.objectsystem.ObjectType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * The type Game screen.
 */
public class GameScreen extends AbstractScreen {
  private final SpriteBatch batch;
  private final SpriteBatch tanks;
  private final int maxX;
  private final int maxY;
  /**
   * The Update.
   */
  private Update update;
  private Table popUpTable;
  private float health;
  private Label ammoValue;
  private Label killCount;
  private Label playerCount;
  private TiledMap map;
  private OrthogonalTiledMapRenderer renderer;
  private Camera camera;
  private Label fpsValue;
  private Label tankXY;
  private Texture tankTexture;
  private Label direction;
  private Container player;

  /**
   * Instantiates a new Game screen.
   */
  GameScreen() {
    super();
    maxX = 1920;
    maxY = 1920;
    try {
      health = 1f;
      player = new Container(new GameObject("__", ObjectType.OTHER));
      ammoValue = UIFactory.createLabel("30");
      killCount = UIFactory.createLabel("0");
      playerCount = UIFactory.createLabel("1");
      fpsValue = UIFactory.createLabel("");
      tankXY = UIFactory.createLabel("");
      direction = UIFactory.createLabel("");
      map = new TmxMapLoader().load("maps/map.tmx");
      tankTexture = new Texture("img/tank.png");
      renderer = new OrthogonalTiledMapRenderer(map);
      this.camera = new Camera(maxX, maxY, 640, 640);
      Gdx.input.setInputProcessor(this);
    } catch (Exception e) {
      e.printStackTrace();
    }
    batch = new SpriteBatch();
    tanks = new SpriteBatch();
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);
    this.camera.getViewport().update(width, height);
  }

  @Override
  public void buildStage() {
    Gdx.graphics.setVSync(true);
    //create root table
    Table rootTable = new Table();
    rootTable.setFillParent(true);
    addActor(rootTable);
    //create table and labels for player count and kills - TOP LEFT
    Table countTable = new Table();
    countTable.setFillParent(true);
    rootTable.addActor(countTable);
    countTable.top().left();
    countTable.defaults().padTop(10).padLeft(10).left();
    Label players = UIFactory.createLabel("player count: ");
    countTable.add(players);
    countTable.add(playerCount);
    countTable.row();
    Label kills = UIFactory.createLabel("kills: ");
    countTable.add(kills);
    countTable.add(killCount);
    //create table for kill feed - BOTTOM LEFT
    Table killTable = new Table();
    killTable.setFillParent(true);
    rootTable.addActor(killTable);
    killTable.bottom().left();
    killTable.defaults().padTop(10).padLeft(10).padBottom(20).left();
    Label tempKill = UIFactory.createGameLabel("Archie killed Ewan");
    killTable.add(tempKill);
    //create table for ammo - BOTTOM RIGHT
    Table ammoTable = new Table();
    ammoTable.setFillParent(true);
    rootTable.addActor(ammoTable);
    ammoTable.bottom().right();
    ammoTable.defaults().padRight(10).padTop(10).padBottom(20).left();
    Label ammo = UIFactory.createLabel("ammo: ");
    ammoTable.add(ammo);
    ammoTable.add(ammoValue);
    Table statsTable = new Table();
    statsTable.setFillParent(true);
    rootTable.addActor(statsTable);
    statsTable.top().right();
    statsTable.defaults().padRight(10).padTop(10).padBottom(20).left();
    Label fps = UIFactory.createLabel("FPS: ");
    Label tank = UIFactory.createLabel("TANK: ");
    statsTable.add(tank);
    statsTable.add(tankXY);
    statsTable.row();
    statsTable.add(fps);
    statsTable.add(this.fpsValue);
    statsTable.row();
    Label direction = UIFactory.createLabel("DIRECTION:");
    statsTable.add(direction);
    statsTable.add(this.direction);
    //create pop up table
    popUpTable = new Table();
    rootTable.addActor(popUpTable);
    popUpTable.setFillParent(true);
    popUpTable.center();
    popUpTable.setVisible(false);
    popUpTable.defaults().pad(10).width(150);
    //create style for pop up tables
    Pixmap tableColour = new Pixmap(1, 1, Pixmap.Format.RGB565);
    tableColour.setColor(Color.DARK_GRAY);
    tableColour.fill();
    popUpTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(tableColour))));
    //create resume button
    TextButton resumeButton = UIFactory.createButton("resume");
    resumeButton.addListener(UIFactory.newListenerEvent(() -> {
      popUpTable.setVisible(false);
      return false;
    }));
    popUpTable.add(resumeButton);
    popUpTable.row();
    //create quit button go back to the main menu and disconnect form server
    TextButton quitButton = UIFactory.createBackButton("quit");
    //TODO add proper exiting of server
    quitButton.addListener(UIFactory.newChangeScreenEvent(MainMenuScreen.class));
    quitButton.addListener(UIFactory.newListenerEvent(() -> {
      Data.INSTANCE.quit();
      refresh();
      return true;
    }));
    popUpTable.add(quitButton);
    new Thread(() -> {
      while (!Thread.currentThread().isInterrupted()) {
        double nanoTime = System.nanoTime();
        backgroundInput();
        while (System.nanoTime() - nanoTime < 1000000000 / 60) {
          try {
            Thread.sleep(0);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }).start();
  }

  @Override
  public void render(float delta) {
    super.render(delta);
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
    this.camera.update();
    renderer.setView(this.camera.getCamera());
    renderer.render();
    tanks.setProjectionMatrix(this.camera.getCamera().combined);
    tanks.begin();
    tanks.setColor(Color.CORAL);
    if (update != null) {
      for (int i = 0; i < update.getPlayers().values().size(); i++) {
        renderContainer(update.getI(i));
      }
    }
    tanks.end();
    batch.begin();
    //health bar
    healthBar();
    batch.draw(Styles.getInstance().getBlank(), 0, 0, Gdx.graphics.getWidth() * health, 5);
    batch.setColor(Color.WHITE);
    batch.end();
    super.act(delta);
    super.draw();
  }

  private void renderContainer(Container c) {
    if (c.getId().equals("")) {
      this.logger.trace(c.getId() + ": " + c.getX() + ", " + c.getY());
    }
    tanks.draw(tankTexture, maxX - c.getX(), maxY - c.getY());
  }

  private void backgroundInput() {
    if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
      //show the pop up table
      popUpTable.setVisible(true);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
      Data.INSTANCE.sendCommand(Command.LEFT);
    } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
      Data.INSTANCE.sendCommand(Command.RIGHT);
    } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
      Data.INSTANCE.sendCommand(Command.UP);
    } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
      Data.INSTANCE.sendCommand(Command.DOWN);
    }
  }

  private void healthBar() {
    if (health > 0.6f) {
      batch.setColor(Color.GREEN);
    } else if (health < 0.6f && health > 0.2f) {
      batch.setColor(Color.ORANGE);
    } else {
      batch.setColor(Color.RED);
    }
    batch.draw(Styles.getInstance().getBlank(), 0, 0, getWidth() * health, 5);
  }

  @Override
  public void dispose() {
    super.dispose();
    map.dispose();
    renderer.dispose();
    batch.dispose();
  }

  @Override
  public void refresh() {
    health = 1;
    ammoValue = UIFactory.createLabel("30");
    killCount = UIFactory.createLabel("0");
    playerCount = UIFactory.createLabel("1");
    tankXY = UIFactory.createLabel("");
    direction = UIFactory.createLabel("");
    popUpTable.setVisible(false);
  }
}
