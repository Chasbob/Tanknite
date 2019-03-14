package com.aticatac.client.screens;

import com.aticatac.client.util.Camera;
import com.aticatac.client.util.Data;
import com.aticatac.client.util.HudUpdate;
import com.aticatac.client.util.Styles;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.model.Command;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.objectsystem.Container;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
    private Update update;
    private Table popUpTable;
    private Table alertTable;
    private Table killLogTable;
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
    private HudUpdate hudUpdate;
    private boolean traction;

    /**
     * Instantiates a new Game screen.
     */
    GameScreen() {
        super();
        maxX = 1920;
        maxY = 1920;
        try {
            player = new Container();
            ammoValue = UIFactory.createGameLabel("");
            killCount = UIFactory.createGameLabel(" 0 ");
            playerCount = UIFactory.createGameLabel(" 1 ");
            fpsValue = UIFactory.createGameLabel("");
            tankXY = UIFactory.createGameLabel("");
            direction = UIFactory.createGameLabel("");
            map = new TmxMapLoader().load("maps/map.tmx");
            tankTexture = new Texture("img/tank.png");
            traction = true;
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
        //create root table
        rootTable = new Table();
        rootTable.setFillParent(true);
        addActor(rootTable);
        Gdx.graphics.setVSync(true);
        //create table and labels for player count and kills - TOP LEFT
        super.addToRoot(createTopLeft());
        //create table for kill feed - BOTTOM LEFT
        super.addToRoot(createBottomLeft());
        //create table for ammo - BOTTOM RIGHT
        super.addToRoot(createBottomRight());
        //create alert table - BOTTOM MIDDLE
        super.addToRoot(createAlertTable());
        //create pop up table
        rootTable.add(createPopUp());
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
        hudUpdate = new HudUpdate(killLogTable, ammoValue, playerCount, killCount);
    }

    private Table createTopLeft(){
        Table topLeftTable = new Table();
        topLeftTable.top().left();
        topLeftTable.defaults().padTop(10).padLeft(10).left();
        Table aliveTable = new Table();
        Table aliveLabelTable = new Table();
        Styles.INSTANCE.addTableColour(aliveLabelTable, Color.GRAY);
        Label aliveLabel = UIFactory.createGameLabel("Alive");
        aliveLabelTable.add(aliveLabel);
        Table playerCountTable = new Table();
        playerCountTable.add(playerCount).center();
        Styles.INSTANCE.addTableColour(playerCountTable, Color.BLACK);
        aliveTable.add(playerCountTable);
        aliveTable.add(aliveLabelTable);
        Table killTable = new Table();
        Table killLableTable = new Table();
        Styles.INSTANCE.addTableColour(killLableTable, Color.GRAY);
        Label killLabel = UIFactory.createGameLabel("Killed");
        killLableTable.add(killLabel);
        Table killCountTable = new Table();
        Styles.INSTANCE.addTableColour(killCountTable, Color.BLACK);
        killCountTable.add(killCount);
        killTable.add(killCountTable);
        killTable.add(killLableTable);
        topLeftTable.add(aliveTable);
        topLeftTable.add(killTable);
        return topLeftTable;
    }

    private Table createTopRight(){
        //todo..radar
        return new Table();
    }

    private Table createBottomLeft(){
        killLogTable = new Table();
        killLogTable.bottom().left();
        killLogTable.defaults().padTop(10).padLeft(10).padBottom(20).left();
        Label tempKill = UIFactory.createGameLabel("");
        killLogTable.add(tempKill);
        return killLogTable;
    }

    private Table createBottomRight(){
        Table bottomRightTable = new Table();
        bottomRightTable.bottom().right();
        bottomRightTable.defaults().padRight(10).padTop(10).padBottom(20).left();
        Table ammoTable = new Table();
        Table ammoValueTable = new Table();
        Styles.INSTANCE.addTableColour(ammoValueTable, Color.BLACK);
        ammoValueTable.add(ammoValue);
        Label ammoLabel = UIFactory.createGameLabel("ammo");
        Table ammoLabelTable = new Table();
        Styles.INSTANCE.addTableColour(ammoLabelTable, Color.GRAY);
        ammoLabelTable.add(ammoLabel);
        ammoTable.add(ammoValueTable);
        ammoTable.add(ammoLabelTable);
        bottomRightTable.add(ammoTable);
        return bottomRightTable;
    }

    private Table createPopUp(){
        popUpTable = new Table();
        popUpTable.center();
        popUpTable.setVisible(false);
        popUpTable.defaults().pad(10).width(150);
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
        quitButton.addListener(UIFactory.newChangeScreenEvent(MainMenuScreen.class));
        quitButton.addListener(UIFactory.newListenerEvent(() -> {
            Data.INSTANCE.quit();
            refresh();
            return true;
        }));
        popUpTable.add(quitButton);
        Styles.INSTANCE.addTableColour(popUpTable, Color.BLACK);
        return popUpTable;
    }

    private Table createAlertTable(){
        alertTable = new Table();
        alertTable.bottom();
        alertTable.defaults().padBottom(20);
        Label alertLabel = UIFactory.createGameLabel("TRACTION DISABLED");
        alertTable.add(alertLabel);
        alertTable.setVisible(false);
        return alertTable;
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
        if (update != null && update.getMe(Data.INSTANCE.getID()) != null) {
            hudUpdate.update(update);
        }
        batch.begin();
        //health bar
        healthBar();
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

    private int getBearing() {
        Vector3 mouseMapPos3 = camera.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Vector2 mouseMapPos = new Vector2(mouseMapPos3.x, mouseMapPos3.y);
        Vector2 tankVec = new Vector2(player.getX(), player.getY());
        Vector2 mouseRel = new Vector2(mouseMapPos.x - maxX + tankVec.x, mouseMapPos.y - maxY + tankVec.y);
        return Math.round(mouseRel.angle());
    }


    private void backgroundInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            //show the pop up table
            popUpTable.setVisible(true);
        }
        if (traction) {
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
        if (Gdx.input.isButtonPressed(0)) {
            Data.INSTANCE.sendCommand(Command.SHOOT, getBearing());
        }

    }

    private void healthBar() {
        final float health = hudUpdate.getHealth();
        if (health > 0.6f) {
            batch.setColor(Color.GREEN);
        } else if (health <= 0.6f && health > 0.2f) {
            batch.setColor(Color.ORANGE);
        } else {
            batch.setColor(Color.RED);
        }
        batch.draw(Styles.getInstance().getBlank(), 0, 0, Gdx.graphics.getWidth() * health, 5);
        if (health <= 0.1f) {
            traction = false;
            alertTable.setVisible(true);
        } else {
            traction = true;
            alertTable.setVisible(false);
        }
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
        ammoValue = UIFactory.createGameLabel("");
        killCount = UIFactory.createGameLabel("");
        playerCount = UIFactory.createGameLabel("");
        tankXY = UIFactory.createGameLabel("");
        direction = UIFactory.createGameLabel("");
        popUpTable.setVisible(false);
        alertTable.setVisible(false);
        traction = true;
    }
}
