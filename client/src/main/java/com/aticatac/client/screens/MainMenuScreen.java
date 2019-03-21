package com.aticatac.client.screens;

import com.aticatac.client.util.Data;
import com.aticatac.client.util.ListenerFactory;
import com.aticatac.client.util.MenuTable;
import com.aticatac.client.util.Styles;
import com.aticatac.server.networking.Server;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * The type Main menu screen.
 */
public class MainMenuScreen extends AbstractScreen {

  HorizontalGroup horizontalGroup;
  Table soundTable;
  Table musicTable;
  private int tabIndex;
  private int dropDownIndex;
  private TextButton exitButton;
  Table visibleTable;
  private MenuTable playTable;
  private MenuTable achievementTable;
  private MenuTable customizeTable;
  MenuTable settingsTable;
  private MenuTable exitTable;

  /**
   * Instantiates a new Main menu screen.
   */
  MainMenuScreen() {
    super();
    Gdx.input.setInputProcessor(this);
    this.dropDownIndex = -1;
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
      switchTab(true);
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
      switchTab(false);
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
      enterPressed();
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
      switchDropDown(true);
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
      switchDropDown(false);
    }
  }

  @Override
  public void buildStage() {
    //create root table
    rootTable = new Table();
    rootTable.setFillParent(true);
    Styles.INSTANCE.addTableColour(rootTable, Color.valueOf("363636"));
    addActor(rootTable);
    //create title table
    Table titleTable = new Table().top().left().padTop(30).padLeft(50);
    super.addToRoot(titleTable);
    Label screenTitle = Styles.INSTANCE.createTitleLabel();
    titleTable.setFillParent(true);
    titleTable.add(screenTitle);
    //create table to store all buttons
    Table buttonTable = new Table().top().left().padLeft(50).padTop(150).padRight(50);
    super.addToRoot(buttonTable);
    //create horizontal group to store top row buttons
    horizontalGroup
      = new HorizontalGroup();
    horizontalGroup
      .space(5);
    buttonTable.add(horizontalGroup
    );
    //table that stores info related to current button
    visibleTable = new Table();
    visibleTable.defaults().left();
    //play button with child buttons
    createPlay();
    //achievements
    achievementTable = createMenuTable(false, true);
    TextButton achievementButton = Styles.INSTANCE.createButton("ACHIEVEMENTS");
    switchTabListener(achievementButton, achievementTable, horizontalGroup);
    //customize
    customizeTable = createMenuTable(false, true);
    TextButton customizeButton = Styles.INSTANCE.createButton("CUSTOMIZE");
    switchTabListener(customizeButton, customizeTable, horizontalGroup);
    //settings with child buttons
    createSettings();
    //create button to close game
    exitTable = createMenuTable(false, true);
    exitButton = Styles.INSTANCE.createBackButton("QUIT");
    exitTable.add(exitButton);
    horizontalGroup
      .addActor(exitTable);
    exitButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Gdx.app.exit();
      return false;
    }));
    horizontalGroup
      .pack();
    tabIndex = 0;
    buttonTable.row();
    buttonTable.add(visibleTable).left().padTop(20);
    visibleTable.add(playTable.getGroup());
  }

  private void createPlay() {
    playTable = createMenuTable(true, true);
    TextButton playButton = Styles.INSTANCE.createButton("PLAY");
    switchTabListener(playButton, playTable, horizontalGroup);
    createPlayChildren();
  }

  private void createPlayChildren() {
    VerticalGroup playChildren = Styles.INSTANCE.createVerticalGroup();
    //create button for single player
    MenuTable singlePlayerTable = createMenuTable(false, false);
    TextButton singlePlayerButton = Styles.INSTANCE.createButton("SINGLE-PLAYER");
    singlePlayerButton.addListener(ListenerFactory.newListenerEvent(() -> {
      switchDropDownMouse(singlePlayerTable);
//      Data.INSTANCE.setSingleplayer(true);
//      Server server = new Server(true, "Single-Player");
//      server.start();
//      refresh();
//      ListenerFactory.newChangeScreenAndReloadEvent(UsernameScreen.class);
      return false;
    }));
    singlePlayerTable.add(singlePlayerButton);
    //create button for multi player
    MenuTable multiPlayerTable = createMenuTable(false, false);
    TextButton multiPlayerButton = Styles.INSTANCE.createButton("MULTI-PLAYER");
    multiPlayerButton.addListener(ListenerFactory.newListenerEvent(() -> {
      switchDropDownMouse(multiPlayerTable);
      return false;
    }));
    multiPlayerTable.add(multiPlayerButton);
    playChildren.addActor(singlePlayerTable);
    playChildren.addActor(multiPlayerTable);
    playChildren.pack();
    playTable.setGroup(playChildren);
  }

//  private void createMultiplayerChildren() {
//    multiplayerTable = new Table();
//    multiplayerTable.defaults().padRight(20);
//    //create button for hosting game
//    TextButton hostButton = Styles.INSTANCE.createLessPopButton("HOST");
//    hostButton.padLeft(270);
//    hostButton.addListener(ListenerFactory.newListenerEvent(() -> {
//      Data.INSTANCE.setHosting(true);
//      //reload username screen and show
//      refresh();
//      ListenerFactory.newChangeScreenAndReloadEvent(UsernameScreen.class);
//      return false;
//    }));
//    multiplayerTable.add(hostButton);
//    //create button for joining
//    TextButton joinButton = Styles.INSTANCE.createLessPopButton("JOIN");
//    joinButton.addListener(ListenerFactory.newListenerEvent(() -> {
//      refresh();
//      Screens.INSTANCE.getScreen(ServerScreen.class).refresh();
//      Screens.INSTANCE.showScreen(ServerScreen.class);
//      return false;
//    }));
//    multiplayerTable.add(joinButton);
//    horizontalGroup
//      .addActorAt(2, multiplayerTable);
//    horizontalGroup
//      .pack();
//  }

  private void createSettings() {
    settingsTable = createMenuTable(false, true);
    TextButton settingsButton = Styles.INSTANCE.createButton("SETTINGS");
    switchTabListener(settingsButton, settingsTable, horizontalGroup);
    Settings.createSettings();
  }

  MenuTable createMenuTable(boolean selected, boolean tab) {
    MenuTable table = new MenuTable(tab);
    if (selected) {
      table.setShowGroup(true);
    } else {
      table.setShowGroup(false);
    }
    table.defaults().padTop(5).padBottom(5).padLeft(10).padRight(10);
    return table;
  }

  private void switchTab(boolean right) {
    //deselect previous table
    MenuTable previousTable = (MenuTable) horizontalGroup.getChildren().get(tabIndex);
    //get current vertical group and make current button unselected
    for (Actor table : previousTable.getGroup().getChildren()) {
      MenuTable menuTable = (MenuTable) table;
      if (menuTable.isShowGroup()) {
        menuTable.setShowGroup(false);
        dropDownIndex = -1;
        break;
      }
    }
    //remove tables vertical group from visible table
    visibleTable.clear();
    previousTable.setShowGroup(false);
    //select new table
    MenuTable newTable;
    if (right) {
      tabIndex++;
    } else {
      tabIndex--;
    }
    try {
      newTable = (MenuTable) horizontalGroup.getChildren().get(tabIndex);
    } catch (IndexOutOfBoundsException e) {
      if (right) {
        tabIndex = 0;
      } else {
        tabIndex = horizontalGroup.getChildren().size - 1;
      }
      newTable = (MenuTable) horizontalGroup.getChildren().get(tabIndex);
    }
    //add tables vertical group to visible table
    visibleTable.add(newTable.getGroup());
    newTable.setShowGroup(true);
  }

  private void switchTabMouse(MenuTable newTable) {
    //deselect previous table
    MenuTable previousTable = (MenuTable) horizontalGroup.getChildren().get(tabIndex);
    //remove tables vertical group from visible table
    visibleTable.clear();
    previousTable.setShowGroup(false);
    //select new table
    tabIndex = horizontalGroup.getChildren().indexOf(newTable, true);
    //add new tables vertical group to visible table
    visibleTable.add(newTable.getGroup());
    newTable.setShowGroup(true);
  }

  private void switchTabListener(TextButton button, MenuTable parentTable, Group parentGroup) {
    parentTable.add(button);
    parentGroup
      .addActor(parentTable);
    button.addListener(ListenerFactory.newListenerEvent(() -> {
      if (!parentTable.isShowGroup()) {
        switchTabMouse(parentTable);
      }
      return false;
    }));
  }

  private void switchDropDown(boolean down) {
    //get current drop down from tab
    MenuTable currentTab = (MenuTable) horizontalGroup.getChildren().get(tabIndex);
    VerticalGroup verticalGroup = currentTab.getGroup();
    if (verticalGroup.getChildren().size != 0) {
      //get current table from group
      MenuTable newTable = null;
      try {
        MenuTable currentTable = (MenuTable) verticalGroup.getChildren().get(dropDownIndex);
        currentTable.setShowGroup(false);
        if (down) {
          dropDownIndex++;
        } else {
          dropDownIndex--;
        }
        try {
          newTable = (MenuTable) verticalGroup.getChildren().get(dropDownIndex);
        } catch (IndexOutOfBoundsException e) {
          dropDownIndex = -1;
        }
      } catch (IndexOutOfBoundsException e) {
        if (down) {
          dropDownIndex++;
        } else {
          dropDownIndex = verticalGroup.getChildren().size - 1;
        }
        newTable = (MenuTable) verticalGroup.getChildren().get(dropDownIndex);
      }
      if (newTable != null) {
        newTable.setShowGroup(true);
      }
    }
  }

  void switchDropDownMouse(MenuTable newTable) {
    //get current vertical group for tab
    MenuTable currentTab = (MenuTable) horizontalGroup.getChildren().get(tabIndex);
    VerticalGroup verticalGroup = currentTab.getGroup();
    for (Actor a : verticalGroup.getChildren()) {
      MenuTable menuTable = (MenuTable) a;
      if (menuTable.isShowGroup()) {
        menuTable.setShowGroup(false);
      }
    }
    //select new table
    dropDownIndex = verticalGroup.getChildren().indexOf(newTable, true);
    newTable.setShowGroup(true);
  }

  private void enterPressed() {
    //want to close game
    if (tabIndex == horizontalGroup.getChildren().size - 1) {
      InputEvent event1 = new InputEvent();
      event1.setType(InputEvent.Type.touchDown);
      exitButton.fire(event1);
    }
  }

  @Override
  public void refresh() {
    this.dropDownIndex = -1;
  }

}
