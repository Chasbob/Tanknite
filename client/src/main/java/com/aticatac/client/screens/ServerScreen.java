package com.aticatac.client.screens;

import com.aticatac.client.util.*;
import com.aticatac.common.model.Updates.Response;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.SnapshotArray;


/**
 * The type Server screen.
 */
public class ServerScreen extends AbstractScreen {

  private Boolean serverSelected;
  /**
   * The Pop up present.
   */
  Boolean popUpPresent;
  private PopulateServers populateServers;
  private HorizontalGroup tabGroup;
  private VerticalGroup dropDownGroup;
  /**
   * The Pop up.
   */
  Table popUp;

  /**
   * Instantiates a new Server screen.
   */
  ServerScreen() {
    super();
    serverSelected = false;
    popUpPresent = false;
    Data.INSTANCE.setManualConfigForServer(false);
    Gdx.input.setInputProcessor(this);
  }

  @Override
  public void buildStage() {
    super.buildStage();
    //create data table
    Table dataTable = new Table();
    super.addToRoot(dataTable);
    //add error label
    Label errorLabel = Styles.INSTANCE.createCustomLabel("", Color.RED);
    dataTable.add(errorLabel).pad(10);
    dataTable.row();
    //add horizontal group to store both buttons
    tabGroup = new HorizontalGroup();
    dataTable.add(tabGroup);
    tabGroup.space(5);
    dataTable.addActor(tabGroup);
    //create table to store join button in
    Table joinTable = Styles.INSTANCE.createPopUpTable();
    //create join button
    TextButton joinButton = Styles.INSTANCE.createButton("JOIN");
    joinTable.add(joinButton);
    tabGroup.addActor(joinTable);
    joinButton.addListener(ListenerFactory.newListenerEvent(() -> {
      if (serverSelected) {
        //join the server selected
        Response response = Data.INSTANCE.connect(Data.INSTANCE.getUsername(), false);
        switch (response) {
          case ACCEPTED:
            ListenerFactory.newChangeScreenAndReloadEvent(LobbyScreen.class);
            break;
          case TAKEN:
          case NO_SERVER:
          case INVALID_NAME:
          case UNKNOWN_HOST:
          case INVALID_RESPONSE:
          case FULL:
            errorLabel.setText("CAN'T CONNECT TO SERVER");
            break;
        }
      }
      return false;
      }
    ));
    ListenerFactory.addHoverListener(joinButton, joinTable);
    //creat table to store manual button in
    Table manualTable = Styles.INSTANCE.createPopUpTable();
    TextButton manualButton = Styles.INSTANCE.createButton("MANUAL");
    manualTable.add(manualButton);
    manualButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Data.INSTANCE.setManualConfigForServer(true);
      popUpPresent = true;
      toggleButtons(true);
      PopUp.createPopUp(false, false, false);
      return false;
    }));
    ListenerFactory.addHoverListener(manualButton, manualTable);
    tabGroup.addActor(manualTable);
    dataTable.row();
    //create a table to contain all groups for displaying servers
    Table serversContainer = new Table();
    Styles.INSTANCE.addTableColour(serversContainer, Color.DARK_GRAY);
    dataTable.add(serversContainer);
    //add vertical group to store all servers and player count in them
    dropDownGroup = new VerticalGroup();
    dropDownGroup.space(5).columnLeft();
    serversContainer.add(dropDownGroup).left().pad(10);
    populateServers = new PopulateServers(dropDownGroup);
    populateServers.update();
    new Thread(() -> {
      while (!Thread.currentThread().isInterrupted()) {
        double nanoTime = System.nanoTime();
        this.logger.trace("Updating servers...");
        populateServers.update();
        while (Math.abs(System.nanoTime() - nanoTime) < 1000000000 / 10) {
          try {
            Thread.sleep(0);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }).start();
  }

  @Override
  public void refresh() {
    serverSelected = false;
    Data.INSTANCE.setManualConfigForServer(false);
    unHighlight();
  }

  /**
   * Un highlight.
   */
  public void unHighlight() {
    //unhighlight previous server
    for (int i = 0; i < dropDownGroup.getChildren().size; i++) {
      Table table = (Table) dropDownGroup.getChildren().get(i);
      Styles.INSTANCE.addTableColour(table, Styles.INSTANCE.getTransparentColour());
    }
  }

  /**
   * Remove pop up.
   */
  void removePopUp() {
    rootTable.removeActor(popUp);
    popUp.clear();
  }

  /**
   * Toggle buttons.
   *
   * @param disable the disable
   */
  void toggleButtons(boolean disable) {
    //deactivate tabs
    toggleButtonsHelper(disable, tabGroup.getChildren());
    toggleButtonsHelper(disable, dropDownGroup.getChildren());
    if (disable) {
      super.backButton.setTouchable(Touchable.disabled);
    } else {
      super.backButton.setTouchable(Touchable.enabled);
    }
  }

  private void toggleButtonsHelper(boolean disable, SnapshotArray<Actor> children) {
    for (int i = 0; i < children.size; i++) {
//      //get the current table
//      Table currentTable =  children.get(i);
//      if (disable) {
//        currentTable.getButton().setTouchable(Touchable.disabled);
//      } else {
//        currentTable.getButton().setTouchable(Touchable.enabled);
//      }
    }
  }

  /**
   * Sets server selected.
   *
   * @param serverSelected the server selected
   */
  public void setServerSelected(Boolean serverSelected) {
    this.serverSelected = serverSelected;
  }
}
