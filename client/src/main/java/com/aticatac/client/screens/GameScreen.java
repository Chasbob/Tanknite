package com.aticatac.client.screens;

import com.aticatac.client.objectsystem.ObjectHelper;
import com.aticatac.client.objectsystem.Renderer;
import com.aticatac.client.util.Styles;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.model.Command;
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
    private GameObject root;
    private GameObject tank;
    private float health;
    private Label ammoValue;
    private Label killCount;
    private Label playerCount;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera cam;
    private final float speed = 3.0f;
    private float cameraHalfWidth;
    private float cameraHalfHeight;
    private int mapTop;
    private int mapBottom;
    private int mapRight;
    private int mapLeft;

    /**
     *
     * Instantiates a new Game screen.
     */
    GameScreen() {
        super();
        try {
            root = new GameObject("root");
            tank = new TankObject(root, "Tank", new Position(320, 320), 100, 100);
            ObjectHelper.AddRenderer(tank.getChildren().get(0), "img/tank.png");
            ObjectHelper.AddRenderer(tank.getChildren().get(1), "img/top.png");
//            //ObjectHelper.AddRenderer(tank.children.get(2), "img/white.png");
            health = 1f;
            ammoValue = UIFactory.createLabel("30");
            killCount = UIFactory.createLabel("0");
            playerCount = UIFactory.createLabel("1");
            map = new TmxMapLoader().load("maps/map.tmx");
            renderer = new OrthogonalTiledMapRenderer(map);
            cam = new OrthographicCamera(getWidth(), getHeight());
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
        cam.position.set(1000, 1000, 0);
        cam.update();
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
//        System.out.println("cam x: " + cam.position.x);
//        System.out.println("cam y: " + cam.position.y);
//        System.out.println("player x: " + tank.transform.getX());
//        System.out.println("player y: " + tank.transform.getY());
        //clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //set cam for renderer and render map
        renderer.setView(cam);
        renderer.render();
        //set polling for inputs
        batch.begin();
        input();
        childRenderer(root);
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
        super.act(delta);
        super.draw();
    }

    private void childRenderer(GameObject g) {
        for (var c : g.getChildren()) {
            childRenderer(c);
            if (c.componentExists(Renderer.class)) {
                Position p = c.getComponent(Transform.class).getPosition();
                Texture t = c.getComponent(Renderer.class).getTexture();
                batch.setColor(Color.CORAL);
                batch.draw(new TextureRegion(t),
                        (float) p.x, (float) p.y,
                        t.getWidth() / 2f, t.getHeight() / 2f,
                        t.getWidth(), t.getHeight(),
                        1, 1,
                        (float) c.getComponent(Transform.class).GetRotation());
                batch.setColor(Color.WHITE);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
//        super.resize(width, height);
//            cam = new OrthographicCamera(width, height);
//            cam.update();
//        }
    }

    @Override
    public void dispose() {
        super.dispose();
        map.dispose();
        renderer.dispose();
        batch.dispose();
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
            if(canMoveCam("left")){
                if(playerHorizontallyCentered()){
                    //we can move cam
                    moveCam(-speed, 0);
                    Screens.INSTANCE.getClient().sendCommand(Command.LEFT);
                }else{
                    //need to move player
                    if(canMovePlayer("left")){
                        movePlayer(speed, 0, 90);
                        Screens.INSTANCE.getClient().sendCommand(Command.LEFT);
                    }
                }
            }else if(canMovePlayer("left")){
                movePlayer(speed, 0, 90);
                Screens.INSTANCE.getClient().sendCommand(Command.LEFT);
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if(canMoveCam("right")){
                if(playerHorizontallyCentered()){
                    //we can move cam
                    moveCam(speed, 0);
                    Screens.INSTANCE.getClient().sendCommand(Command.RIGHT);
                }else{
                    //need to move player
                    if(canMovePlayer("right")){
                        movePlayer(-speed, 0, 90);
                        Screens.INSTANCE.getClient().sendCommand(Command.RIGHT);
                    }
                }
            }else if(canMovePlayer("right")){
                movePlayer(-speed, 0, 90);
                Screens.INSTANCE.getClient().sendCommand(Command.RIGHT);
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if(canMoveCam("up")){
                if(playerVerticallyCentered()){
                    //we can move cam
                    moveCam(0, speed);
                    Screens.INSTANCE.getClient().sendCommand(Command.UP);
                }else{
                    //need to move player
                    if(canMovePlayer("up")){
                        movePlayer(0, -speed, 0);
                        Screens.INSTANCE.getClient().sendCommand(Command.UP);
                    }
                }
            }else if(canMovePlayer("up")){
                movePlayer(0, -speed, 0);
                Screens.INSTANCE.getClient().sendCommand(Command.UP);
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if(canMoveCam("down")){
                if(playerVerticallyCentered()){
                    //we can move cam
                    moveCam(0, -speed);
                    Screens.INSTANCE.getClient().sendCommand(Command.DOWN);
                }else{
                    //need to move player
                    if(canMovePlayer("down")){
                        movePlayer(0, speed, 0);
                        Screens.INSTANCE.getClient().sendCommand(Command.DOWN);
                    }
                }
            }else if(canMovePlayer("down")){
                movePlayer(0, speed, 0);
                Screens.INSTANCE.getClient().sendCommand(Command.DOWN);
            }
        }
        cam.update();
        //mouseMoved(Gdx.input.getX(), Gdx.input.getY());
    }

    private boolean playerHorizontallyCentered() {
        boolean result = false;
        if(tank.getComponent(Transform.class).getX()==cameraHalfWidth){
            result = true;
        }
        return result;
    }

    private boolean playerVerticallyCentered() {
        boolean result = false;
        if(tank.getComponent(Transform.class).getY()==cameraHalfHeight){
            result = true;
        }
        return result;
    }

    private boolean canMovePlayer(String direction){
        switch (direction) {
            case "left": {
                float tempPlayerX = (float)tank.getComponent(Transform.class).getX() + speed;
                if (tempPlayerX <= cam.viewportWidth){
                    return true;
                }
                break;
            }
            case "right": {
                float tempPlayerX = (float)tank.getComponent(Transform.class).getX() - speed;
                float tankWidth = 32;
                if(tempPlayerX >= tankWidth){
                    return true;
                }
                break;
            }
            case "up": {
                float tempPlayerY = (float)tank.getComponent(Transform.class).getY() - speed;
                float tankHeight = 64;
                if(tempPlayerY >= tankHeight){
                    return true;
                }
                break;
            }
            case "down": {
                float tempPlayerY = (float)tank.getComponent(Transform.class).getY() + speed;
                if(tempPlayerY <= cam.viewportHeight){
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private void movePlayer(float xDistance, float yDistance, int rotation) {
        tank.children.get(0).getComponent(Transform.class).SetRotation(rotation);
        tank.getComponent(Transform.class).Transform(xDistance, yDistance);
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
                if (tempCameraRight<= mapRight) {
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
    private void moveCam(float xDistance, float yDistance) {
        // Move camera after player
        cam.position.set(cam.position.x + xDistance,cam.position.y + yDistance, 0);
        cam.update();
    }








    /**
     * Y lib gdx 2 y transform float.
     *
     * @param y the y
     * @return the float
     */
////TODO Convert game co-ord to Transform.class cord
//    private float YLibGdx2YTransform(float y) {
//        y = y - cam.viewportHeight;
//        y = -y;
//        y = y + cam.viewportWidth / 2f;
//        return y;
//    }

    /**
     * Transform y 2 libgdx float.
     *
     * @param y the y
     * @return the float
     */
//    public float TransformY2Libgdx(float y) {
//        y = y + cam.viewportWidth / 2f;
//        y = -y;
//        y = y - cam.viewportHeight;
//        return y;
//    }

    /**
     * X lib gdx 2 x transform float.
     *
     * @param x the x
     * @return the float
     */
//    private float XLibGdx2XTransform(float x) {
//        x = x + cam.viewportWidth / 2f;
//        return x;
//    }

//    @Override
//    public boolean keyDown(int keycode) {
//        return true;
//    }
//
//    @Override
//    public boolean keyUp(int keycode) {
//        return false;
//    }
//
//    @Override
//    public boolean keyTyped(char character) {
//        return true;
//    }

    //@Override
//    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//        super.touchDown(screenX, screenY, pointer, button);
//        if (button == input.Buttons.LEFT) {
//            try {
//
//                var newX = XLibGdx2XTransform(screenX);
//                var newY = YLibGdx2YTransform(screenY);
//                System.out.println("X:" + newX + "\nY:" + newY);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        if (button == input.Buttons.RIGHT) {
//        }
//        return false;
//    }

//    @Override
//    public boolean touchDragged(int screenX, int screenY, int pointer) {
//        return false;
//    }
//
//    @Override
//    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//        return false;
//    }

//    @Override
//    public boolean mouseMoved(int screenX, int screenY) {
//        var XMouse = XLibGdx2XTransform(screenX);
//        var YMouse = YLibGdx2YTransform(screenY);
//        var XTankTop = tank.getChildren().get(1).transform.getX();
//        var YTankTop = tank.getChildren().get(1).transform.getY();
//        var X = XMouse - XTankTop;
//        var Y = YMouse - YTankTop;
//        var rotation = Math.atan(Y / X);
////        if (XMouse >= XTankTop)
////            tank.getChildren().get(1).transform.SetRotation(Math.toDegrees(rotation) - 90f);
////        else
////            tank.getChildren().get(1).transform.SetRotation(Math.toDegrees(rotation) + 90f);
//        return false;
//    }
}
