package com.aticatac.client.screens;

import com.aticatac.client.objectsystem.Renderer;
import com.aticatac.client.util.Styles;
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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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
    private final float speed = 3.0f;
    private Table popUpTable;
    private GameObject root;
    private GameObject tank;
    private Vector3 tankScreenCoords;
    private float health;
    private Label ammoValue;
    private Label killCount;
    private Label playerCount;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera cam;
    private boolean camSet;
    private float cameraHalfWidth;
    private float cameraHalfHeight;
    private int mapTop;
    private int mapBottom;
    private int mapRight;
    private int mapLeft;

    /**
     * Instantiates a new Game screen.
     */
    GameScreen() {
        super();
        try {
            health = 1f;
            ammoValue = UIFactory.createLabel("30");
            killCount = UIFactory.createLabel("0");
            playerCount = UIFactory.createLabel("1");
            map = new TmxMapLoader().load("maps/map.tmx");
            renderer = new OrthogonalTiledMapRenderer(map);
            cam = new OrthographicCamera(getWidth(), getHeight());
            camSet = false;
            mapLeft = 0;
            mapRight = 1920;
            mapBottom = 0;
            mapTop = 1920;
            cameraHalfWidth = cam.viewportWidth * .5f;
            cameraHalfHeight = cam.viewportHeight * .5f;
            Gdx.input.setInputProcessor(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        batch = new SpriteBatch();
    }

    @Override
    public void buildStage() {
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
    }

    @Override
    public void render(float delta) {
        //clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //set cam for renderer and render map
        renderer.setView(cam);
        renderer.render();
        //set polling for inputs
        batch.begin();
        if (Screens.INSTANCE.getRoot() != null) {
            this.root = Screens.INSTANCE.getRoot();
            this.tank = this.root.findObject(ObjectType.TANK, this.root);
            if (this.tank != null) {
                //if there exists a tank in the game we can poll input.
                input();
                cam.update();
            }
        }
        try {
            childRenderer(this.root);
        } catch (InvalidClassInstance | ComponentExistsException e) {
            this.getLogger().error(e);
        }
        //health bar
        healthBar();
        batch.draw(Styles.getInstance().getBlank(), 0, 0, getWidth() * health, 5);
        batch.setColor(Color.WHITE);
        batch.end();
        cam.update();
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
            var transform = c.getComponent(Transform.class);
            if (!camSet){
                //sets the camera and tank up when tank drops in to map
                setCam(transform);
                tankScreenCoords = getScreenCoords(new Vector3((float)transform.getX(), (float) transform.getY(), 0));
            }
            var t = c.getComponent(Renderer.class).getTexture();
            batch.setColor(Color.CORAL);
            batch.draw(new TextureRegion(t),
                    tankScreenCoords.x, tankScreenCoords.y,
                    t.getWidth() / 2f, t.getHeight() / 2f,
                    t.getWidth(), t.getHeight(),
                    1, 1,
                    (float) transform.getRotation());
            batch.setColor(Color.WHITE);
        }
    }

    private void setCam(Transform transform) {
        cam.position.set((float)transform.getX(), (float) transform.getY(), 0);
        camSet = true;
        cam.update();
    }

    /**
     * input.
     */
    private void input() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            //show the pop up table
            popUpTable.setVisible(true);
        }
        String direction;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction = "left";
            if (canMoveCam(direction)) {
                if (playerHorizontallyCentered()) {
                    //we can move cam
                    moveCam(direction);
                    Screens.INSTANCE.getClient().sendCommand(Command.LEFT);
                } else {
                    //need to move player
                    if (canMovePlayer(direction)) {
                        Screens.INSTANCE.getClient().sendCommand(Command.LEFT);
                        movePlayerLocal("left");
                    }
                }
            } else if (canMovePlayer(direction)) {
                Screens.INSTANCE.getClient().sendCommand(Command.LEFT);
                movePlayerLocal("left");
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction = "right";
            if (canMoveCam(direction)) {
                if (playerHorizontallyCentered()) {
                    //we can move cam
                    moveCam(direction);
                    Screens.INSTANCE.getClient().sendCommand(Command.RIGHT);
                } else {
                    //need to move player
                    if (canMovePlayer(direction)) {
                        movePlayerLocal("right");
                        Screens.INSTANCE.getClient().sendCommand(Command.RIGHT);
                    }
                }
            } else if (canMovePlayer(direction)) {
                movePlayerLocal("right");
                Screens.INSTANCE.getClient().sendCommand(Command.RIGHT);
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            direction = "up";
            if (canMoveCam(direction)) {
                if (playerVerticallyCentered()) {
                    //we can move cam
                    moveCam(direction);
                    Screens.INSTANCE.getClient().sendCommand(Command.UP);
                } else {
                    //need to move player
                    if (canMovePlayer(direction)) {
                        Screens.INSTANCE.getClient().sendCommand(Command.UP);
                        movePlayerLocal("up");
                    }
                }
            } else if (canMovePlayer(direction)) {
                Screens.INSTANCE.getClient().sendCommand(Command.UP);
                movePlayerLocal("up");
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction = "down";
            if (canMoveCam(direction)) {
                if (playerVerticallyCentered()) {
                    //we can move cam
                    moveCam(direction);
                    Screens.INSTANCE.getClient().sendCommand(Command.DOWN);
                } else {
                    //need to move player
                    if (canMovePlayer(direction)) {
                        movePlayerLocal("down");
                        Screens.INSTANCE.getClient().sendCommand(Command.DOWN);
                    }
                }
            } else if (canMovePlayer(direction)) {
                movePlayerLocal("down");
                Screens.INSTANCE.getClient().sendCommand(Command.DOWN);
            }
        }
    }

    private boolean playerHorizontallyCentered() {
        boolean result = false;
        //System.out.println("is "+ tankScreenCoords.x+" equal to " + cameraHalfWidth);
        if (tankScreenCoords.x == cameraHalfWidth) {
            result = true;
        }
        return result;
    }

    private boolean playerVerticallyCentered() {
        boolean result = false;
        if (tankScreenCoords.y == cameraHalfHeight) {
            result = true;
        }
        return result;
    }

    private boolean canMovePlayer(String direction) {
        switch (direction) {
            case "left": {
                float tempPlayerX = tankScreenCoords.x - speed;
                if (tempPlayerX >= 0) {
                    return true;
                }
                break;
            }
            case "right": {
                float tankWidth = 32;
                float tempPlayerX = tankScreenCoords.x + speed;
                if (tempPlayerX <= cam.viewportWidth - tankWidth) {
                    return true;
                }
                break;
            }
            case "up": {
                float tankHeight = 64;
                float tempPlayerY = tankScreenCoords.y + speed;
                if (tempPlayerY <= cam.viewportHeight - tankHeight) {
                    return true;
                }
                break;
            }
            case "down": {
                float tempPlayerY = tankScreenCoords.y - speed;
                if (tempPlayerY > 0) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private void movePlayerLocal(String direction) {
        float xDistance = 0;
        float yDistance = 0;
        int rotation = 0;
        switch (direction) {
            case "left": {
                xDistance = -speed;
                rotation = 90;
                break;
            }
            case "right": {
                xDistance = speed;
                rotation = 90;
                break;
            }
            case "up": {
                yDistance = speed;
                break;
            }
            case "down": {
                yDistance = -speed;
                break;
            }
        }
        tankScreenCoords = new Vector3(tankScreenCoords.x + xDistance, tankScreenCoords.y + yDistance, 0);
    }

    private boolean canMoveCam(String direction) {
        float cameraLeft = cam.position.x - cameraHalfWidth;
        float cameraRight = cam.position.x + cameraHalfWidth;
        float cameraBottom = cam.position.y - cameraHalfHeight;
        float cameraTop = cam.position.y + cameraHalfHeight;
        boolean result = false;
        switch (direction) {
            case "left": {
                //can the cam move 3.0 to the left.
                float tempCameraLeft = cameraLeft - speed;
                if (tempCameraLeft >= mapLeft) {
                    result = true;
                }
                break;
            }
            case "right": {
                float tempCameraRight = cameraRight + speed;
                if (tempCameraRight <= mapRight) {
                    result = true;
                }
                break;
            }
            case "up": {
                float tempCameraTop = cameraTop + speed;
                if (tempCameraTop <= mapTop) {
                    result = true;
                }
                break;
            }
            case "down": {
                float tempCamBottom = cameraBottom - speed;
                if (tempCamBottom >= mapBottom) {
                    result = true;
                }
                break;
            }
        }
        return result;
    }

    /**
     * Center camera to game object.
     */
    private void moveCam(String direction) {
        float xDistance = 0;
        float yDistance = 0;
        switch (direction) {
            case "left": {
                xDistance = -speed;
                break;
            }
            case "right": {
                xDistance = speed;
                break;
            }
            case "up": {
                yDistance = speed;
                break;
            }
            case "down": {
                yDistance = -speed;
                break;
            }
        }
        cam.position.set(cam.position.x + xDistance, cam.position.y + yDistance, 0);
        cam.update();
    }

    private Vector3 getScreenCoords(Vector3 worldCoords){
        //System.out.println("world x: " + worldCoords.x);
        Vector3 v = worldCoords;
        cam.project(v);
        //v.x = Math.round(v.x);
        //v.y = Math.round(v.y);
        //System.out.println("screen x: " + v.x);
        return v;
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
