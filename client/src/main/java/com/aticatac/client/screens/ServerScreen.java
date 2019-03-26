package com.aticatac.client.screens;
import com.aticatac.client.util.Data;
import com.aticatac.client.util.ListServers;
import com.aticatac.client.util.ListenerFactory;
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
    super.buildStage();
    //create data table
    Table dataTable = new Table();
    //create table for top labels
    Table serverDetailsTable = super.createTopLabelTable(dataTable);
    Label waitingLabel = Styles.INSTANCE.createLabel("SERVERS");
    serverDetailsTable.add(waitingLabel);
    //add table with join button to get into lobby after entering username
    Table buttonTable = new Table();
    buttonTable.setFillParent(true);
    dataTable.addActor(buttonTable);
    buttonTable.top().padTop(100);
    TextButton joinButton = Styles.INSTANCE.createStartButton("JOIN");
    buttonTable.defaults().padLeft(25).padRight(25);
    buttonTable.add(joinButton);
    joinButton.addListener(ListenerFactory.newListenerEvent(() -> {
        if (serverSelected) {
          ListenerFactory.newChangeScreenAndReloadEvent(UsernameScreen.class);
        }
        return false;
      }
    ));
    TextButton manualButton = Styles.INSTANCE.createStartButton("MANUAL");
    manualButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Data.INSTANCE.setManualConfigForServer(true);
      ListenerFactory.newChangeScreenAndReloadEvent(UsernameScreen.class);
      return false;
    }));
    buttonTable.add(manualButton);
    //add table to store all current open servers
    Table serversTable = new Table();
    serversTable.defaults().pad(5).width(400).padLeft(100);
    serversTable.top().padTop(50);
    Table playersTable = new Table();
    playersTable.defaults().pad(5);
    playersTable.top().padTop(50);
    listServers = new ListServers(serversTable, playersTable);
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
    dataTable.add(serversTable);
    dataTable.add(playersTable);
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
