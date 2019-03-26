package com.aticatac.client.screens;

import com.aticatac.client.util.Data;
import com.aticatac.client.util.ListenerFactory;
import com.aticatac.client.util.PopulatePlayers;
import com.aticatac.client.util.Styles;
import com.aticatac.common.model.Command;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;

/**
 * The type Lobby screen.
 */
public class LobbyScreen extends AbstractScreen {

  /**
   * Instantiates a new Lobby screen.
   */
  LobbyScreen() {
    super();
  }

  @Override
  public void buildStage() {
    super.buildStage();
    //create data table
    Table dataTable = new Table();
    dataTable.defaults().pad(10);
    super.addToRoot(dataTable);
    //add horizontal group to store looking label and player count
    HorizontalGroup lobbyInfoGroup = new HorizontalGroup();
    dataTable.add(lobbyInfoGroup);
    dataTable.row();
    //create lobby info actors
    Label lookingLabel = Styles.INSTANCE.createLabel("");
    lobbyInfoGroup.addActor(lookingLabel);
    Label countLabel = Styles.INSTANCE.createLabel("0");
    lobbyInfoGroup.addActor(countLabel);
    Label maxLabel = Styles.INSTANCE.createLabel("/10");
    lobbyInfoGroup.addActor(maxLabel);
    //add horizontal group to store buttons
    HorizontalGroup buttonGroup = new HorizontalGroup();
    buttonGroup.space(20);
    dataTable.add(buttonGroup);
    dataTable.row();
    //create button group actors
    if (Data.INSTANCE.isHosting()) {
      TextButton startButton = Styles.INSTANCE.createStartButton("START");
      startButton.addListener(ListenerFactory.newListenerEvent(() -> {
        Data.INSTANCE.sendCommand(Command.START);
        return true;
      }));
      buttonGroup.addActor(startButton);
      TextButton aiButton = Styles.INSTANCE.createStartButton("FILL AI");
      aiButton.addListener(ListenerFactory.newListenerEvent(() -> {
        Data.INSTANCE.sendCommand(Command.FILL_AI);
        return true;
      }));
      buttonGroup.addActor(aiButton);
    } else {
      Label waitingLabel = Styles.INSTANCE.createLabel("WAITING FOR HOST");
      buttonGroup.addActor(waitingLabel);
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
      Screens.INSTANCE.showScreen(GameScreen.class);
      refresh();
    }
  }
}
