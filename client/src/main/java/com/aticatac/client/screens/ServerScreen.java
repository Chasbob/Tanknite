package com.aticatac.client.screens;

import com.aticatac.client.util.Styles;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * The type Server screen.
 */
public class ServerScreen extends AbstractScreen {
  private Boolean serverSelected;
  private TextButton currentServer;

  /**
   * Instantiates a new Server screen.
   */
  ServerScreen() {
    super();
    serverSelected = false;
    //TODO dont use null
    currentServer = null;
  }

  /**
   * Gets current server.
   *
   * @return the current server
   */
  public TextButton getCurrentServer() {
    return currentServer;
  }

  /**
   * Sets current server.
   *
   * @param currentServer the current server
   */
  public void setCurrentServer(TextButton currentServer) {
    this.currentServer = currentServer;
  }

  /**
   * Gets server selected.
   *
   * @return the server selected
   */
  public Boolean getServerSelected() {
    return serverSelected;
  }

  /**
   * Sets server selected.
   *
   * @param serverSelected the server selected
   */
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
      if (serverSelected) {
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
    UIFactory.getServers(serversTable);
    dataTable.addActor(serversTable);
    //create table to store back button
    Table backTable = new Table();
    backTable.setFillParent(true);
    rootTable.addActor(backTable);
    backTable.bottom();
    TextButton backButton = UIFactory.createBackButton("back");
    backTable.add(backButton).bottom().padBottom(10);
    backButton.addListener(UIFactory.newChangeScreenEvent(MainMenuScreen.class));
//        //create refresh button table
//        Table refreshButtonTable = new Table();
//        serverDetailsTable.add(refreshButtonTable);
//        //refreshButtonTable.debug();
//        //add refresh button
//        ImageButton refreshButton = Styles.getInstance().getRefreshButton();
//        refreshButtonTable.add(refreshButton).padLeft(30);
//        refreshButton.addListener(UIFactory.newListenerEvent(()->{
//                    UIFactory.getServers(serversTable);
//                    return false;
//                }
//        ));
//        //add label table
//        Table labelTable = new Table();
//        serverDetailsTable.addActor(labelTable);
//        labelTable.center();
    //add labels to serverDetailsTable
    TextButton refreshButton = UIFactory.createStartButton("Refresh");
    //refreshButton.setStyle(Styles.getInstance().getSelectedButtonStyle());
    refreshButton.addListener(UIFactory.newListenerEvent(() -> {
      UIFactory.getServers(serversTable);
      return false;
    }
    ));
    buttonTable.add(refreshButton);
  }

  @Override
  public void render(float delta) {
    super.render(delta);
  }
}
