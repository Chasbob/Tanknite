package com.aticatac.client.screens;

import com.aticatac.client.util.*;
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
  Boolean popUpPresent;
  private ListServers listServers;
  private int tabIndex;
  public int dropDownIndex;
  private HorizontalGroup tabGroup;
  public VerticalGroup dropDownGroup;
  Table popUp;

  /**
   * Instantiates a new Server screen.
   */
  ServerScreen() {
    super();
    serverSelected = false;
    popUpPresent = false;
    Data.INSTANCE.setManualConfigForServer(false);
    tabIndex = 0;
    dropDownIndex = 0;
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
          //join the server selected
          Data.INSTANCE.connect(Data.INSTANCE.getUsername(), false);
          ListenerFactory.newChangeScreenAndReloadEvent(LobbyScreen.class);
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
      popUpPresent = true;
      toggleButtons(true);
      PopUp.createPopUp(false);
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
    unHighlight();
    tabIndex = 0;
    dropDownIndex = 0;
    //rehighlight first server
    MenuTable menuTable = (MenuTable) dropDownGroup.getChildren().get(dropDownIndex);
    menuTable.setShowGroup(true);
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    //need to poll for input
    if (!popUpPresent) {
      if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
        tabIndex = switcher(true, tabGroup, tabIndex, false);
      } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
        tabIndex = switcher(false, tabGroup, tabIndex, false);
      } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
        dropDownIndex = switcher(true, dropDownGroup, dropDownIndex, true);
      } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
        dropDownIndex = switcher(false, dropDownGroup, dropDownIndex, true);
      } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
        enterPressed();
      }
    }
  }

  private int switcher(boolean rightOrDown, Group group, int index, boolean dropDown) {
    //deselect current table
    MenuTable currentTable = (MenuTable) group.getChildren().get(index);
    if (dropDown) {
      unHighlight();
    } else {
      currentTable.setShowGroup(false);
    }
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
        unHighlight();
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

  public void unHighlight() {
    //get current menu table from drop down
    MenuTable currentTable = (MenuTable) dropDownGroup.getChildren().get(dropDownIndex);
    currentTable.setShowGroup(false);
    currentTable.getLabel().setStyle(Styles.INSTANCE.createLabelStyle(Styles.INSTANCE.italicFont, Color.GRAY));
    currentTable.getButton().setStyle(Styles.INSTANCE.createButtonStyle(Styles.INSTANCE.baseFont, Color.GRAY));
  }

  void removePopUp() {
    rootTable.removeActor(popUp);
    popUp.clear();
  }

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
      //get the current table
      MenuTable currentTable = (MenuTable) children.get(i);
      if (disable) {
        currentTable.getButton().setTouchable(Touchable.disabled);
      } else {
        currentTable.getButton().setTouchable(Touchable.enabled);
      }
    }
  }
}
