package com.aticatac.client.screens;

import com.aticatac.client.objectsystem.ObjectHelper;
import com.aticatac.client.objectsystem.Renderer;
import com.aticatac.client.util.Styles;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.model.Command;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.objectsystem.Converter;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.server.prefabs.TankObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
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
    private Table popUpTable;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera cam;
    private GameObject root;
    private float health;

    /**
     * Instantiates a new Game screen.
     */
    GameScreen() {
        super();
        try {
            cam = new OrthographicCamera(getWidth(), getHeight());
            cam.position.set(getWidth() / 2f, getHeight() / 2f, cam.position.z);
            root = new GameObject("root");
            //ObjectHelper.AddRenderer(tank.children.get(2), "img/white.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        health = 1f;
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void buildStage() {
        //load in map
        map = new TmxMapLoader().load("maps/map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
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
        countTable.row();
        Label kills = UIFactory.createLabel("kills: ");
        countTable.add(kills);
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
//        (new Thread(() -> {
//            //TODO remove testing thread
//            while (true) {
////                try {
//                    Update update = Screens.INSTANCE.getUpdate();
//                    System.out.println(Converter.Constructor(update.getObj()));
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
//            }
//        })).start();
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Update update = Screens.INSTANCE.getUpdate();
        if (update.getObj() != null) {
            this.root = Converter.Constructor(update.getObj());
            try {
                if (root != null) {
                    for (var c:root.getChildren().get(0).getChildren()){
                        var tank = new TankObject(root, "Tank", new Position(getWidth() / 2 + c.transform.getX(), getHeight() / 2+ c.transform.getY()), 100, 100);
                        ObjectHelper.AddRenderer(tank.getChildren().get(0), "img/TankBottom.png");
                        ObjectHelper.AddRenderer(tank.getChildren().get(1), "img/TankTop.png");
                    }
                }
            }catch (Exception e){}
            this.getLogger().trace("Root position: " + this.root.transform.getPosition().toString());
        }
        renderer.setView(cam);
        renderer.render();
        input();
        batch.begin();
        ChildRenderer(root);
        //health bar
        if (health > 0.6f) {
            batch.setColor(Color.GREEN);
        } else if (health < 0.6f && health > 0.2f) {
            batch.setColor(Color.ORANGE);
        } else {
            batch.setColor(Color.RED);
        }
        batch.draw(Styles.getInstance().getBlank(), 0, 0, getWidth() * health, 5);
        batch.setColor(Color.WHITE);
        batch.end();
        cam.update();
        super.act(delta);
        super.draw();
    }

    private void ChildRenderer(GameObject g) {
        for (var c : g.getChildren()) {
            ChildRenderer(c);
            if (c.componentExists(Renderer.class)) {
                Position p = c.getComponent(Transform.class).getPosition();
                Texture t = c.getComponent(Renderer.class).getTexture();
                batch.draw(new TextureRegion(t),
                        (float) p.x - cam.position.x, (float) p.y - cam.position.y,
                        t.getWidth() / 2f, t.getHeight() / 2f,
                        t.getWidth(), t.getHeight(),
                        1, 1,
                        (float) c.getComponent(Transform.class).GetRotation());
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        cam.viewportWidth = width;
        cam.viewportHeight = height;
        cam.update();
    }

    /**
     * Center camera to game object.
     *
     * @param gameObject the game object
     */
    private void CenterCameraToGameObject(GameObject gameObject) {
        Position g = gameObject.getComponent(Transform.class).getPosition();
        Position r = root.getComponent(Transform.class).getPosition();
        renderer.setView(cam);
    }

    @Override
    public void dispose() {
        super.dispose();
        map.dispose();
        renderer.dispose();
    }

    /**
     * Y lib gdx 2 y transform float.
     *
     * @param y the y
     *
     * @return the float
     */
//TODO Convert game co-ord to Transform.class cord
    private float YLibGdx2YTransform(float y) {
        y = y - cam.viewportHeight;
        y = -y;
        y = y + cam.viewportWidth / 2f;
        return y;
    }

    /**
     * Transform y 2 libgdx float.
     *
     * @param y the y
     *
     * @return the float
     */
    public float TransformY2Libgdx(float y) {
        y = y + cam.viewportWidth / 2f;
        y = -y;
        y = y - cam.viewportHeight;
        return y;
    }

    /**
     * X lib gdx 2 x transform float.
     *
     * @param x the x
     *
     * @return the float
     */
    private float XLibGdx2XTransform(float x) {
        x = x + cam.viewportWidth / 2f;
        return x;
    }

    /**
     * input.
     */
    private void input() {
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
        mouseMoved(Gdx.input.getX(), Gdx.input.getY());
    }

    @Override
    public boolean keyDown(int keycode) {
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        super.touchDown(screenX, screenY, pointer, button);
        if (button == Input.Buttons.LEFT) {
            try {
                var newX = XLibGdx2XTransform(screenX);
                var newY = YLibGdx2YTransform(screenY);
                System.out.println("X:" + newX + "\nY:" + newY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (button == Input.Buttons.RIGHT) {
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
//        var XMouse = XLibGdx2XTransform(screenX);
//        var YMouse = YLibGdx2YTransform(screenY);
//        var XTankTop = tank.getChildren().get(1).transform.getX();
//        var YTankTop = tank.getChildren().get(1).transform.getY();
//        var X = XMouse - XTankTop;
//        var Y = YMouse - YTankTop;
//        var rotation = Math.atan(Y / X);
//        if (XMouse >= XTankTop)
//            tank.getChildren().get(1).transform.SetRotation(Math.toDegrees(rotation) - 90f);
//        else
//            tank.getChildren().get(1).transform.SetRotation(Math.toDegrees(rotation) + 90f);
        return false;
    }
}
