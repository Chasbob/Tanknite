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

    private UIFactory uiFactory;
    private ScreenEnum prevScreen;

    /**
     * Instantiates a new Main menu screen.
     */
    public MainMenuScreen(ScreenEnum prevScreen, UIFactory uiFactory) {
        super();
        this.prevScreen = prevScreen;
        this.uiFactory = uiFactory;
    }

    @Override
    public void buildStage() {

        //create root table
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        addActor(rootTable);

        Label screenTitle = uiFactory.createTitleLabel("Main Menu");
        screenTitle.setFillParent(true);
        rootTable.add(screenTitle).padTop(50).top();

        //create table to store all buttons
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        rootTable.addActor(buttonTable);
        buttonTable.defaults().pad(10).width(100).center();

        //create button for single player
        TextButton singlePlayerButton = uiFactory.createButton("single player");
        singlePlayerButton.addListener(uiFactory.createListener(ScreenEnum.USERNAME, ScreenEnum.MAIN_MENU, uiFactory));
        buttonTable.add(singlePlayerButton);
        buttonTable.row();

        //create button for multi player
        TextButton multiPlayerButton = uiFactory.createButton("Multi Player");
        multiPlayerButton.addListener(uiFactory.createListener(ScreenEnum.MUlTIPLAYER, ScreenEnum.MAIN_MENU, uiFactory));
        buttonTable.add(multiPlayerButton);
        buttonTable.row();

        //create button for leaderboard
        TextButton leaderboardButton = uiFactory.createButton("Leaderboard");
        leaderboardButton.addListener(uiFactory.createListener(ScreenEnum.LEADERBOARD, ScreenEnum.MAIN_MENU, uiFactory));
        buttonTable.add(leaderboardButton);
        buttonTable.row();

        //create button for settings
        TextButton settingsButton = uiFactory.createButton("Settings");
        settingsButton.addListener(uiFactory.createListener(ScreenEnum.SETTINGS, ScreenEnum.MAIN_MENU, uiFactory));
        buttonTable.add(settingsButton);
        buttonTable.row();

        //create button to close game
        TextButton exitButton = uiFactory.createBackButton("Quit");
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
