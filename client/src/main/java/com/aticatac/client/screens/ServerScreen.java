package com.aticatac.client.screens;

import com.aticatac.client.util.Data;
import com.aticatac.client.util.ListServers;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * The type Server screen.
 */
public class ServerScreen extends AbstractScreen {

  private Boolean serverSelected;
  private ListServers listServers;

  /**
   * Instantiates a new Server screen.
   */
  ServerScreen() {
    super();
    refresh();
  }

  public void setServerSelected(Boolean serverSelected) {
    this.serverSelected = serverSelected;
  }

  @Override
  public void buildStage() {
    super.buildStage();
    //create data table
    Table dataTable = new Table();
    //create table for top labels
    Table serverDetailsTable = super.createTopLabelTable(dataTable);
    Label waitingLabel = UIFactory.createLabel("servers");
    serverDetailsTable.add(waitingLabel);
    //add table with join button to get into lobby after entering username
    Table buttonTable = new Table();
    buttonTable.setFillParent(true);
    dataTable.addActor(buttonTable);
    buttonTable.top().padTop(100);
    TextButton joinButton = UIFactory.createStartButton("Join");
    buttonTable.defaults().padLeft(25).padRight(25);
    buttonTable.add(joinButton);
    joinButton.addListener(UIFactory.newListenerEvent(() -> {
      if (serverSelected) {
        UIFactory.newChangeScreenAndReloadEvent(UsernameScreen.class);
      }
      return false;
    }
    ));
    TextButton manualButton = UIFactory.createStartButton("Manual");
    manualButton.addListener(UIFactory.newListenerEvent(() -> {
      Data.INSTANCE.setManualConfigForServer(true);
      UIFactory.newChangeScreenAndReloadEvent(UsernameScreen.class);
      return false;
    }));
    buttonTable.add(manualButton);
    //add table to store all current open servers
    Table serversTable = new Table();
    serversTable.setFillParent(true);
    serversTable.defaults().pad(10).width(450);
    serversTable.top().padTop(150);
    listServers = new ListServers(serversTable);
    listServers.update();
    new Thread(() -> {
      while (!Thread.currentThread().isInterrupted()) {
        double nanoTime = System.nanoTime();
        this.logger.trace("Updating servers...");
        listServers.update();
        while (Math.abs(System.nanoTime() - nanoTime) < 1000000000 / 10) {
          try {
            Thread.sleep(0);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }).start();
    dataTable.addActor(serversTable);
  }

  @Override
  public void refresh() {
    serverSelected = false;
    Data.INSTANCE.setManualConfigForServer(false);
  }

  @Override
  public void render(float delta) {
    super.render(delta);
  }
}
