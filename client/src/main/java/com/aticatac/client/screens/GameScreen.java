package com.aticatac.client.screens;

import com.aticatac.client.objectsystem.Renderer;
import com.aticatac.client.util.Camera;
import com.aticatac.client.util.Styles;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.model.Command;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.common.objectsystem.ObjectType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
    private Table popUpTable;
    private GameObject root;
    private GameObject tank;
    private float health;
    private Label ammoValue;
    private Label killCount;
    private Label playerCount;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Camera camera;
    private Position playerPos;
    private Label fpsValue;
    private Label tankXY;

    /**
     * Instantiates a new Game screen.
     */
    GameScreen() {
        super();
        maxX = 1920;
        maxY = 1920;
        try {
            health = 1f;
            ammoValue = UIFactory.createLabel("30");
            killCount = UIFactory.createLabel("0");
            playerCount = UIFactory.createLabel("1");
            fpsValue = UIFactory.createLabel("");
            tankXY = UIFactory.createLabel("");
            map = new TmxMapLoader().load("maps/map.tmx");
            renderer = new OrthogonalTiledMapRenderer(map);
            this.camera = new Camera(maxX, maxY, getWidth(), getHeight());
            Gdx.input.setInputProcessor(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        batch = new SpriteBatch();
        tanks = new SpriteBatch();
    }

    @Override
    public void buildStage() {
        Gdx.graphics.setVSync(false);
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
        popUpTable.add(quitButton);
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(300);
                    this.fpsValue.setText(Gdx.graphics.getFramesPerSecond());
                    if (this.tank != null) {
                        tankXY.setText(this.tank.getTransform().getX() + ", " + this.tank.getTransform().getY());
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        (new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(16);
                    backgroundInput();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        })).start();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        //TODO figure out why it flickers when going side to side.
        //clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //set cam for renderer and render map
        if (Screens.INSTANCE.getRoot() != null) {
            this.root = Screens.INSTANCE.getRoot();
            this.tank = this.root.findObject(ObjectType.TANK, this.root);
            if (this.tank != null) {
                this.playerPos = tank.getTransform().getPosition();
                this.playerPos.setX(1920 - this.playerPos.getX());
                this.playerPos.setY(1920 - this.playerPos.getY());
                this.camera.setPosititon((float) playerPos.getX(), (float) playerPos.getY());
            }
        }
        renderer.setView(this.camera.getCamera());
        renderer.render();
        this.camera.update();
        tanks.setProjectionMatrix(this.camera.getCamera().combined);
        try {
            tanks.begin();
            childRenderer(this.tank);
            tanks.end();
        } catch (InvalidClassInstance | ComponentExistsException e) {
            logger.error(e);
        }
        batch.begin();
        //health bar
        healthBar();
        batch.draw(Styles.getInstance().getBlank(), 0, 0, getWidth() * health, 5);
        batch.setColor(Color.WHITE);
        batch.end();
        super.act(delta);
        super.draw();
    }

    private void childRenderer(GameObject g) throws InvalidClassInstance, ComponentExistsException {
        renderObject(g);
        for (var c : g.getChildren()) {
            childRenderer(c);
        }
    }

    private void renderObject(GameObject c) throws ComponentExistsException, InvalidClassInstance {
        if (c.hasTexture() && !c.componentExists(Renderer.class)) {
            c.addComponent(Renderer.class);
            c.getComponent(Renderer.class).setTexture(c.getTexture());
        }
        if (c.componentExists(Renderer.class)) {
//            var t = ;
            var pos = c.getComponent(Transform.class).getPosition();
            tanks.setColor(Color.CORAL);
            tanks.draw(c.getComponent(Renderer.class).getTexture(), 1920 - (float) pos.getX(), 1920 - (float) pos.getY());
        }
    }

    private void backgroundInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            //show the pop up table
            popUpTable.setVisible(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            Screens.INSTANCE.getClient().sendCommand(Command.LEFT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            Screens.INSTANCE.getClient().sendCommand(Command.RIGHT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            Screens.INSTANCE.getClient().sendCommand(Command.UP);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            Screens.INSTANCE.getClient().sendCommand(Command.DOWN);
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
}
