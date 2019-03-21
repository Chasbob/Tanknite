package com.aticatac.client.screens;

import com.aticatac.client.util.Data;
import com.aticatac.client.util.ListenerFactory;
import com.aticatac.client.util.Styles;
import com.aticatac.server.networking.Server;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

/**
 * The type Main menu screen.
 */
public class MainMenuScreen extends AbstractScreen {

  private boolean wantsToPlay;
  private boolean wantsToPlayMultiplayer;
  VerticalGroup verticalGroup;
  private Table modeTable;
  private Table multiplayerTable;
  Table soundTable;
  Table musicTable;
  private boolean changeSettings;
  int offsetForPosition;

  /**
   * Instantiates a new Main menu screen.
   */
  MainMenuScreen() {
    super();
    wantsToPlay = false;
    wantsToPlayMultiplayer = false;
    changeSettings = false;
  }

  @Override
  public void buildStage() {
    //create root table
    rootTable = new Table();
    rootTable.setFillParent(true);
    Styles.INSTANCE.addTableColour(rootTable, Color.valueOf("363636"));
    addActor(rootTable);
    //create title table
    Table titleTable = new Table().top().left().padTop(50).padLeft(50);
    super.addToRoot(titleTable);
    Label screenTitle = Styles.INSTANCE.createTitleLabel();
    titleTable.setFillParent(true);
    titleTable.add(screenTitle);
    //create table to store all buttons
    Table buttonTable = new Table().left().padLeft(50);
    super.addToRoot(buttonTable);
    //create vertical group to store buttons
    verticalGroup = new VerticalGroup();
    verticalGroup.columnLeft();
    verticalGroup.space(5);
    buttonTable.add(verticalGroup);
    //play button with child buttons
    createPlay();
    //button for achievements
    TextButton achievementButton = Styles.INSTANCE.createButton("Achievements");
    verticalGroup.addActor(achievementButton);
    //button for store
    TextButton storeButton = Styles.INSTANCE.createButton("Store");
    verticalGroup.addActor(storeButton);
    //button for leaderboard
    TextButton leaderboardButton = Styles.INSTANCE.createButton("Leaderboard");
    leaderboardButton.addListener(ListenerFactory.newListenerEvent(() -> {
      refresh();
      Screens.INSTANCE.showScreen(LeaderBoardScreen.class);
      return false;
    }));
    verticalGroup.addActor(leaderboardButton);
    //settings with child buttons
    createSettings();
    //create button to close game
    TextButton exitButton = Styles.INSTANCE.createBackButton("Quit");
    exitButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Gdx.app.exit();
      return false;
    }));
    verticalGroup.addActor(exitButton);
    verticalGroup.pack();
  }

  private void createPlay() {
    TextButton playButton = Styles.INSTANCE.createButton("Play");
    verticalGroup.addActor(playButton);
    playButton.addListener(ListenerFactory.newListenerEvent(() -> {
      if (wantsToPlay) {
        wantsToPlay = false;
        verticalGroup.removeActor(modeTable);
        offsetForPosition--;
        if (verticalGroup.getChildren().contains(multiplayerTable, true)) {
          verticalGroup.removeActor(multiplayerTable);
          wantsToPlayMultiplayer = false;
          offsetForPosition--;
        }
        verticalGroup.pack();
      } else {
        wantsToPlay = true;
        createPlayChildren();
        offsetForPosition++;
      }
      return false;
    }));

  }

  private void createPlayChildren() {
    modeTable = new Table();
    modeTable.padLeft(20);
    modeTable.defaults().padRight(20);
    //create button for single player
    TextButton singlePlayerButton = Styles.INSTANCE.createPopButton("Single-Player");
    singlePlayerButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Data.INSTANCE.setSingleplayer(true);
      Server server = new Server(true, "Single-Player");
      server.start();
      refresh();
      ListenerFactory.newChangeScreenAndReloadEvent(UsernameScreen.class);
      return false;
    }));
    modeTable.add(singlePlayerButton);
    //create button for multi player
    TextButton multiPlayerButton = Styles.INSTANCE.createPopButton("Multi-Player");
    multiPlayerButton.addListener(ListenerFactory.newListenerEvent(() -> {
      if (wantsToPlayMultiplayer) {
        wantsToPlayMultiplayer = false;
        verticalGroup.removeActor(multiplayerTable);
        offsetForPosition--;
        verticalGroup.pack();
      } else {
        wantsToPlayMultiplayer = true;
        createMultiplayerChildren();
        offsetForPosition++;
      }
      return false;
    }));
    modeTable.add(multiPlayerButton);
    verticalGroup.addActorAt(1, modeTable);
    verticalGroup.pack();
  }

  private void createMultiplayerChildren() {
    multiplayerTable = new Table();
    multiplayerTable.defaults().padRight(20);
    //create button for hosting game
    TextButton hostButton = Styles.INSTANCE.createLessPopButton("Host");
    hostButton.padLeft(270);
    hostButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Data.INSTANCE.setHosting(true);
      //reload username screen and show
      refresh();
      ListenerFactory.newChangeScreenAndReloadEvent(UsernameScreen.class);
      return false;
    }));
    multiplayerTable.add(hostButton);
    //create button for joining
    TextButton joinButton = Styles.INSTANCE.createLessPopButton("Join");
    joinButton.addListener(ListenerFactory.newListenerEvent(() -> {
      refresh();
      Screens.INSTANCE.getScreen(ServerScreen.class).refresh();
      Screens.INSTANCE.showScreen(ServerScreen.class);
      return false;
    }));
    multiplayerTable.add(joinButton);
    verticalGroup.addActorAt(2, multiplayerTable);
    verticalGroup.pack();
  }

  private void createSettings() {
    TextButton settingsButton = Styles.INSTANCE.createButton("Settings");
    settingsButton.addListener(ListenerFactory.newListenerEvent(() -> {
      if (changeSettings) {
        changeSettings = false;
        verticalGroup.removeActor(soundTable);
        verticalGroup.removeActor(musicTable);
        offsetForPosition = offsetForPosition - 2;
      } else {
        changeSettings = true;
        Settings.createSettings();
        offsetForPosition = offsetForPosition + 2;
      }
      return false;
    }));
    verticalGroup.addActor(settingsButton);
  }

  @Override
  public void refresh() {
    wantsToPlay = false;
    wantsToPlayMultiplayer = false;
    changeSettings = false;
    verticalGroup.removeActor(modeTable);
    verticalGroup.removeActor(multiplayerTable);
    verticalGroup.removeActor(musicTable);
    verticalGroup.removeActor(soundTable);
    verticalGroup.pack();
    offsetForPosition = 0;
  }


}
