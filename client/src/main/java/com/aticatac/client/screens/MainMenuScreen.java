package com.aticatac.client.screens;

import com.aticatac.client.util.Data;
import com.aticatac.server.networking.Server;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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
  private VerticalGroup verticalGroup;
  private Table modeTable;
  private Table multiplayerTable;
  private Table soundTable;
  private Table musicTable;
  private boolean changeSettings;
  private boolean sound;
  private boolean music;
  private int offsetForPosition;

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
    addActor(rootTable);
    //create title table
    Table titleTable = new Table().top().left().padTop(50);
    super.addToRoot(titleTable);
    Label screenTitle = UIFactory.createTitleLabel("TANK TROUBLE");
    titleTable.setFillParent(true);
    titleTable.add(screenTitle);
    //create table to store all buttons
    Table buttonTable = new Table().left();
    super.addToRoot(buttonTable);
    //create vertical group to store buttons
    verticalGroup = new VerticalGroup();
    verticalGroup.columnLeft();
    verticalGroup.space(15);
    buttonTable.add(verticalGroup);
    //play button with child buttons
    createPlay();
    //button for achievements
    TextButton achievementButton = UIFactory.createButton("Achievements");
    verticalGroup.addActor(achievementButton);
    //button for store
    TextButton storeButton = UIFactory.createButton("Store");
    verticalGroup.addActor(storeButton);
    //button for leaderboard
    TextButton leaderboardButton = UIFactory.createButton("Leaderboard");
    leaderboardButton.addListener(UIFactory.newListenerEvent(() -> {
      refresh();
      Screens.INSTANCE.showScreen(LeaderBoardScreen.class);
      return false;
    }));
    verticalGroup.addActor(leaderboardButton);
    //settings with child buttons
    createSettings();
    //create button to close game
    TextButton exitButton = UIFactory.createBackButton("Quit");
    exitButton.addListener(UIFactory.newListenerEvent(() -> {
      Gdx.app.exit();
      return false;
    }));
    verticalGroup.addActor(exitButton);
    verticalGroup.pack();
  }

  private void createPlay() {
    TextButton playButton = UIFactory.createButton("Play");
    verticalGroup.addActor(playButton);
    playButton.addListener(UIFactory.newListenerEvent(() -> {
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
        System.out.println(offsetForPosition);
      } else {
        wantsToPlay = true;
        createPlayChildren();
        offsetForPosition++;
        System.out.println(offsetForPosition);
      }
      return false;
    }));

  }

  private void createPlayChildren() {
    modeTable = new Table();
    modeTable.padLeft(20);
    modeTable.defaults().padRight(20);
    //create button for single player
    TextButton singlePlayerButton = UIFactory.createLessSubtleButton("Single-Player");
    singlePlayerButton.addListener(UIFactory.newListenerEvent(() -> {
      Data.INSTANCE.setSingleplayer(true);
      Server server = new Server(true, "Single-Player");
      System.out.println(Data.INSTANCE.isManualConfigForServer());
      server.start();
      refresh();
      UIFactory.newChangeScreenAndReloadEvent(UsernameScreen.class);
      return false;
    }));
    modeTable.add(singlePlayerButton);
    //create button for multi player
    TextButton multiPlayerButton = UIFactory.createLessSubtleButton("Multi-Player");
    multiPlayerButton.addListener(UIFactory.newListenerEvent(() -> {
      System.out.println(wantsToPlayMultiplayer);
      if (wantsToPlayMultiplayer) {
        wantsToPlayMultiplayer = false;
        verticalGroup.removeActor(multiplayerTable);
        offsetForPosition--;
        verticalGroup.pack();
        System.out.println(offsetForPosition);
      } else {
        wantsToPlayMultiplayer = true;
        createMultiplayerChildren();
        offsetForPosition++;
        System.out.println(offsetForPosition);
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
    TextButton hostButton = UIFactory.createSubtleButton("Host");
    hostButton.padLeft(270);
    hostButton.addListener(UIFactory.newListenerEvent(() -> {
      Data.INSTANCE.setHosting(true);
      //reload username screen and show
      refresh();
      UIFactory.newChangeScreenAndReloadEvent(UsernameScreen.class);
      return false;
    }));
    multiplayerTable.add(hostButton);
    //create button for joining
    TextButton joinButton = UIFactory.createSubtleButton("Join");
    joinButton.addListener(UIFactory.newListenerEvent(() -> {
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
    TextButton settingsButton = UIFactory.createButton("Settings");
    settingsButton.addListener(UIFactory.newListenerEvent(() -> {
      if (changeSettings) {
        changeSettings = false;
        verticalGroup.removeActor(soundTable);
        verticalGroup.removeActor(musicTable);
        offsetForPosition = offsetForPosition - 2;
        System.out.println(offsetForPosition);
      } else {
        changeSettings = true;
        createSettingsChildren();
        offsetForPosition = offsetForPosition + 2;
        System.out.println(offsetForPosition);
      }
      return false;
    }));
    verticalGroup.addActor(settingsButton);
  }

  private void createSettingsChildren() {
    //create table to store setting toggles
    soundTable = createToggle("Toggle Sound: ");
    musicTable = createToggle("Toggle Music: ");
    verticalGroup.addActorAt(offsetForPosition + 5, soundTable);
    verticalGroup.addActorAt(offsetForPosition + 6, musicTable);
  }

  private Table createToggle(String labelString) {
    Table table = new Table().padLeft(20);
    table.defaults().padRight(10);
    TextButton button = UIFactory.createLessSubtleButton(labelString);
    Label label;
    if ((labelString.equals("Toggle Sound: ") && sound) || (labelString.equals("Toggle Music: ") && music)) {
      label = UIFactory.createSubtleLabel("ON");
    } else {
      label = UIFactory.createSubtleLabel("OFF");
    }
    button.addListener(buttonToChangeBool(button, label));
    table.add(button);
    table.add(label);
    return table;
  }

  private InputListener buttonToChangeBool(TextButton button, Label label) {
    return UIFactory.newListenerEvent(() -> {
      if (button.getText().toString().equals("Toggle Sound: ")) {
        if (sound) {
          setSound(false);
          label.setText("OFF");
        } else {
          setSound(true);
          label.setText("ON");
        }
      } else {
        if (music) {
          setMusic(false);
          label.setText("OFF");
        } else {
          setMusic(true);
          label.setText("ON");
        }
      }
      return false;
    });
  }

  private void setMusic(boolean music) {
    this.music = music;
  }

  private void setSound(boolean sound) {
    this.sound = sound;
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
