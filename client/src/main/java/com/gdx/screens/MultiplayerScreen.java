package com.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.gdx.game.GDXGame;

public class MultiplayerScreen implements Screen {

    private GDXGame game;
    private Stage stage;
    private Label.LabelStyle titleStyle;
    private TextButton.TextButtonStyle buttonStyle;

    MultiplayerScreen(GDXGame game, Label.LabelStyle titleStyle, TextButton.TextButtonStyle buttonStyle) {
        this.game = game;
        this.titleStyle = titleStyle;
        this.buttonStyle = buttonStyle;
    }

    @Override
    public void show() {

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        //create root table
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        //add multiplayer label to root, creating styling for it as well
        Label screenTitle = new Label("Multi Player", titleStyle);
        screenTitle.setFillParent(true);
        rootTable.add(screenTitle).padTop(50).top();

        //create table to store host/join buttons
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        rootTable.addActor(buttonTable);
        buttonTable.defaults().pad(10).width(100).center();

        //create button for single player
        TextButton hostButton = new TextButton("Host", buttonStyle);
        hostButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //TODO call networking functions to host server
                game.setScreen(new GameScreen(game));
                return false;
            }
        });
        buttonTable.add(hostButton);

        //create button for multi player
        TextButton joinButton = new TextButton("Join", buttonStyle);
        joinButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //TODO call networking functions to join server
                game.setScreen(new GameScreen(game));
                return false;
            }
        });
        buttonTable.add(joinButton);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
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
        stage.dispose();
    }
}
