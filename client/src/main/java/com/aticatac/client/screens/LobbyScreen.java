package com.aticatac.client.screens;

import com.aticatac.client.util.Data;
import com.aticatac.common.model.Command;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

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
    //create table for waiting for players label and player count for the lobby
    Table lobbyDetailsTable = super.createTopLabelTable(dataTable);
    //add labels to lobbyDetailsTable
    Label lookingLabel = UIFactory.createLabel("Looking for players..   ");
    lobbyDetailsTable.add(lookingLabel);
    Label countLabel = UIFactory.createLabel("0");
    lobbyDetailsTable.add(countLabel);
    Label maxLabel = UIFactory.createLabel("/10");
    lobbyDetailsTable.add(maxLabel);
    //add table with start button or waiting for host label
    Table startTable = new Table();
    startTable.setFillParent(true);
    startTable.top().padTop(100);
    if (Data.INSTANCE.isHosting()) {
      TextButton startButton = UIFactory.createStartButton("Start");
      startTable.add(startButton);
      startButton.addListener(UIFactory.newListenerEvent(() -> {
        Data.INSTANCE.sendCommand(Command.START);
        while (Data.INSTANCE.getMe() == null) {
          Thread.sleep(0);
        }
        Screens.INSTANCE.showScreen(GameScreen.class);
        return true;
      }));
      startButton.addListener(UIFactory.newChangeScreenEvent(GameScreen.class));
    } else {
      Label waitingLabel = UIFactory.createColouredLabel("Waiting for Host");
      startTable.add(waitingLabel);
      new Thread(() -> {
        while ((Data.INSTANCE.peekUpdate() == null || !Data.INSTANCE.peekUpdate().isStart())) {
          try {
            Thread.sleep(0);
          } catch (InterruptedException e) {
            this.logger.error(e);
          }
        }
        Screens.INSTANCE.showScreen(GameScreen.class);
      }).start();
    }
    dataTable.addActor(startTable);
    //add table to store players joining server
    Table playersTable = new Table();
    playersTable.setFillParent(true);
    playersTable.defaults().pad(10).left().width(450);
    playersTable.top().padTop(150);
    //UIFactory.populateLobby(playersTable, countLabel);
    dataTable.addActor(playersTable);
  }

  @Override
  public void refresh() {
  }
}
