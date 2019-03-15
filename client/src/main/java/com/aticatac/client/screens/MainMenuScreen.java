package com.aticatac.client.screens;

import com.aticatac.client.util.Data;
import com.aticatac.server.networking.Server;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * The type Main menu screen.
 */
public class MainMenuScreen extends AbstractScreen {

    /**
     * Instantiates a new Main menu screen.
     */
    MainMenuScreen() {
        super();
    }

  @Override
  public void buildStage() {
    //create root table
    rootTable = new Table();
    rootTable.setFillParent(true);
    addActor(rootTable);
    //create title
    Label screenTitle = UIFactory.createTitleLabel("TANK TROUBLE");
    screenTitle.setFillParent(true);
    rootTable.add(screenTitle).padTop(50).top();
    //create table to store all buttons
    Table buttonTable = new Table();
    super.addToRoot(buttonTable);
    buttonTable.defaults().pad(10).width(100).center();
    //create button for single player
    TextButton singlePlayerButton = UIFactory.createButton("single player");
    singlePlayerButton.addListener(UIFactory.newListenerEvent(() -> {
      Data.INSTANCE.setSingleplayer(true);
      Server server = new Server(true, "SinglePlayer");
      server.start();
      //reload the username screen and show it to show relevant fields
      UIFactory.newChangeScreenAndReloadEvent(UsernameScreen.class);
      return false;
    }));
    buttonTable.add(singlePlayerButton);
    buttonTable.row();
    //create button for multi player
    TextButton multiPlayerButton = UIFactory.createButton("Multi Player");
    multiPlayerButton.addListener(UIFactory.newListenerEvent(()->{
      Screens.INSTANCE.getScreen(MultiplayerScreen.class).refresh();
      Screens.INSTANCE.showScreen(MultiplayerScreen.class);
      return false;
    }));
    buttonTable.add(multiPlayerButton);
    buttonTable.row();
    //create button for leaderboard
    TextButton leaderboardButton = UIFactory.createButton("Leaderboard");
    leaderboardButton.addListener(UIFactory.newChangeScreenEvent(LeaderBoardScreen.class));
    buttonTable.add(leaderboardButton);
    buttonTable.row();
    //create button for settings
    TextButton settingsButton = UIFactory.createButton("Settings");
    settingsButton.addListener(UIFactory.newChangeScreenEvent(SettingsScreen.class));
    buttonTable.add(settingsButton);
    buttonTable.row();
    //create button to close game
    TextButton exitButton = UIFactory.createBackButton("Quit");
    exitButton.addListener(UIFactory.newListenerEvent(() -> {
      Gdx.app.exit();
      return false;
    }));
    buttonTable.add(exitButton);
  }

  @Override
  public void refresh() { }
}
