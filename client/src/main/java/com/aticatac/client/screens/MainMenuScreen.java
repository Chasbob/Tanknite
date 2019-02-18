package com.aticatac.client.screens;

import com.aticatac.client.util.ScreenEnum;
import com.aticatac.client.util.UIFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * The type Main menu screen.
 */
public class MainMenuScreen extends AbstractScreen {
//    private List<TextButton> widgetList;
//    private HashMap<String, Widget> widgetHashMap;

    /**
     * Instantiates a new Main menu screen.
     */
    public MainMenuScreen( ) {
        super();
    }

//    public List<TextButton> getWidgetList() {
//        return widgetList;
//    }

    @Override
    public void buildStage() {
        //create root table
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        addActor(rootTable);
        Label screenTitle = UIFactory.createTitleLabel("Main Menu");
        screenTitle.setFillParent(true);
        rootTable.add(screenTitle).padTop(50).top();
        //create table to store all buttons
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        rootTable.addActor(buttonTable);
        buttonTable.defaults().pad(10).width(100).center();
        //create button for single player
        TextButton singlePlayerButton = UIFactory.createButton("single player");
        singlePlayerButton.addListener(UIFactory.createListener(ScreenEnum.USERNAME, ScreenEnum.MAIN_MENU));
        buttonTable.add(singlePlayerButton);
        buttonTable.row();
        //create button for multi player
        TextButton multiPlayerButton = UIFactory.createButton("Multi Player");
        multiPlayerButton.addListener(UIFactory.createListener(ScreenEnum.MUlTIPLAYER, ScreenEnum.MAIN_MENU));
        buttonTable.add(multiPlayerButton);
        buttonTable.row();
        //create button for leaderboard
        TextButton leaderboardButton = UIFactory.createButton("Leaderboard");
        leaderboardButton.addListener(UIFactory.createListener(ScreenEnum.LEADERBOARD, ScreenEnum.MAIN_MENU));
        buttonTable.add(leaderboardButton);
        buttonTable.row();
        //create button for settings
        TextButton settingsButton = UIFactory.createButton("Settings");
        settingsButton.addListener(UIFactory.createListener(ScreenEnum.SETTINGS, ScreenEnum.MAIN_MENU));
        buttonTable.add(settingsButton);
        buttonTable.row();
        //create button to close game
        TextButton exitButton = UIFactory.createBackButton("Quit");
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
