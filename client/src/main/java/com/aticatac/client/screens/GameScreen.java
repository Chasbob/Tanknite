package com.aticatac.client.screens;

import com.aticatac.client.objectsystem.AddTexture;
import com.aticatac.client.objectsystem.ObjectHelper;
import com.aticatac.client.objectsystem.Renderer;
import com.aticatac.client.util.ScreenEnum;
import com.aticatac.client.util.UIFactory;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.common.objectsystem.RootObject;
import com.aticatac.common.prefab.Bullet;
import com.aticatac.common.prefab.Tank;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * The type Game screen.
 */
public class GameScreen extends AbstractScreen {

    private UIFactory uiFactory;
    private ScreenEnum prevScreen;
    private Table popUpTable;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera cam;

    private RootObject rootAbstract = new RootObject("Root");
    private GameObject root;
    private GameObject tank;
    private SpriteBatch batch;

    /**
     * Instantiates a new Game screen.
     */
    public GameScreen(ScreenEnum prevScreen, UIFactory uiFactory) {
        super();
        this.uiFactory = uiFactory;
        this.prevScreen = prevScreen;
        try {
            cam = new OrthographicCamera(640, 640);
            cam.position.set(getWidth()/2f,getHeight()/2f,cam.position.z);

            root = new GameObject("Root",rootAbstract);

            tank = new Tank("Tank1",root,new Position(getWidth()/2,getHeight()/2));

            ObjectHelper.AddRenderer(tank.children.get(0),"img/TankBottom.png");
            ObjectHelper.AddRenderer(tank.children.get(1),"img/TankTop.png");

        } catch (Exception e) {
            e.printStackTrace();
        }

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
        //rootTable.setPosition(640/2f, 640/2f);
        addActor(rootTable);

        //create pop up table
        popUpTable = new Table();
        rootTable.add(popUpTable);
        popUpTable.setVisible(false);
        popUpTable.defaults().pad(10).width(150).center();

        //create style for pop up tables
        Pixmap tableColour = new Pixmap(1, 1, Pixmap.Format.RGB565);
        tableColour.setColor(Color.DARK_GRAY);
        tableColour.fill();
        popUpTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(tableColour))));

        //create quit button go back to the main menu
        TextButton quitButton = uiFactory.createBackButton("quit");
        quitButton.addListener(uiFactory.createListener(ScreenEnum.MAIN_MENU, ScreenEnum.GAME, uiFactory));
        popUpTable.add(quitButton);

    }

    @Override
    public void render(float delta) {

        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        CenterCameraToGameObject(tank);

        renderer.render();

        Input();

        batch.begin();
        ChildRenderer(root);
        batch.end();

        cam.update();

        super.act(delta);
        super.draw();
    }

    private void ChildRenderer(GameObject g){
        for (var c:g.children) {
            ChildRenderer(c);

            if (c.componentExists(Renderer.class)) {
                Position p = c.getComponent(Transform.class).GetPosition();

                Texture t = c.getComponent(Renderer.class).getTexture();

                batch.draw(new TextureRegion(t),
                        (float) p.x - cam.position.x, (float) p.y - cam.position.y,
                        t.getWidth() / 2f, t.getHeight() / 2f,
                        t.getWidth(), t.getHeight(),
                        1, 1,
                        (float)c.getComponent(Transform.class).GetRotation());
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

    public void CenterCameraToGameObject(GameObject gameObject)
    {
        Position g = gameObject.getComponent(Transform.class).GetPosition();
        Position r = root.getComponent(Transform.class).GetPosition();
        //cam.position.set(cam.position.x+1,cam.position.y,cam.position.z);
        //cam.position.set((float)(r.x-g.x+getWidth()/2f),(float)(r.y-g.y+getHeight()/2f),cam.position.z);

        renderer.setView(cam);

    }

    @Override
    public void dispose() {
        super.dispose();
        map.dispose();
        renderer.dispose();
    }


    //TODO Convert game coord to Transform.class cord
    public float YLibGdx2YTransform(float y){
        y=y-cam.viewportHeight;
        y = - y;

        y = y+cam.viewportWidth/2f;

        return y;
    }

    public float TransformY2Libgdx(float y){
        y = y+cam.viewportWidth/2f;
        y = - y;
        y=y-cam.viewportHeight;
        return y;
    }

    public float XLibGdx2XTransform(float x){
        x = x+cam.viewportWidth/2f;
        return x;
    }


    //Input

    public void Input(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            //show the pop up table
            popUpTable.setVisible(true);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            tank.children.get(0).getComponent(Transform.class).SetRotation(90);
            tank.getComponent(Transform.class).Transform(3,0);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            tank.children.get(0).getComponent(Transform.class).SetRotation(-90);
            tank.getComponent(Transform.class).Transform(-3,0);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            tank.children.get(0).getComponent(Transform.class).SetRotation(0);
            tank.getComponent(Transform.class).Transform(0,-3);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            tank.children.get(0).getComponent(Transform.class).SetRotation(180);
            tank.getComponent(Transform.class).Transform(0,3);
        }

        mouseMoved(Gdx.input.getX(),Gdx.input.getY());
    }

    @Override
    public boolean keyDown(int keycode){
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
        if(button == Input.Buttons.LEFT){
            try {
                var bullet = new Bullet("Bullet",root);
                AddTexture.addBulletTexture(bullet);
                var newX =XLibGdx2XTransform(screenX);
                var newY =YLibGdx2YTransform(screenY);
                System.out.println("X:"+newX+"\nY:"+newY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(button == Input.Buttons.RIGHT){
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        var XMouse =XLibGdx2XTransform(screenX);
        var YMouse =YLibGdx2YTransform(screenY);
        var XTankTop = tank.children.get(1).transform.getX();
        var YTankTop = tank.children.get(1).transform.getY();

        var X = XMouse - XTankTop;
        var Y = YMouse - YTankTop;

        var rotation = Math.atan(Y/X);

        if(XMouse>=XTankTop)
            tank.children.get(1).transform.SetRotation(Math.toDegrees(rotation)-90f);
        else
            tank.children.get(1).transform.SetRotation(Math.toDegrees(rotation)+90f);

        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
