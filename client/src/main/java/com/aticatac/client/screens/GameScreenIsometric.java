package com.aticatac.client.screens;

import com.aticatac.client.isometric.DynamicShadow;
import com.aticatac.client.isometric.Helper;
import com.aticatac.client.isometric.IsoContainer;
import com.aticatac.client.isometric.TileHolder;
import com.aticatac.client.networking.Servers;
import com.aticatac.client.server.Position;
import com.aticatac.client.util.*;
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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Game screen.
 */
public class GameScreenIsometric extends AbstractScreen {
  private final SpriteBatch healthBarBatch;
  private final SpriteBatch game;
  private final SpriteBatch tanksMiniMap;
  private final SpriteBatch minimapBackGround;
  private final int maxX;
  private final int maxY;
  private Update update;
  /**
   * The Pop up table.
   */
  Table popUpTable;
  /**
   * The Vertical group.
   */
  VerticalGroup verticalGroup;
  private Table alertTable;
  private Table killLogTable;
  private Label ammoValue;
  private Label killCount;
  private Label playerCount;
  private float health;
  private TiledMap map;
  private TiledMapTileSet tileSet;
  private IsometricTiledMapRenderer renderer;
  private Camera camera;
  private MinimapViewport minimapViewport;
  private Label fpsValue;
  private Label tankXY;
  private Texture tankTexture;
  private Texture powerUpTexture;
  private Texture bulletTexture;
  private Texture projectileTexture;
  private Texture shadow;
  private Texture shadowUltraLight;
  private ArrayList<Texture> rotations;
  private Texture stick;
  private Label direction;
  private PlayerContainer player;
  private HudUpdate hudUpdate;
  private boolean tractionHealth;
  /**
   * The Traction pop up.
   */
  boolean tractionPopUp;
  private boolean endGame;

  private ArrayList<ArrayList<TileHolder>> rows = new ArrayList<>();
  private HashMap<Integer, TextureRegion> tileTextures = new HashMap<>();

  /**
   * The Lighting.
   */
  HashMap<Position, DynamicShadow> lighting;

  /**
   * Instantiates a new Game screen.
   */
  GameScreenIsometric() {
    super();
    maxX = 1920;
    maxY = 1920;
    try {
      player = new PlayerContainer();
      ammoValue = Styles.INSTANCE.createLabel("0");
      killCount = Styles.INSTANCE.createLabel("0");
      playerCount = Styles.INSTANCE.createLabel("0");
      fpsValue = Styles.INSTANCE.createLabel("0");
      tankXY = Styles.INSTANCE.createLabel("0");
      direction = Styles.INSTANCE.createLabel("0");

      map = new TmxMapLoader().load("maps/mapData/mapIsometric.tmx");
      renderer = new IsometricTiledMapRenderer(map);
      tileSet = map.getTileSets().getTileSet(0);
      tankTexture = new Texture("maps/tank1.png");
      powerUpTexture = new Texture("maps/mapData/powerup.png");
      shadow = new Texture("maps/shadow/shadow.png");
      shadowUltraLight = new Texture("maps/shadow/shadow2.png");
      bulletTexture = new Texture("maps/mapData/bullet.png");
      projectileTexture = new Texture("img/bullet.png");
      tractionHealth = true;
      tractionPopUp = true;
      endGame = false;

      for (TiledMapTile tiledMapTile : tileSet) {
        tileTextures.put(tiledMapTile.getTextureRegion().hashCode(), tiledMapTile.getTextureRegion());
      }

      rotations = new ArrayList<>();
      for (int i = 0; i <= 359; i++) {
        rotations.add(new Texture("maps/tank-with-shadow2/" + String.format("%04d", i) + ".png"));
      }

      for (int i = 0; i != 61; i++) {
        ArrayList<TileHolder> ps = new ArrayList<>();
        rows.add(ps);
      }

      int counter = 1;

      for (int j = 0; j <= 61; j++) {
        for (int i = -counter; i < counter + 1; i++) {
          int x = (int) (j + 0.5f - i);
          int y = (int) (j - 0.5f + i);
          try {
            var tile = ((TiledMapTileLayer) map.getLayers().get(1)).getCell(60 - y, x).getTile();//new Position(60-p.getY()-1,p.getX()+1);
            if (tile != null) {
              rows.get(j).add(new TileHolder(new Position(x, y), tile.getTextureRegion().hashCode()));
            }
          } catch (Exception ignored) {
          }

        }

        for (int i = -counter; i < counter + 1; i++) {
          int x = j - i;
          int y = j + i;
          try {
            var tile = ((TiledMapTileLayer) map.getLayers().get(1)).getCell(60 - y, x).getTile();
            if (tile != null) {
              rows.get(j).add(new TileHolder(new Position(x, y), tile.getTextureRegion().hashCode()));
            }
          } catch (Exception ignored) {
          }

        }

        counter++;
      }

      minimapViewport = new MinimapViewport(0.2f, 0.025f, new OrthographicCamera());
      minimapViewport.setWorldSize(maxX, maxY);
      this.camera = new Camera(maxX, maxY, 640, 640, true);
      Gdx.input.setInputProcessor(this);

      setupLighting();

    } catch (Exception e) {
      e.printStackTrace();

    }
    healthBarBatch = new SpriteBatch();
    game = new SpriteBatch();
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
  public void render(float delta) {
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
      this.camera.setPosititon(Helper.tileToScreenX(player.getX() + 32, player.getY() - 32), Helper.tileToScreenY(player.getX() + 32, player.getY() - 32));
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
    renderer.getBatch().begin();
    renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(0));
    //renderer.getBatch().setColor(0.3f, 0.3f, 0.3f, 1);
    renderer.getBatch().end();

    //game
    game.setProjectionMatrix(this.camera.getCamera().combined);
    game.begin();

    //game.setColor(0.3f, 0.3f, 0.3f, 1);

    var collection = new ArrayList<IsoContainer>();
    collection.addAll(returnTanks());
    collection.addAll(returnProjectiles());
    collection.addAll(returnPowerups());

    renderGame(game, collection);

    //vec4 color = texture2D(TextureSampler, TexCoord) * Color.GRAY;

    game.end();

    renderer.getBatch().begin();
    renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(2));
    renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(3));
    renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(4));
    renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(5));
    renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(6));
    //renderer.getBatch().setColor(0.3f, 0.3f, 0.3f, 1);
    renderer.getBatch().end();

    //health bar
    healthBar();

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
      hudUpdate.update(update, player);
    }
    //hud viewport
    super.act(delta);
    getViewport().apply();
    super.draw();
  }

  private void renderTanks(SpriteBatch tanks) {
    if (update != null) {
      for (Container c : update.getPlayers().values()) {
        renderContainer(c, tanks);
        tanks.draw(stick, maxX - c.getX(), maxY - c.getY(), stick.getWidth() / 2f, 0, stick.getWidth(), stick.getHeight(), 1, 0.7f, c.getR() - 90f, 0, 0, stick.getWidth(), stick.getHeight(), false, false);
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
        tanks.setColor(Color.WHITE);
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
    if (c.getId().equals("")) {
      this.logger.trace(c.getId() + ": " + c.getX() + ", " + c.getY());
    }
    batch.draw(tankTexture, maxX - c.getX() - tankTexture.getWidth() / 2f, maxY - c.getY() - tankTexture.getHeight() / 2f);
  }

  private int getBearing() {
    try {
      Vector3 mouseMapPos3 = camera.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

      Position mouseMapIso =
        Helper.screenToTile(new Position(
          (int) (mouseMapPos3.x),
          (int) (mouseMapPos3.y))
        );

      float deltaX = mouseMapIso.getX() - player.getX() + tankTexture.getWidth() / 2f;
      float deltaY = mouseMapIso.getY() - player.getY() + tankTexture.getHeight() / 2f;

      return (int) (Math.round(Math.toDegrees(Math.atan2(deltaY, deltaX))));
    } catch (Exception ignored) {
      return 0;
    }
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
          if (Screens.INSTANCE.getCurrentScreen().equals(GameScreenIsometric.class)) {
            AudioEnum.INSTANCE.getTankMove();
          }
          Data.INSTANCE.sendCommand(Command.LEFT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
          if (Screens.INSTANCE.getCurrentScreen().equals(GameScreenIsometric.class)) {
            AudioEnum.INSTANCE.getTankMove();
          }
          Data.INSTANCE.sendCommand(Command.RIGHT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
          if (Screens.INSTANCE.getCurrentScreen().equals(GameScreenIsometric.class)) {
            AudioEnum.INSTANCE.getTankMove();
          }
          Data.INSTANCE.sendCommand(Command.DOWN);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
          if (Screens.INSTANCE.getCurrentScreen().equals(GameScreenIsometric.class)) {
            AudioEnum.INSTANCE.getTankMove();
          }
          Data.INSTANCE.sendCommand(Command.UP);
        } else if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
          this.camera.getCamera().zoom -= 0.1f;
        } else if (Gdx.input.isKeyPressed(Input.Keys.E)) {
          this.camera.getCamera().zoom += 0.1f;
        }
      }
      if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
        AudioEnum.INSTANCE.getShoot();
        Data.INSTANCE.sendCommand(Command.SHOOT);
      }
      if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
        AudioEnum.INSTANCE.getShoot();
        Data.INSTANCE.sendCommand(Command.BULLET_SPRAY);
      }
      if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isButtonPressed(0)) {
        AudioEnum.INSTANCE.getShoot();
        Data.INSTANCE.sendCommand(Command.FREEZE_BULLET);
      }
    }
    Data.INSTANCE.submit(changeAngle(getBearing(), 180));
  }

  @Override
  public void dispose() {
    super.dispose();
    map.dispose();
    renderer.dispose();
    game.dispose();
    tanksMiniMap.dispose();
    healthBarBatch.dispose();
  }

  @Override
  public void refresh() {
    ammoValue = Styles.INSTANCE.createLabel("0");
    killCount = Styles.INSTANCE.createLabel("0");
    playerCount = Styles.INSTANCE.createLabel("0");
    tankXY = Styles.INSTANCE.createLabel("0");
    direction = Styles.INSTANCE.createLabel("0");
    popUpTable.setVisible(false);
    alertTable.setVisible(false);
    tractionHealth = true;
  }

  /*Isometric rendering begin*/

  /**
   * Render game.
   *
   * @param sb      the sb
   * @param objects the objects
   */
  public void renderGame(SpriteBatch sb, ArrayList<IsoContainer> objects) {

    int index = 0;
    var usedShadows = new ArrayList<Position>();
    int X1;
    int X2 = 0;

    for (ArrayList<TileHolder> ps : rows) {

      for (TileHolder tileHolder : ps) {
        var p = tileHolder.getPosition();
        var pp = p;
        var tile = tileTextures.get(tileHolder.getTexture());
        //(60-y-1) * 32, (x+1) * 32
        var objPp = new Position((pp.getX()) * 32, (pp.getY()) * 32);

        var temp = screenToIsoTiles(objPp);

        X1 = X2;
        X2 = temp.getX();

        objects.sort(Comparator.comparing(Container::getY));
        objects.sort(Comparator.comparing(Container::getX));

        for (var c : objects) {
          int objX = Helper.tileToScreenX(c.getX(), c.getY());
          if (objX >= X1 && objX <= X2) {
            if ((int) ((Math.ceil((c.getX()) / 32f) + Math.ceil(60 - ((c.getY()) / 32f))) / 2f) == index) {
              if (c.getObjectType() == EntityType.BULLET) {
                sb.draw(bulletTexture, Helper.tileToScreenX(c.getX() + 10, c.getY() - 10), Helper.tileToScreenY(c.getX() + 10, c.getY() - 10));
              } else if (c.getObjectType() == EntityType.AMMO_POWERUP || c.getObjectType() == EntityType.HEALTH_POWERUP || c.getObjectType() == EntityType.DAMAGE_POWERUP || c.getObjectType() == EntityType.SPEED_POWERUP) {
                sb.draw(powerUpTexture, Helper.tileToScreenX(c.getX() + 10, c.getY() - 10), Helper.tileToScreenY(c.getX() + 10, c.getY() - 10));
                sb.draw(shadow, Helper.tileToScreenX(c.getX() + 10, c.getY() - 10), Helper.tileToScreenY(c.getX() + 10, c.getY() - 10));
                sb.draw(shadowUltraLight, Helper.tileToScreenX(c.getX() + 10, c.getY() - 32 - 10), Helper.tileToScreenY(c.getX() + 10, c.getY() - 32 - 10));
              } else if (c.getObjectType() == EntityType.TANK) {
                //sb.draw(tankTexture, Helper.tileToScreenX(c.getX() + 10, c.getY() - 10), Helper.tileToScreenY(c.getX() + 10, c.getY() - 10));
                int t = update.getMe(Data.INSTANCE.getID()).getId().equals(c.getId()) ? changeAngle(-getBearing(), 90) : changeAngle(-c.getR(), 270);//changeAngle(-c.getR(),-90);
                t = t % 360;
                if (t == 359) t = 0;
                //sb.draw(rotations.get(t),Helper.tileToScreenX(c.getX() + 1, c.getY() - 1), Helper.tileToScreenY(c.getX() + 1, c.getY() - 1));
                var img = rotations.get(t);
                sb.draw(img,
                  Helper.tileToScreenX((float) c.getX() + 32f, (float) c.getY() - 32f) - 16f,
                  Helper.tileToScreenY((float) c.getX() + 32f, (float) c.getY() - 32f) - 16f,
                  0,
                  0,
                  img.getWidth(),
                  img.getHeight(),
                  1,
                  1,
                  0,
                  0,
                  0,
                  img.getWidth(),
                  img.getHeight(),
                  false,
                  false);
                for (int i = 0; i < c.getDegrees().size(); i++) {
                  //this.logger.info(c.getDegrees().get(i));
                }
              }
            }
          }
        }


        drawTiles(sb, objPp.getX(), objPp.getY(), tile);
        var shadowPp = new Position((pp.getX() + 1) * 32, (pp.getY()) * 32);
        if (!usedShadows.contains(pp)) {
          drawShadow(sb, shadowPp.getX(), shadowPp.getY(), shadow);
          usedShadows.add(pp);
        }
      }


      index++;

    }
  }

  private ArrayList<IsoContainer> returnProjectiles() {
    var array = new ArrayList<IsoContainer>();
    if (update != null) {
      for (int i = 0; i < update.getProjectiles().values().size(); i++) {
        Container updater = update.getProjectiles().get(update.getProjectiles().keySet().toArray()[i]);
        array.add(new IsoContainer(updater));
      }
    }
    return array;
  }

  private ArrayList<IsoContainer> returnTanks() {
    var array = new ArrayList<IsoContainer>();
    if (update != null) {

      for (int i = 0; i < update.getPlayers().values().size(); i++) {
        Container updater = update.getI(i);
        array.add(new IsoContainer(updater));
      }
    }
    return array;
  }

  private ArrayList<IsoContainer> returnPowerups() {
    var array = new ArrayList<IsoContainer>();
    if (update != null) {
      for (int i = 0; i < update.getPowerups().values().size(); i++) {
        Container updater = update.getPowerups().get(update.getPowerups().keySet().toArray()[i]);
        array.add(new IsoContainer(updater));
      }
    }
    return array;
  }

  private void drawTiles(SpriteBatch sb, int i, int j, TextureRegion tile) {

    var pos4 = screenToIsoTiles(new Position(i, j));

    sb.draw(tile, pos4.getX(), pos4.getY());
  }

  private void drawShadow(SpriteBatch sb, int i, int j, Texture shadow) {
    var pos4 = screenToIsoTiles(new Position(i, j));

    sb.draw(shadow, pos4.getX(), pos4.getY());
  }

  private int changeAngle(int angle, int change) {
    if (angle + change >= 0)
      return (angle + change) % 360;
    return (angle + change + 360) % 360;
  }

  private static Position screenToIsoTiles(Position position) {
    var pos1 = new Position(position.getX() - 960, position.getY() - 960);
    var pos2 = new Position(pos1.getY(), -pos1.getX());
    var pos3 = new Position(pos2.getX() + 960, pos2.getY() + 960);

    return Helper.tileToScreen(pos3);
  }

  private void setupLighting() {
    lighting = new HashMap<>();
  }

  private ArrayList<IsoContainer> CalculateLighting(ArrayList<IsoContainer> containers) {

    lighting = new HashMap<>();

    for (var updater : containers) {
      for (int x = -16; x <= 16; x++) {
        for (int y = -16; y <= 16; y++) {
          lighting.put(new Position(updater.getX() + x, updater.getY() + y), new DynamicShadow(updater));
        }
      }
    }

    for (var updater : containers) {
      for (int w = 0; w < 360; w++) {
        for (int z = 0; z != 1920 * 1.5; z++) {
          if (lighting.get(new Position(
            (int) (z * Math.sin(Math.toRadians(w))) + updater.getX(),
            (int) (z * Math.cos(Math.toRadians(w))) + updater.getY()
          )) != null) {

            if (!lighting.get(new Position(
              (int) (z * Math.sin(Math.toRadians(w))) + updater.getX(),
              (int) (z * Math.cos(Math.toRadians(w))) + updater.getY()
            )).getParent().equals(updater)) {

              //this.logger.info(w);

              lighting.get(new Position(
                (int) (z * Math.sin(Math.toRadians(w))) + updater.getX(),
                (int) (z * Math.cos(Math.toRadians(w))) + updater.getY()
              )).getParent().addLight(w, 1);
            }

            break;
          }
        }
      }
    }
    return containers;
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

}
