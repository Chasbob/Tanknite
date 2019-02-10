package com.aticatac.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.aticatac.client.util.ScreenEnum;
import com.aticatac.client.util.UIFactory;

/**
 * The type Main menu screen.
 */
public class MainMenuScreen extends AbstractScreen {

    /**
     * Instantiates a new Main menu screen.
     */
    public MainMenuScreen() {
        super();
    }

    @Override
    public void buildStage() {

        //create ui factory instance
        UIFactory ui = new UIFactory();

        //create root table
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        addActor(rootTable);

        Label screenTitle = ui.createLabel("Main Menu");
        screenTitle.setFillParent(true);
        rootTable.add(screenTitle).padTop(50).top();

        //create table to store all buttons
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        rootTable.addActor(buttonTable);
        buttonTable.defaults().pad(10).width(100).center();

        //create button for single player
        TextButton singlePlayerButton = ui.createButton("single player");
        singlePlayerButton.addListener(ui.createListener(ScreenEnum.GAME));
        buttonTable.add(singlePlayerButton);
        buttonTable.row();

        //create button for multi player
        TextButton multiPlayerButton = ui.createButton("Multi Player");
        multiPlayerButton.addListener(ui.createListener(ScreenEnum.MUlTIPLAYER));
        buttonTable.add(multiPlayerButton);
        buttonTable.row();

        //create button for leaderboard
        TextButton leaderboardButton = ui.createButton("Leaderboard");
        leaderboardButton.addListener(ui.createListener(ScreenEnum.LEADERBOARD));
        buttonTable.add(leaderboardButton);
        buttonTable.row();

        //create button for settings
        TextButton settingsButton = ui.createButton("Settings");
        settingsButton.addListener(ui.createListener(ScreenEnum.SETTINGS));
        buttonTable.add(settingsButton);
        buttonTable.row();

        //create button to close game
        TextButton exitButton = ui.createButton("Quit");
        exitButton.addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        Gdx.app.exit();
                        return false;
                    }
                });
        buttonTable.add(exitButton);

    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
