package com.aticatac.client.screens;

import com.aticatac.client.networking.Servers;
import com.aticatac.client.util.AudioEnum;
import com.aticatac.client.util.Camera;
import com.aticatac.client.util.Data;
import com.aticatac.client.util.HudUpdate;
import com.aticatac.client.util.MinimapViewport;
import com.aticatac.client.util.Styles;
import com.aticatac.common.model.Command;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.common.objectsystem.containers.Container;
import com.aticatac.common.objectsystem.containers.PlayerContainer;
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
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import java.util.concurrent.ConcurrentHashMap;

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
  /**
   * The Pop up table.
   */
  Table popUpTable;
  /**
   * The Vertical group.
   */
  VerticalGroup verticalGroup;
  /**
   * The Traction pop up.
   */
  boolean tractionPopUp;
  private Update update;
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
  private Texture projectileTexture;
  private Texture stick;
  private Label direction;
  private PlayerContainer player;
  private HudUpdate hudUpdate;
  private boolean tractionHealth;
  private boolean endGame;

  /**
   * Instantiates a new Game screen.
   */
  public GameScreen() {
    super();
    maxX = 1920;
    maxY = 1920;
    try {
      player = new PlayerContainer();
      ammoValue = Styles.INSTANCE.createLabel("");
      killCount = Styles.INSTANCE.createLabel("0");
      playerCount = Styles.INSTANCE.createLabel("0");
      fpsValue = Styles.INSTANCE.createLabel("");
      tankXY = Styles.INSTANCE.createLabel("");
      direction = Styles.INSTANCE.createLabel("");
      map = new TmxMapLoader().load("maps/mapData/map.tmx");
      tankTexture = new Texture("img/tank.png");
      projectileTexture = new Texture("img/bullet.png");
      stick = new Texture("img/top.png");
      tractionHealth = true;
      tractionPopUp = true;
      renderer = new OrthogonalTiledMapRenderer(map);
      minimapViewport = new MinimapViewport(0.2f, 0.025f, new OrthographicCamera());
      minimapViewport.setWorldSize(maxX, maxY);
      this.camera = new Camera(maxX, maxY, 640, 640, false);
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
  public void buildStage() {
    Servers.INSTANCE.clearServers();
    //create root table
    rootTable = new Table();
    rootTable.setFillParent(true);
    addActor(rootTable);
    Gdx.graphics.setVSync(true);
    //create table and labels for player count and kills - TOP LEFT
    super.addToRoot(HUD.createHudTopLeft(playerCount, killCount));
    //create table for kill feed - BOTTOM LEFT
    killLogTable = HUD.createHudBottomLeft();
    super.addToRoot(killLogTable);
    //create table for ammo - BOTTOM RIGHT
    super.addToRoot(HUD.createHudBottomRight(ammoValue));
    //create alert table - BOTTOM MIDDLE
    alertTable = HUD.createHudAlertTable();
    super.addToRoot(alertTable);
    hudUpdate = new HudUpdate(killLogTable, ammoValue, playerCount, killCount);
  }

  @Override
  public void refresh() {
    ammoValue = Styles.INSTANCE.createLabel("0");
    killCount = Styles.INSTANCE.createLabel("0");
    playerCount = Styles.INSTANCE.createLabel("0");
    tankXY = Styles.INSTANCE.createLabel("");
    direction = Styles.INSTANCE.createLabel("");
    popUpTable.setVisible(false);
    alertTable.setVisible(false);
    tractionHealth = true;
  }

  @Override
  public void render(float delta) {
    AudioEnum.INSTANCE.getTheme();
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    backgroundInput();
    this.fpsValue.setText(Gdx.graphics.getFramesPerSecond());
    Update newUpdate = Data.INSTANCE.nextUpdate();
    if (newUpdate != null) {
      if (player.isAlive()) {
        update = newUpdate;
        player = update.getMe(Data.INSTANCE.getID());
        int playerAlive = checkAlive(update.getPlayers());
        if (playerAlive != Integer.valueOf(playerCount.getText().toString())) {
          playerCount.setText(Integer.toString(playerAlive));
        }
        if(!Data.INSTANCE.isConnectedToGame()){
          if (!endGame) {
            PopUp.createPopUp(false, true, false);
            popUpTable.setVisible(true);
            endGame = true;
          }
        }
        playerCount.setText(playerAlive);
        if (playerAlive == 1 && player.isAlive()) {
          Data.INSTANCE.setWon(true);
          if (!endGame) {
            PopUp.createPopUp(false, true, false);
            popUpTable.setVisible(true);
            endGame = true;
          }
        }
        player = newUpdate.getMe(Data.INSTANCE.getID());
      } else {
        Data.INSTANCE.setWon(false);
        if (!endGame) {
          PopUp.createPopUp(false, true, false);
          popUpTable.setVisible(true);
          endGame = true;
        }
        PlayerContainer temp = newUpdate.getMe(Data.INSTANCE.getID());
        player.setR(temp.getR());
        update = newUpdate;
      }
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
    if (update != null) {
      for (Container c : update.getPlayers().values()) {
        louderSound(c);
      }
      Container corner = new Container(0, 0, 0, "", EntityType.NONE);
      louderSound(corner);
      Container corner2 = new Container(0, 1920, 0, "", EntityType.NONE);
      louderSound(corner2);
      Container corner3 = new Container(1920, 0, 0, "", EntityType.NONE);
      louderSound(corner3);
      Container corner4 = new Container(1920, 1920, 0, "", EntityType.NONE);
      louderSound(corner4);
      if (update.getMe(Data.INSTANCE.getID()) != null) {
        renderContainer(update.getMe(Data.INSTANCE.getID()), tanksMiniMap);
      }
      tanksMiniMap.setColor(Color.RED);
      for (Container c :
          update.getNewShots().values()) {
        renderContainer(c, tanksMiniMap);
        //Audio: plays sound when tank shoots in range
        if (camera.getCamera().frustum.pointInFrustum(c.getX(), c.getY(), 0)) {
          float dx = c.getX() - (1920 - player.getX());
          float dy = c.getY() - (1920 - player.getY());
          int max = 320;
          double distance = Math.sqrt(dx * dx + dy * dy);
          double volumeScalar = (max - distance) / max;
          AudioEnum.INSTANCE.getOtherTankShoot((float) volumeScalar);
        }
      }
    }
    tanksMiniMap.end();
    if (update != null && update.getMe(Data.INSTANCE.getID()) != null) {
      hudUpdate.update(update, player);
    }
    //hud viewport
    act(delta);
    getViewport().apply();
    draw();
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);
    this.camera.getViewport().update(width, height);
    this.minimapViewport.update(width, height, true);
  }

  private void louderSound(Container c) {
    if (camera.getCamera().frustum.pointInFrustum(c.getX(), c.getY(), 0)) {
      float dx = c.getX() - (1920 - player.getX());
      float dy = c.getY() - (1920 - player.getY());
      double distance = Math.sqrt(dx * dx + dy * dy);
      if (distance < 160) {
        int max = 160;
        double volumeScalar = (max - distance) / max;
        float musicVolume = AudioEnum.INSTANCE.getMusicVolume();
        float musicScaled = (float) (musicVolume + volumeScalar);
        if (musicScaled <= 1.0f) {
          AudioEnum.INSTANCE.getTheme().setVolume(musicScaled);
        } else {
          AudioEnum.INSTANCE.getTheme().setVolume(1.0f);
        }
        this.logger.info(musicVolume + volumeScalar);
      }
    }
  }

  private void renderTanks(SpriteBatch tanks) {
    if (update != null) {
      for (PlayerContainer c : update.getPlayers().values()) {
        if (c.isAlive()) {
          renderContainer(c, tanks);
          tanks.draw(stick, maxX - c.getX(), maxY - c.getY(), stick.getWidth() / 2f, 0, stick.getWidth(), stick.getHeight(), 1, 0.7f, c.getR() - 90f, 0, 0, stick.getWidth(), stick.getHeight(), false, false);
        }
      }
    }
    renderProjectiles(tanks);
    renderPowerups(tanks);
    tanks.end();
  }

  private void renderProjectiles(SpriteBatch tanks) {
    if (update != null) {
      for (Container c :
          update.getProjectiles().values()) {
        renderProjectiles(c, tanks);
      }
    }
  }

  private void renderPowerups(SpriteBatch tanks) {
    if (update != null) {
      for (Container c :
          update.getPowerups().values()) {
        switch (c.getObjectType()) {
          case NONE:
            break;
          case TANK:
            break;
          case BULLET:
            break;
          case WALL:
            break;
          case OUTOFBOUNDS:
            break;
          case AMMO_POWERUP:
            tanks.setColor(Color.CYAN);
            break;
          case SPEED_POWERUP:
            tanks.setColor(Color.RED);
            break;
          case HEALTH_POWERUP:
            tanks.setColor(Color.BLUE);
            break;
          case DAMAGE_POWERUP:
            tanks.setColor(Color.YELLOW);
            break;
          case BULLETSPRAY_POWERUP:
            tanks.setColor(Color.PURPLE);
            break;
          case FREEZEBULLET_POWERUP:
            tanks.setColor(Color.FOREST);
            break;
        }
        //tanks.setColor(Color.WHITE);
        renderContainer(c, tanks);
      }
    }
  }

  private void renderProjectiles(Container c, SpriteBatch batch) {
    batch.draw(projectileTexture, maxX - c.getX(), maxY - c.getY());
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
    batch.draw(tankTexture, maxX - c.getX() - tankTexture.getWidth() / 2f, maxY - c.getY() - tankTexture.getHeight() / 2f);
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
      if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
        PopUp.createPopUp(false, false, false);
        //show the pop up table
        popUpTable.setVisible(true);
        tractionPopUp = false;
      }
      if (tractionHealth) {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
          if (Screens.INSTANCE.getCurrentScreen().equals(GameScreen.class)) {
            AudioEnum.INSTANCE.getTankMove();
          }
          Data.INSTANCE.sendCommand(Command.LEFT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
          if (Screens.INSTANCE.getCurrentScreen().equals(GameScreen.class)) {
            AudioEnum.INSTANCE.getTankMove();
          }
          Data.INSTANCE.sendCommand(Command.RIGHT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
          if (Screens.INSTANCE.getCurrentScreen().equals(GameScreen.class)) {
            AudioEnum.INSTANCE.getTankMove();
          }
          Data.INSTANCE.sendCommand(Command.UP);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
          if (Screens.INSTANCE.getCurrentScreen().equals(GameScreen.class)) {
            AudioEnum.INSTANCE.getTankMove();
          }
          Data.INSTANCE.sendCommand(Command.DOWN);
        }
      }
      if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonPressed(0)) {
        AudioEnum.INSTANCE.getShoot();
        Data.INSTANCE.sendCommand(Command.SHOOT);
      }
      if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
        AudioEnum.INSTANCE.getShoot();
        Data.INSTANCE.sendCommand(Command.BULLET_SPRAY);
      }
      if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
        AudioEnum.INSTANCE.getShoot();
        Data.INSTANCE.sendCommand(Command.FREEZE_BULLET);
      }
    }
    Data.INSTANCE.submit(getBearing());
  }

  private int checkAlive(ConcurrentHashMap<String, PlayerContainer> players) {
    //go through all players count how many are alive
    int count = 0;
    for (String key :
        players.keySet()) {
      if (players.get(key).isAlive()) {
        count++;
      }
    }
    return count;
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
}
