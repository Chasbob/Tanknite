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

  private int tabIndex;
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
      MenuTable startTable = Styles.INSTANCE.createMenuTable(true, true);
      TextButton startButton = Styles.INSTANCE.createButton("START");
      ListenerFactory.addHoverListener(startButton, startTable);
      startTable.setButton(startButton);
      startButton.addListener(ListenerFactory.newListenerEvent(() -> {
        Data.INSTANCE.sendCommand(Command.START);
        return true;
      }));
      buttonGroup.addActor(startTable);
      MenuTable aiTable = Styles.INSTANCE.createMenuTable(false, true);
      TextButton aiButton = Styles.INSTANCE.createButton("FILL AI");
      ListenerFactory.addHoverListener(aiButton, aiTable);
      aiButton.addListener(ListenerFactory.newListenerEvent(() -> {
        Data.INSTANCE.sendCommand(Command.FILL_AI);
        return true;
      }));
      aiTable.setButton(aiButton);
      buttonGroup.addActor(aiTable);
    } else {
      MenuTable waitingTable = Styles.INSTANCE.createMenuTable(false, false);
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
      Screens.INSTANCE.showScreen(GameScreenIsometric.class);
      refresh();
    }
    keyPressed();
  }

  private void keyPressed() {
    if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
      switchTab(true);
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
      switchTab(false);
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
      enterPressed();
    }
  }

  private void enterPressed() {
    if (Data.INSTANCE.isHosting()) {
      //get current tab
      MenuTable currentTab = (MenuTable) buttonGroup.getChildren().get(tabIndex);
      //create an input event to fire the button
      InputEvent inputEvent = new InputEvent();
      inputEvent.setType(InputEvent.Type.touchDown);
      currentTab.getButton().fire(inputEvent);
    }
  }

  private void switchTab(boolean right) {
    //get current tab and unselect
    MenuTable currentTab = (MenuTable) buttonGroup.getChildren().get(tabIndex);
    currentTab.setShowGroup(false);
    MenuTable menuTable = (MenuTable) buttonGroup.getChildren().get(tabIndex);
    menuTable.setShowGroup(false);
    tabIndex = super.applyIndex(buttonGroup, tabIndex, right);
    //get new tab
    MenuTable newTab = (MenuTable) buttonGroup.getChildren().get(tabIndex);
    newTab.setShowGroup(true);
  }
}
