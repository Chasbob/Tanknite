package com.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdx.game.GDXGame;

public class SplashScreen implements Screen {

    private GDXGame game;
    private SpriteBatch spriteBatch;
    private Texture splsh;

    public SplashScreen(GDXGame g) {
        this.game = g;
    }


    @Override
    public void show() {

        //spriteBatch = new SpriteBatch();
        //splsh = new Texture(Gdx.files.internal("splash.gif"));

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //spriteBatch.begin();
        //spriteBatch.draw(splsh, 0, 0);
        //spriteBatch.end();

//        if (Gdx.input.justTouched()) {
//            game.setScreen(new MainMenuScreen(game,));
//        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
