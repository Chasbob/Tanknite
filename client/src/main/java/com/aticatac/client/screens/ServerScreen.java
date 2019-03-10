package com.aticatac.client.screens;

import com.aticatac.client.util.ListServers;
import com.aticatac.client.util.Styles;
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
    //create root table
    Table rootTable = new Table();
    rootTable.setFillParent(true);
    addActor(rootTable);
    //create data table
    Table dataTable = new Table();
    dataTable.setFillParent(true);
    rootTable.addActor(dataTable);
    //create table for top lables
    Table serverDetailsTable = new Table();
    serverDetailsTable.setFillParent(true);
    dataTable.addActor(serverDetailsTable);
    serverDetailsTable.top().padTop(50);
    Label waitingLabel = UIFactory.createLabel("servers");
    serverDetailsTable.add(waitingLabel);
    //add table with join button to get into lobby after entering username
    Table buttonTable = new Table();
    buttonTable.setFillParent(true);
    buttonTable.top().padTop(100);
    TextButton joinButton = UIFactory.createStartButton("Join");
    buttonTable.add(joinButton).padRight(50);
    joinButton.addListener(UIFactory.newListenerEvent(() -> {
      if (Data.INSTANCE.isServerSelected()) {
        //TODO show lobby of currentServer
        currentServer.setStyle(Styles.INSTANCE.getButtonStyle());
        Screens.INSTANCE.showScreen(UsernameScreen.class);
      }
      return false;
    }
    ));
    dataTable.addActor(buttonTable);
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
    //create table to store back button
    Table backTable = new Table();
    backTable.setFillParent(true);
    rootTable.addActor(backTable);
    backTable.bottom();
    TextButton backButton = UIFactory.createBackButton("back");
    backTable.add(backButton).bottom().padBottom(10);
    backButton.addListener(UIFactory.newChangeScreenEvent(MainMenuScreen.class));
    //add labels to serverDetailsTable
    TextButton refreshButton = UIFactory.createStartButton("Refresh");
//    refreshButton.setStyle(Styles.getInstance().getSelectedButtonStyle());
    refreshButton.addListener(UIFactory.newListenerEvent(() -> {
      listServers.update();
      return false;
    }
    ));
    buttonTable.add(refreshButton);
  }

  @Override
  public void refresh() {
    serverSelected = false;
  }

  @Override
  public void render(float delta) {
    super.render(delta);
  }
}
