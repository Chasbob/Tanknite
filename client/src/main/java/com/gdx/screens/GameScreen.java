package com.gdx.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameScreen extends AbstractScreen {

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera cam;

    public GameScreen() {
        super();
        cam = new OrthographicCamera(640, 640);
        cam.position.set(640 / 2f, 640 / 2f, 0);
    }

    @Override
    public void buildStage() {
        map = new TmxMapLoader().load("maps/map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        renderer.setView(cam);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        cam.viewportWidth = width;
        cam.viewportHeight = height;
        cam.update();
    }

    @Override
    public void dispose() {
        super.dispose();
        map.dispose();
        renderer.dispose();
    }
}
