package com.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.gdx.game.GDXGame;

public class MainMenuScreen implements Screen {

    private GDXGame game;
    private Label.LabelStyle titleStyle;
    private TextButton.TextButtonStyle buttonStyle;
    private Stage stage;

    public MainMenuScreen(GDXGame game, Label.LabelStyle titleStyle, TextButton.TextButtonStyle buttonStyle) {
        this.game = game;
        this.titleStyle = titleStyle;
        this.buttonStyle = buttonStyle;
    }

    //screen methods
    @Override
    public void show() {

        //create new stage
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        //create root table
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        Label screenTitle = new Label("Main Menu", titleStyle);
        screenTitle.setFillParent(true);
        rootTable.add(screenTitle).padTop(50).top();

        //create table to store all buttons
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        rootTable.addActor(buttonTable);
        buttonTable.defaults().pad(10).width(100).center();

        //create button for single player
        TextButton singlePlayerButton = new TextButton("Single Player", buttonStyle);
        singlePlayerButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //TODO call networking functions like before
                game.setScreen(new GameScreen(game));
                return false;
            }
        });
        buttonTable.add(singlePlayerButton);
        buttonTable.row();

        //create button for multi player
        TextButton multiPlayerButton = new TextButton("Multi Player", buttonStyle);
        multiPlayerButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MultiplayerScreen(game, titleStyle, buttonStyle));
                return false;
            }
        });
        buttonTable.add(multiPlayerButton);
        buttonTable.row();

        //create button for leaderboard
        TextButton leaderboardButton = new TextButton("Leaderboard", buttonStyle);
        leaderboardButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new LeaderBoardScreen(game));
                return false;
            }
        });
        buttonTable.add(leaderboardButton);
        buttonTable.row();

        //create button for settings
        TextButton settingsButton = new TextButton("Settings", buttonStyle);
        settingsButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new SettingsScreen(game));
                return false;
            }
        });
        buttonTable.add(settingsButton);
        buttonTable.row();

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
        stage.getViewport().update(width, height, true);
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
