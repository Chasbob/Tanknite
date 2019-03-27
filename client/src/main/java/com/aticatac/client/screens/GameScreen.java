package com.aticatac.client.screens;

import com.aticatac.client.isometric.Helper;
import com.aticatac.client.util.*;
import com.aticatac.client.util.Camera;
import com.aticatac.common.model.Command;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.objectsystem.Container;
import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.Position;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import java.util.ArrayList;

/**
 * The type Game screen.
 */
public class GameScreen extends AbstractScreen {
    private final SpriteBatch healthBarBatch;
    private final SpriteBatch game;
    private final SpriteBatch tanksMiniMap;
    private final SpriteBatch minimapBackGround;
    private final int maxX;
    private final int maxY;
    VerticalGroup verticalGroup;
    private Update update;
    private Table popUpTable;
    private Table alertTable;
    private Table killLogTable;
    private Label ammoValue;
    private Label killCount;
    private Label playerCount;
    private float health;
    private TiledMap map;
    private MapLayers mapLayers;
    private TiledMapTileSet tileSet;
    private TiledMapTileLayer wallLayer;
    private IsometricTiledMapRenderer renderer;
    private Camera camera;
    private MinimapViewport minimapViewport;
    private Label fpsValue;
    private Label tankXY;
    private Texture tankTexture;
    private Texture powerUpTexture;
    private Texture bulletTexture;
    private Texture projectileTexture;
    private Texture tankTexture2;
    private Texture shadow;
    private Texture shadowUltraLight;
    private ArrayList<Texture> rotations;
    private Label direction;
    private Container player;
    private HudUpdate hudUpdate;
    private boolean tractionHealth;
    private boolean tractionPopUp;

    private ArrayList<Position> tileObjects;
    private ArrayList<ArrayList<Position>> rows = new ArrayList<>();

    /**
     * Instantiates a new Game screen.
     */
    GameScreen() {
        super();
        maxX = 1920;
        maxY = 1920;
        try {
            tileObjects = new ArrayList<>();
            player = new Container();
            ammoValue = Styles.INSTANCE.createGameLabel("");
            killCount = Styles.INSTANCE.createGameLabel(" 0 ");
            playerCount = Styles.INSTANCE.createGameLabel(" 1 ");
            fpsValue = Styles.INSTANCE.createGameLabel("");
            tankXY = Styles.INSTANCE.createGameLabel("");
            direction = Styles.INSTANCE.createGameLabel("");
            map = new TmxMapLoader().load("maps/map.tmx");
            renderer = new IsometricTiledMapRenderer(map);
            tileSet = map.getTileSets().getTileSet(0);
            mapLayers = map.getLayers();
            wallLayer = (TiledMapTileLayer) mapLayers.get(1);
            tankTexture = new Texture("maps/tank1.png");
            tankTexture2 = new Texture("img/tank.png");
            powerUpTexture = new Texture("maps/powerup.png");
            shadow = new Texture("maps/shadow.png");
            shadowUltraLight = new Texture("maps/shadow2.png");
            bulletTexture = new Texture("maps/bullet.png");
            projectileTexture = new Texture("img/bullet.png");

            rotations = new ArrayList<>();
            for (int i = 0;i<=359;i++){
                rotations.add(new Texture("maps/turret_with_base/"+String.format("%04d", i)+ ".png"));
            }

            tractionHealth = true;
            tractionPopUp = true;
            minimapViewport = new MinimapViewport(0.2f, 0.025f, new OrthographicCamera());
            minimapViewport.setWorldSize(maxX, maxY);
            this.camera = new Camera(maxX, maxY, 640, 640);
            Gdx.input.setInputProcessor(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        healthBarBatch = new SpriteBatch();
        game = new SpriteBatch();
        tanksMiniMap = new SpriteBatch();
        minimapBackGround = new SpriteBatch();

        for (int i = 0; i != 61; i++) {
            ArrayList<Position> ps = new ArrayList<>();
            rows.add(ps);
        }

        int counter = 1;

        for (int j = 0; j <= 61; j++) {
            for (int i = -counter; i < counter + 1; i++) {
                int x = (int) (j + 0.5f - i);
                int y = (int) (j - 0.5f + i);
                try {
                    rows.get(j).add(new Position(x, y));
                } catch (Exception ignored) {
                }

            }

            for (int i = -counter; i < counter + 1; i++) {
                int x = j - i;
                int y = j + i;
                try {
                    rows.get(j).add(new Position(x, y));
                } catch (Exception ignored) {
                }

            }

            counter++;
        }

    }

    @Override
    public void buildStage() {

        Pixmap pixmap = new Pixmap(Gdx.files.internal("img/gun-pointer.png"));
        int xHotspot = pixmap.getWidth() / 2;
        int yHotspot = pixmap.getHeight() / 2;
        Cursor cursor = Gdx.graphics.newCursor(pixmap, xHotspot, yHotspot);
        Gdx.graphics.setCursor(cursor);
        pixmap.dispose();

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
        //create pop up table
        popUpTable = new Table();
        popUpTable.center();
        popUpTable.setVisible(false);
        rootTable.add(createHudPopUp());

        hudUpdate = new HudUpdate(killLogTable, ammoValue, playerCount, killCount);
    }

    @Override
    public void refresh() {
        ammoValue = Styles.INSTANCE.createGameLabel("");
        killCount = Styles.INSTANCE.createGameLabel("");
        playerCount = Styles.INSTANCE.createGameLabel("");
        tankXY = Styles.INSTANCE.createGameLabel("");
        direction = Styles.INSTANCE.createGameLabel("");
        popUpTable.setVisible(false);
        alertTable.setVisible(false);
        tractionHealth = true;
    }

    @Override
    public void render(float delta) {
        backgroundInput();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.fpsValue.setText(Gdx.graphics.getFramesPerSecond());
        Update newUpdate = Data.INSTANCE.nextUpdate();
        if (newUpdate != null) {
            update = newUpdate;
            player = update.getMe(Data.INSTANCE.getID());
        }
        if (player != null) {
//      this.camera.setPosititon(maxX - player.getX(), maxY - player.getY());
            this.camera.setPosititon(Helper.tileToScreenX(player.getX(), player.getY()), Helper.tileToScreenY(player.getX(), player.getY()));
//      this.tankXY.setText(Math.round(maxX - player.getX()) + ", " + Math.round(maxY - player.getY()));
            this.tankXY.setText(player.getX() / 32 + "," + (60 - player.getY() / 32));
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
        renderer.getBatch().end();


        //game
        game.setProjectionMatrix(this.camera.getCamera().combined);
        game.begin();

        var collection = new ArrayList<Container>();
        collection.addAll(returnTanks());
        collection.addAll(returnProjectiles());
        collection.addAll(returnPowerups());

        renderGame(game,collection);

        game.end();

        //health bar
        healthBar();

        //mini viewport
        minimapViewport.apply();
        minimap();
        tanksMiniMap.begin();
        tanksMiniMap.setProjectionMatrix(minimapViewport.getCamera().combined);
        if (update != null && update.getProjectiles() != null) {
            renderProjectiles(tanksMiniMap);
        }
        tanksMiniMap.setColor(Color.CYAN);
        if (update != null && update.getMe(Data.INSTANCE.getID()) != null) {
            renderContainer(update.getMe(Data.INSTANCE.getID()), tanksMiniMap);
        }
        if (update != null && update.getPowerups() != null) {
            renderPowerups(tanksMiniMap, true);
        }
        if (update != null && update.getNewShots() != null) {
            tanksMiniMap.setColor(Color.RED);
            for (Container c :
                    update.getNewShots().values()) {
                renderContainer(c, tanksMiniMap);
            }
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

    /*Isometric rendering begin*/

    public void renderGame(SpriteBatch sb, ArrayList<Container> objects) {
        int index = 0;
        for (ArrayList<Position> ps : rows) {
            for (Position pp : ps) {
                try {
                    var tile = ((TiledMapTileLayer) map.getLayers().get(1)).getCell(pp.getX(), pp.getY()).getTile();
                    drawTiles(sb, pp.getX(), pp.getY(), tile);
                    drawShadow(sb,pp.getX(), pp.getY(), shadow);
                } catch (Exception ignored) {
                }
            }
            for (var c : objects) {
                if((int)((Math.ceil(c.getX()/32f)+Math.ceil(60-(c.getY()/32f)))/2f)==index) {
                    if(c.getObjectType() == EntityType.BULLET) {
                        sb.draw(bulletTexture, Helper.tileToScreenX(c.getX() + 10, c.getY() - 10), Helper.tileToScreenY(c.getX() + 10, c.getY() - 10));
                    }else if(c.getObjectType() == EntityType.AMMO_POWERUP || c.getObjectType() == EntityType.HEALTH_POWERUP || c.getObjectType() == EntityType.DAMAGE_POWERUP || c.getObjectType() == EntityType.SPEED_POWERUP) {
                        sb.draw(powerUpTexture, Helper.tileToScreenX(c.getX() + 10, c.getY() - 10), Helper.tileToScreenY(c.getX() + 10, c.getY() - 10));
                        sb.draw(shadow,Helper.tileToScreenX(c.getX() + 10, c.getY() - 10), Helper.tileToScreenY(c.getX() + 10, c.getY() - 10));
                        sb.draw(shadowUltraLight,Helper.tileToScreenX(c.getX() + 10, c.getY() - 32 - 10), Helper.tileToScreenY(c.getX() + 10, c.getY() - 32 - 10));
                    }else if(c.getObjectType() == EntityType.TANK) {
                        //sb.draw(tankTexture, Helper.tileToScreenX(c.getX() + 10, c.getY() - 10), Helper.tileToScreenY(c.getX() + 10, c.getY() - 10));
                        int t = changeAngle(-getBearing(),90);
                        if (t==359) t = 0;
                        sb.draw(rotations.get(t),Helper.tileToScreenX(c.getX() + 10, c.getY() - 10), Helper.tileToScreenY(c.getX() + 10, c.getY() - 10));
                    }
                }
            }
            index++;
        }
    }

    private ArrayList<Container> returnProjectiles() {
        var array = new ArrayList<Container>();
        if (update != null) {
            array.addAll(update.getProjectiles().values());
        }
        return array;
    }

    private ArrayList<Container> returnTanks(){
        var array = new ArrayList<Container>();
        if (update != null) {

            for (int i = 0; i < update.getPlayers().values().size(); i++) {
                Container updater = update.getI(i);
                array.add(updater);
            }
        }
        return array;
    }

    private ArrayList<Container> returnPowerups() {
        var array = new ArrayList<Container>();
        if (update != null) {
            array.addAll(update.getPowerups().values());
        }
        return array;
    }

    private void drawTiles(SpriteBatch sb, int i, int j, TiledMapTile tile) {
        var pos = new Position((i) * 32, (j) * 32);

        var pos1 = new Position(pos.getX() - 960, pos.getY() - 960);
        var pos2 = new Position(pos1.getY(), -pos1.getX());
        var pos3 = new Position(pos2.getX() + 960, pos2.getY() + 960);
        var pos4 = Helper.tileToScreen(pos3);

        sb.draw(tile.getTextureRegion(), pos4.getX(), pos4.getY());
    }

    private void drawShadow(SpriteBatch sb, int i, int j, Texture shadow) {
        var pos = new Position((i+1) * 32, (j) * 32);

        var pos1 = new Position(pos.getX() - 960, pos.getY() - 960);
        var pos2 = new Position(pos1.getY(), -pos1.getX());
        var pos3 = new Position(pos2.getX() + 960, pos2.getY() + 960);
        var pos4 = Helper.tileToScreen(pos3);

        sb.draw(shadow, pos4.getX(), pos4.getY());
    }

    /*Isometric rendering ends*/

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.camera.getViewport().update(width, height);
        this.minimapViewport.update(width, height, true);
    }

    private Table createHudTopLeft() {
        Table topLeftTable = new Table();
        topLeftTable.top().left();
        topLeftTable.defaults().padTop(10).padLeft(10).left();
        Table aliveTable = new Table();
        Table aliveLabelTable = new Table();
        Styles.INSTANCE
                .addTableColour(aliveLabelTable, Color.GRAY);
        Label aliveLabel = Styles.INSTANCE.createGameLabel("Alive");
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
        Label killLabel = Styles.INSTANCE.createGameLabel("Killed");
        killLableTable.add(killLabel);
        Table killCountTable = new Table();
        Styles.INSTANCE
                .addTableColour(killCountTable, new Color(0f, 0f, 0f, 0.5f));
        killCountTable.add(killCount);
        killTable.row();
        killTable.add(tankXY);
        killTable.row();
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
        Label tempKill = Styles.INSTANCE.createGameLabel("");
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
        Label ammoLabel = Styles.INSTANCE.createGameLabel("Ammo");
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
            if (health > 0.1f) {
                tractionPopUp = true;
            }
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
        Label alertLabel = Styles.INSTANCE.createGameLabel("TRACTION DISABLED");
        alertTable.add(alertLabel);
        alertTable.setVisible(false);
        return alertTable;
    }


    private void renderProjectiles(SpriteBatch tanks) {
        if (update != null) {
            for (var c : update.getProjectiles().values()) {
                renderProjectiles(c, tanks);
            }
        }
    }

    private void renderPowerups(SpriteBatch tanks, boolean ortho) {
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
                }
                if (ortho) {
                    tanks.setColor(Color.WHITE);
                    renderOrthoContainer(c, tanks);
                } else {
                    tanks.setColor(Color.BLACK);
                    renderContainer(c, tanks);
                }
            }
        }
    }

    private void renderProjectiles(Container c, SpriteBatch batch) {
        if (c.getId().equals("")) {
            this.logger.trace(c.getId() + ": " + c.getX() + ", " + c.getY());
        }
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
        batch.draw(tankTexture, c.getX(), c.getY());
    }

    private void renderOrthoContainer(Container c, SpriteBatch batch) {
        if (c.getId().equals("")) {
            this.logger.trace(c.getId() + ": " + c.getX() + ", " + c.getY());
        }
        batch.draw(tankTexture2, c.getX(), c.getY());
    }

    private int getBearing() {
        Vector3 mouseMapPos3 = camera.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        Position mouseMapIso =
                Helper.screenToTile(new Position(
                        (int)(mouseMapPos3.x),
                        (int)(mouseMapPos3.y))
                );

        float deltaX = mouseMapIso.getX()-player.getX()+tankTexture.getWidth()/2f;
        float deltaY = mouseMapIso.getY()-player.getY()+tankTexture.getHeight()/2f;

        return (int)(Math.round(Math.toDegrees(Math.atan2(deltaY,deltaX))));
    }

    private void backgroundInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            //show the pop up table
            popUpTable.setVisible(true);
            tractionPopUp = false;
        }
        if (tractionHealth && tractionPopUp) {
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                Data.INSTANCE.sendCommand(Command.LEFT);
                //Data.INSTANCE.sendCommand(Command.DOWN);
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                Data.INSTANCE.sendCommand(Command.RIGHT);
                //Data.INSTANCE.sendCommand(Command.UP);
            } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                //Data.INSTANCE.sendCommand(Command.RIGHT);
                Data.INSTANCE.sendCommand(Command.DOWN);
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                //Data.INSTANCE.sendCommand(Command.LEFT);
                Data.INSTANCE.sendCommand(Command.UP);
            } else if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
                this.camera.getCamera().zoom -= 0.1f;
            } if (Gdx.input.isKeyPressed(Input.Keys.E)) {
                this.camera.getCamera().zoom += 0.1f;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonPressed(0)) {
            Data.INSTANCE.sendCommand(Command.SHOOT);
        }
        Data.INSTANCE.submit(changeAngle(getBearing(),180));
    }

    private int changeAngle(int angle, int change) {
        if (angle + change >= 0)
            return (angle + change) % 360;
        return (angle + change + 360) % 360;
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
}
