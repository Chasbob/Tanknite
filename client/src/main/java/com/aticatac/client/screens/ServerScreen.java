package com.aticatac.client.screens;

import com.aticatac.client.util.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;


/**
 * The type Server screen.
 */
public class ServerScreen extends AbstractScreen {

  private Boolean serverSelected;
  private ListServers listServers;
  private int tabIndex;
  public int dropDownIndex;
  private HorizontalGroup tabGroup;
  private VerticalGroup dropDownGroup;

  /**
   * Instantiates a new Server screen.
   */
  ServerScreen() {
    super();
    refresh();
    Gdx.input.setInputProcessor(this);
  }

  public void setServerSelected(Boolean serverSelected) {
    this.serverSelected = serverSelected;
  }

  @Override
  public void buildStage() {
    super.buildStage();
    //create data table
    Table dataTable = new Table();
    super.addToRoot(dataTable);
    //add horizontal group to store both buttons
    tabGroup = new HorizontalGroup();
    dataTable.add(tabGroup).padBottom(10);
    tabGroup.space(5);
    dataTable.addActor(tabGroup);
    //create table to store join button in
    MenuTable joinTable = Styles.INSTANCE.createMenuTable(true, true);
    //create join button
    TextButton joinButton = Styles.INSTANCE.createButton("JOIN");
    joinTable.setButton(joinButton);
    tabGroup.addActor(joinTable);
    joinButton.addListener(ListenerFactory.newListenerEvent(() -> {
        if (serverSelected) {
          ListenerFactory.newChangeScreenAndReloadEvent(UsernameScreen.class);
        }
        return false;
      }
    ));
    ListenerFactory.addHoverListener(joinButton, joinTable);
    //creat table to store manual button in
    MenuTable manualTable = Styles.INSTANCE.createMenuTable(false, true);
    TextButton manualButton = Styles.INSTANCE.createButton("MANUAL");
    manualTable.setButton(manualButton);
    manualButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Data.INSTANCE.setManualConfigForServer(true);
      ListenerFactory.newChangeScreenAndReloadEvent(UsernameScreen.class);
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
    dropDownGroup.space(5);
    serversContainer.add(dropDownGroup).left().pad(10);
    listServers = new ListServers(dropDownGroup);
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
  }

  @Override
  public void refresh() {
    serverSelected = false;
    Data.INSTANCE.setManualConfigForServer(false);
    tabIndex = 0;
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    //need to poll for input
    if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
      tabIndex = switchTab(true, tabGroup, tabIndex, false);
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
      tabIndex = switchTab(false, tabGroup, tabIndex, false);
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
      dropDownIndex = switchTab(true, dropDownGroup, dropDownIndex, true);
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
      dropDownIndex = switchTab(false, dropDownGroup, dropDownIndex, true);
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
      enterPressed();
    }
  }

  private int switchTab(boolean rightOrDown, Group group, int index, boolean dropDown) {
    //deselect current tab
    MenuTable currentTable = (MenuTable) group.getChildren().get(index);
    currentTable.setShowGroup(false);
    if (rightOrDown) {
      if (index == group.getChildren().size - 1) {
        index = 0;
      } else {
        index++;
      }
    } else {
      if (index == 0) {
        index = group.getChildren().size - 1;
      } else {
        index--;
      }
    }
    //get new table and select
    MenuTable newTable = (MenuTable) group.getChildren().get(index);
    newTable.setShowGroup(true);
    if (dropDown) {
      ServerButton currentButton = (ServerButton) currentTable.getButton();
      if (!currentButton.getLabel().getText().toString().equals("<EMPTY>")) {
        //make old button and label text grey
        currentButton.setStyle(Styles.INSTANCE.createButtonStyle(Styles.INSTANCE.baseFont, Color.GRAY));
        currentButton.getLabel().setStyle(Styles.INSTANCE.createLabelStyle(Styles.INSTANCE.italicFont, Color.GRAY));
      }
      ServerButton newButton = (ServerButton) newTable.getButton();
      if (!newButton.getLabel().getText().toString().equals("<EMPTY>")) {
        //if we are moving drop down make button text white
        newTable.getButton().setStyle(Styles.INSTANCE.createButtonStyle(Styles.INSTANCE.baseFont, Color.WHITE));
        newTable.getLabel().setStyle(Styles.INSTANCE.createLabelStyle(Styles.INSTANCE.italicFont, Color.LIME));
      }
    }
    return index;
  }

  private void enterPressed() {
    //set up input event
    InputEvent inputEvent = new InputEvent();
    inputEvent.setType(InputEvent.Type.touchDown);
    //get current tab
    MenuTable currentTab = (MenuTable) tabGroup.getChildren().get(tabIndex);
    //get current drop down element
    MenuTable currentDrop = (MenuTable) dropDownGroup.getChildren().get(dropDownIndex);
    ServerButton currentButton = (ServerButton) currentDrop.getButton();
    if (currentButton.getText().toString().equals("JOIN") && !currentButton.getLabel().getText().toString().equals("<EMPTY>")) {
      setServerSelected(true);
      Data.INSTANCE.setCurrentInformation(currentButton.getServerInformation());
    }
    //fire the current tab
    currentTab.getButton().fire(inputEvent);
  }

}
