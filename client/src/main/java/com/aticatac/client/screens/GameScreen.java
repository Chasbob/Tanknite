package com.aticatac.client.screens;

import com.aticatac.client.objectsystem.Camera;
import com.aticatac.client.objectsystem.Renderer;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.common.objectsystem.RootObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

/**
 * The type Game screen.
 */
public class GameScreen extends AbstractScreen {

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    public RootObject rootAbstract = new RootObject("Root");
    public GameObject root;

    private GameObject tank;

    private SpriteBatch batch;

    /**
     * Instantiates a new Game screen.
     */
    public GameScreen() {
        super();

        try {
            root = new GameObject("Root",rootAbstract);
            root.addComponent(Camera.class);

            tank = new GameObject("Tank1",root);
            tank.addComponent(Renderer.class).setTexture("img/tank.png");

        } catch (Exception e) {
            e.printStackTrace();
        }

        batch = new SpriteBatch();

    }

    @Override
    public void buildStage() {
        map = new TmxMapLoader().load("maps/map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        renderer.setView(root.getComponent(Camera.class).cam);

        root.getComponent(Transform.class).Transform(0.3,0.3);

        root.getComponent(Camera.class).cam.update();
        renderer.render();

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            tank.getComponent(Transform.class).SetRotation(-90);
            tank.getComponent(Transform.class).Forward(3);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            tank.getComponent(Transform.class).SetRotation(90);
            tank.getComponent(Transform.class).Forward(3);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            tank.getComponent(Transform.class).SetRotation(0);
            tank.getComponent(Transform.class).Forward(-3);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            tank.getComponent(Transform.class).SetRotation(180);
            tank.getComponent(Transform.class).Forward(-3);
        }

        batch.begin();
        for (var c:root.children) {
            Position p = c.getComponent(Transform.class).GetPosition();

            batch.draw(c.getComponent(Renderer.class).getTexture(),(int)p.x , (int)p.y);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        root.getComponent(Camera.class).cam.viewportWidth = width;
        root.getComponent(Camera.class).cam.viewportHeight = height;
        root.getComponent(Camera.class).cam.update();
    }

    @Override
    public void dispose() {
        super.dispose();
        map.dispose();
        renderer.dispose();
    }
}
