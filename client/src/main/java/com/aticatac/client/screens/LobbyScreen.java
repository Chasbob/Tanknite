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
    //create root table
    Table rootTable = new Table();
    rootTable.setFillParent(true);
    addActor(rootTable);
    //create data table
    Table dataTable = new Table();
    dataTable.setFillParent(true);
    rootTable.addActor(dataTable);
    //create table for waiting for players label and player count for the lobby
    Table lobbyDetailsTable = new Table();
    lobbyDetailsTable.setFillParent(true);
    dataTable.addActor(lobbyDetailsTable);
    lobbyDetailsTable.top().padTop(50);
    //add labels to lobbyDetailsTable
    Label waitingLabel = UIFactory.createLabel("Waiting for players..   ");
    lobbyDetailsTable.add(waitingLabel);
    Label countLabel = UIFactory.createLabel("0");
    lobbyDetailsTable.add(countLabel);
    Label maxLabel = UIFactory.createLabel("/10");
    lobbyDetailsTable.add(maxLabel);
    //add table with start button to load game
    Table startTable = new Table();
    startTable.setFillParent(true);
    startTable.top().padTop(100);
    TextButton startButton = UIFactory.createStartButton("Start");
    startTable.add(startButton);
    startButton.addListener(UIFactory.newListenerEvent(() -> {
      Data.INSTANCE.sendCommand(Command.START);
      return true;
    }));
    startButton.addListener(UIFactory.newChangeScreenEvent(GameScreen.class));
    dataTable.addActor(startTable);
    //add table to store players joining server
    Table playersTable = new Table();
    playersTable.setFillParent(true);
    playersTable.defaults().pad(10).left().width(450);
    playersTable.top().padTop(150);
    UIFactory.populateLobby(playersTable, countLabel);
    dataTable.addActor(playersTable);
    //create table to store back button
    Table backTable = new Table();
    backTable.setFillParent(true);
    rootTable.addActor(backTable);
    backTable.bottom();
    TextButton backButton = UIFactory.createBackButton("quit");
    backTable.add(backButton).bottom().padBottom(10);
    backButton.addListener(UIFactory.newChangeScreenEvent(MainMenuScreen.class));
  }
}
