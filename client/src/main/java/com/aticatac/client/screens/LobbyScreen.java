package com.aticatac.client.screens;

import com.aticatac.client.util.*;
import com.aticatac.common.model.Command;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;

/**
 * The type Lobby screen.
 */
public class LobbyScreen extends AbstractScreen {

  private HorizontalGroup buttonGroup;

  /**
   * Instantiates a new Lobby screen.
   */
  LobbyScreen() {
    super();
    Gdx.input.setInputProcessor(this);
  }

  @Override
  public void buildStage() {
    super.buildStage();
    //create data table
    Table dataTable = new Table();
    dataTable.defaults();
    super.addToRoot(dataTable);
    //add horizontal group to store looking label and player count
    HorizontalGroup lobbyInfoGroup = new HorizontalGroup();
    dataTable.add(lobbyInfoGroup).padBottom(20);
    dataTable.row();
    //create lobby info actors
    Label lookingLabel = Styles.INSTANCE.createCustomLabel("", Color.CORAL);
    lobbyInfoGroup.addActor(lookingLabel);
    Label countLabel = Styles.INSTANCE.createCustomLabel("0", Color.CORAL);
    lobbyInfoGroup.addActor(countLabel);
    Label maxLabel = Styles.INSTANCE.createCustomLabel("/10", Color.CORAL);
    lobbyInfoGroup.addActor(maxLabel);
    //add horizontal group to store buttons
    buttonGroup = new HorizontalGroup();
    dataTable.add(buttonGroup);
    dataTable.row();
    //create button group actors
    if (Data.INSTANCE.isHosting()) {
      Table startTable = Styles.INSTANCE.createPopUpTable();
      TextButton startButton = Styles.INSTANCE.createButton("START");
      ListenerFactory.addHoverListener(startButton, startTable);
      startTable.add(startButton);
      startButton.addListener(ListenerFactory.newListenerEvent(() -> {
        Data.INSTANCE.sendCommand(Command.START);
        return true;
      }));
      buttonGroup.addActor(startTable);
      Table aiTable = Styles.INSTANCE.createPopUpTable();
      TextButton aiButton = Styles.INSTANCE.createButton("FILL AI");
      ListenerFactory.addHoverListener(aiButton, aiTable);
      aiButton.addListener(ListenerFactory.newListenerEvent(() -> {
        Data.INSTANCE.sendCommand(Command.FILL_AI);


        return true;
      }));
      aiTable.add(aiButton);
      buttonGroup.addActor(aiTable);
    } else {
      Table waitingTable = Styles.INSTANCE.createPopUpTable();
      Label waitingLabel = Styles.INSTANCE.createLabel("WAITING FOR HOST");
      waitingTable.add(waitingLabel);
      buttonGroup.addActor(waitingTable);
    }
    //add table to store player group
    Table playersContainer = new Table();
    Styles.INSTANCE.addTableColour(playersContainer, Color.DARK_GRAY);
    dataTable.add(playersContainer);
    //add vertical group to store players
    VerticalGroup playersGroup = new VerticalGroup();
    playersGroup.space(5).columnLeft();
    playersContainer.add(playersGroup).left().pad(10);
    //populate players
    PopulatePlayers populatePlayers = new PopulatePlayers(playersGroup, countLabel, lookingLabel);
    populatePlayers.start();
  }

  @Override
  public void refresh() {
    Data.INSTANCE.setHosting(false);
    Data.INSTANCE.setManualConfigForServer(false);
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    if (Data.INSTANCE.isStarted()) {
      if (Data.INSTANCE.isIso()) {
        Screens.INSTANCE.reloadScreen(GameScreenIsometric.class);
        Screens.INSTANCE.showScreen(GameScreenIsometric.class);
      } else {
        Screens.INSTANCE.reloadScreen(GameScreen.class);
        Screens.INSTANCE.showScreen(GameScreen.class);
      }
      refresh();
    }
  }
}
