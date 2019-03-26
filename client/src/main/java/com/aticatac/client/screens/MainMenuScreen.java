package com.aticatac.client.screens;

import com.aticatac.client.util.Data;
import com.aticatac.client.util.ListenerFactory;
import com.aticatac.client.util.MenuTable;
import com.aticatac.client.util.Styles;
import com.aticatac.common.model.Updates.Response;
import com.aticatac.server.networking.Server;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * The type Main menu screen.
 */
public class MainMenuScreen extends AbstractScreen {

  private HorizontalGroup horizontalGroup;
  Table soundTable;
  Table musicTable;
  private int tabIndex;
  private int dropDownIndex;
  private int popUpIndex;
  private Table dropDownTable;
  private MenuTable playTable;
  MenuTable settingsTable;
  VerticalGroup popUpGroup;
  Table popUpRootTable;
  public boolean popUpMultiplayer;
  public boolean popUpLogin;
  private Table containerTable;
  private Label screenTitle;
  private float green;
  private boolean colorDirection;
  private SpriteBatch aidBarBatch;

  /**
   * Instantiates a new Main menu screen.
   */
  MainMenuScreen() {
    super();
    Gdx.input.setInputProcessor(this);
    popUpMultiplayer = false;
    green = 0.5f;
    aidBarBatch = new SpriteBatch();
  }

  @Override
  public void render(float delta) {
    //TODO batch is not being drawn under aid label
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    if (!popUpMultiplayer && !popUpLogin) {
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
    } else {
      if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
        rootTable.removeActor(popUpRootTable);
        popUpMultiplayer = false;
        toggleButtonDeactivation(false);
      } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
        keySwitcher(popUpGroup, popUpIndex, false);
      } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
        keySwitcher(popUpGroup, popUpIndex, true);
      } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
        enterPressed();
      } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
        keySwitcher(popUpGroup, popUpIndex, true);
      } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
        keySwitcher(popUpGroup, popUpIndex, false);
      }
    }
    //setting colour dynamic for title label
    if (green < 1 && colorDirection) {
      //if there is room to go up and we are going up then go up
      green = green + 0.01f;
    } else if (green < 1 && !colorDirection) {
      if (green > 0.5f) {
        //we can decrease
        green = green - 0.01f;
      } else {
        //we cant decrease and need to go up again
        colorDirection = true;
      }
    } else if (green > 1 && !colorDirection) {
      //go down
      green = green - 0.1f;
    } else {
      //need to go back down
      colorDirection = false;
    }
    screenTitle.setStyle(Styles.INSTANCE.createLabelStyle(Styles.INSTANCE.titleFont, new Color(0.973f, green, 0.475f, 1)));
    //draw aid bar
    aidBarBatch.begin();
    aidBarBatch.setColor(Color.DARK_GRAY);
    aidBarBatch.draw(Styles.INSTANCE.getBlank(), 0, 0, Gdx.graphics.getWidth(), 30);
    aidBarBatch.setColor(Color.WHITE);
    aidBarBatch.end();
    act(delta);
    draw();
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
    screenTitle = Styles.INSTANCE.createTitleLabel();
    titleTable.add(screenTitle);
    super.addToRoot(titleTable);
    popUpLogin = true;
    PopUp.createPopUp(true);
  }

  private void createAidTable() {
    Table aidTable = new Table();
    super.addToRoot(aidTable);
    Label aidLabel = Styles.INSTANCE.createCustomLabelWithFont(Styles.INSTANCE.smallFont, "[ARROW KEYS TO NAVIGATE - ENTER TO CLICK BUTTON - MOUSE AS ALTERNATIVE]", Color.GRAY);
    aidTable.add(aidLabel).padBottom(5);
    aidTable.bottom();
  }

  private void createNavigationMenus() {
    //create table to store all buttons
    containerTable = new Table().top().left().padLeft(50).padTop(150).padRight(50);
    super.addToRoot(containerTable);
    //create horizontal group to store top row buttons
    horizontalGroup
      = new HorizontalGroup();
    horizontalGroup
      .space(5);
    containerTable.add(horizontalGroup
    );
    //table that stores info related to current button
    dropDownTable = new Table();
    dropDownTable.defaults().left();
    //play button with child buttons
    createPlay();
    //challenges
    createChallenges();
    //customize
    createCustomize();
    //settings with child buttons
    createSettings();
    //create button to close game
    MenuTable exitTable = Styles.INSTANCE.createMenuTable(false, true);
    TextButton exitButton = Styles.INSTANCE.createBackButton("QUIT");
    exitTable.setButton(exitButton);
    horizontalGroup
      .addActor(exitTable);
    exitButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Gdx.app.exit();
      return false;
    }));
    horizontalGroup
      .pack();
    ListenerFactory.addHoverListener(exitButton, exitTable);
    tabIndex = 0;
    containerTable.row();
    containerTable.add(dropDownTable).left().padTop(20);
    dropDownTable.add(playTable.getGroup());
  }

  private void createCustomize() {
    MenuTable customizeTable = Styles.INSTANCE.createMenuTable(false, true);
    TextButton customizeButton = Styles.INSTANCE.createButton("CUSTOMIZE");
    switchTabListener(customizeButton, customizeTable, horizontalGroup);
    ListenerFactory.addHoverListener(customizeButton, customizeTable);
    //create table to store buttons
    Table customizeDataTable = createTableWithPadding();
    customizeTable.setTable(customizeDataTable);
    //create button
    TextButton yellowButton = Styles.INSTANCE.createItalicButton("YELLOW (test)");
    yellowButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Data.INSTANCE.setTankColour(Color.YELLOW);
      return false;
    }));
    customizeDataTable.add(yellowButton).padBottom(10);
  }

  private void createChallenges() {
    MenuTable challengesButtonTable = Styles.INSTANCE.createMenuTable(false, true);
    TextButton challengesButton = Styles.INSTANCE.createButton("CHALLENGES");
    switchTabListener(challengesButton, challengesButtonTable, horizontalGroup);
    ListenerFactory.addHoverListener(challengesButton, challengesButtonTable);
    //create challenge table
    Table challengesDataTable = createTableWithPadding();
    //add table to store header
    Table headerTable = new Table();
    //TODO label not appearing properly.
    headerTable.add(Styles.INSTANCE.createCustomLabel("DAILY CHALLENGES", Color.GRAY));
    //create table to store challenges
    Table challengesTable = new Table();
    challengesTable.defaults().left().padTop(10);
    challengesTable.add(Styles.INSTANCE.createItalicLabel("Kill 3 enemies"));
    challengesTable.row();
    challengesTable.add(Styles.INSTANCE.createItalicLabel("Obtain a power up and kill someone whilst its activated"));
    challengesTable.row();
    challengesTable.add(Styles.INSTANCE.createItalicLabel("Win a game."));
    //add tables together
    challengesDataTable.add(headerTable);
    challengesDataTable.row();
    challengesDataTable.add(challengesTable).padBottom(10);
    //add table to the menu table
    challengesButtonTable.setTable(challengesDataTable);
  }

  private Table createTableWithPadding() {
    Table table = new Table();
    Styles.INSTANCE.addTableColour(table, Color.DARK_GRAY);
    table.defaults().left().padLeft(10).padRight(10).top().padTop(10);
    return table;
  }

  private void createAssets() {
    Table assetsTable = new Table();
    assetsTable.top().right().pad(10);
    assetsTable.defaults().left();
    assetsTable.add(Styles.INSTANCE.createLabel("ID: "));
    assetsTable.add(Styles.INSTANCE.createCustomLabel(Data.INSTANCE.getUsername(), Color.CORAL)).padRight(10);
    assetsTable.add(Styles.INSTANCE.createLabel("XP: "));
    assetsTable.add(Styles.INSTANCE.createCustomLabel("1500", Color.CORAL));
    super.addToRoot(assetsTable);
  }

  private void createLockedTable() {
    Table lockedTable = createTableWithPadding();
    //stats container table
    Table statsContainerTable = new Table();
    statsContainerTable.defaults().left();
    //create section header for stats
    statsContainerTable.add(Styles.INSTANCE.createCustomLabel("STATS", Color.GRAY)).padTop(10).padBottom(10);
    //populate stats
    statsContainerTable.row();
    populateStats(statsContainerTable);
    //create section header for top players
    Table leaderboardContainerTable = new Table();
    leaderboardContainerTable.defaults().left();
    leaderboardContainerTable.add(Styles.INSTANCE.createCustomLabel("TOP PLAYERS", Color.GRAY)).padTop(10).padBottom(10);
    //populate leaderboard
    leaderboardContainerTable.row();
    populateTopPlayers(leaderboardContainerTable);
    //add tables to locked table
    lockedTable.add(statsContainerTable);
    lockedTable.add(leaderboardContainerTable);
    containerTable.add(lockedTable).left().padTop(50);
  }

  private void populateTopPlayers(Table leaderboardContainerTable) {
    //TODO use database values
    Table rankingTable = new Table();
    rankingTable.defaults().left();
    rankingTable.add(Styles.INSTANCE.createCustomLabel("1. ", Color.CORAL));
    rankingTable.add(Styles.INSTANCE.createItalicLabel("dylanAI - 6000"));
    rankingTable.row();
    rankingTable.add(Styles.INSTANCE.createCustomLabel("2. ", Color.CORAL));
    rankingTable.add(Styles.INSTANCE.createItalicLabel("charlie27 - 5687"));
    rankingTable.row();
    rankingTable.add(Styles.INSTANCE.createCustomLabel("3. ", Color.CORAL));
    rankingTable.add(Styles.INSTANCE.createItalicLabel("claireISSICK - 5000"));
    rankingTable.row();
    rankingTable.add(Styles.INSTANCE.createCustomLabel("4. ", Color.CORAL));
    rankingTable.add(Styles.INSTANCE.createItalicLabel("bob - 1235"));
    rankingTable.row();
    rankingTable.add(Styles.INSTANCE.createCustomLabel("5. ", Color.CORAL));
    rankingTable.add(Styles.INSTANCE.createItalicLabel("3wanisshit - 0"));
    leaderboardContainerTable.add(rankingTable).padBottom(10);
  }

  private void populateStats(Table statsContainerTable) {
    Table statsTable = new Table();
    statsTable.defaults().left().padRight(20).top();
    //we want to seperate the stats as too many to just list so have left table
    Table statsLeftTable = new Table();
    statsLeftTable.defaults().left();
    statsLeftTable.add(Styles.INSTANCE.createItalicLabel("TOP KILLSTREAK: "));
    statsLeftTable.row();
    statsLeftTable.add(Styles.INSTANCE.createItalicLabel("KILLS: "));
    statsLeftTable.row();
    statsLeftTable.add(Styles.INSTANCE.createItalicLabel("DEATHS: "));
    statsLeftTable.row();
    statsLeftTable.add(Styles.INSTANCE.createItalicLabel("K/D: "));
    statsLeftTable.row();
    //create right table
    Table statsRightTable = new Table();
    statsRightTable.defaults().left();
    statsLeftTable.add(Styles.INSTANCE.createItalicLabel("WINS: "));
    //statsRightTable.row();
    statsRightTable.add(Styles.INSTANCE.createItalicLabel("LOSSES: "));
    statsRightTable.row();
    statsRightTable.add((Styles.INSTANCE.createItalicLabel("W/L: ")));
    statsRightTable.row();
    statsRightTable.add((Styles.INSTANCE.createItalicLabel("RANKING: ")));
    statsTable.add(statsLeftTable);
    statsTable.add(statsRightTable);
    statsContainerTable.add(statsTable).padBottom(10);
  }

  private void createPlay() {
    playTable = Styles.INSTANCE.createMenuTable(true, true);
    TextButton playButton = Styles.INSTANCE.createButton("PLAY");
    switchTabListener(playButton, playTable, horizontalGroup);
    ListenerFactory.addHoverListener(playButton, playTable);
    createPlayChildren();
  }

  private void createPlayChildren() {
    VerticalGroup playChildren = Styles.INSTANCE.createVerticalGroup();
    //create button for single player
    MenuTable singlePlayerTable = Styles.INSTANCE.createMenuTable(true, false);
    TextButton singlePlayerButton = Styles.INSTANCE.createItalicButton("SINGLE-PLAYER");
    singlePlayerButton.addListener(ListenerFactory.newListenerEvent(() -> {
      switchDropDownMouse(singlePlayerTable);
      Data.INSTANCE.setSingleplayer(true);
      Server server = new Server(true, "Single-Player");
      server.start();
      refresh();
      //ListenerFactory.newChangeScreenAndReloadEvent(UsernameScreen.class);
      //join single player server
      Data.INSTANCE.connect(Data.INSTANCE.getUsername(), true);
      Screens.INSTANCE.showScreen(GameScreen.class);
      return false;
    }));
    singlePlayerTable.setButton(singlePlayerButton);
    //create button for multi player
    MenuTable multiPlayerTable = Styles.INSTANCE.createMenuTable(false, false);
    TextButton multiPlayerButton = Styles.INSTANCE.createItalicButton("MULTI-PLAYER");
    multiPlayerButton.addListener(ListenerFactory.newListenerEvent(() -> {
      switchDropDownMouse(multiPlayerTable);
      popUpMultiplayer = true;
      PopUp.createPopUp(false);
      toggleButtonDeactivation(true);
      return false;
    }));
    multiPlayerTable.setButton(multiPlayerButton);
    playChildren.addActor(singlePlayerTable);
    playChildren.addActor(multiPlayerTable);
    playChildren.pack();
    playTable.setGroup(playChildren);
  }

  private void createSettings() {
    settingsTable = Styles.INSTANCE.createMenuTable(false, true);
    TextButton settingsButton = Styles.INSTANCE.createButton("SETTINGS");
    switchTabListener(settingsButton, settingsTable, horizontalGroup);
    ListenerFactory.addHoverListener(settingsButton, settingsTable);
    Settings.createSettings();
  }


  private void switchTab(boolean right) {
    deselectTab();
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
    selectTab(newTable);
  }

  private void deselectTab() {
    //deselect previous table
    MenuTable previousTable = (MenuTable) horizontalGroup.getChildren().get(tabIndex);
    //get current vertical group and make current button unselected
    if (previousTable.getGroup() != null) {
      for (Actor table : previousTable.getGroup().getChildren()) {
        MenuTable menuTable = (MenuTable) table;
        if (menuTable.isShowGroup()) {
          menuTable.setShowGroup(false);
          dropDownIndex = 0;
          break;
        }
      }
    }
    //remove tables group/groups from visible tables
    dropDownTable.clear();
    previousTable.setShowGroup(false);
  }

  private void switchTabMouse(MenuTable newTable) {
    deselectTab();
    //select new table
    tabIndex = horizontalGroup.getChildren().indexOf(newTable, true);
    //add new tables vertical group to visible table
    selectTab(newTable);
  }

  private void selectTab(MenuTable newTable) {
    if (newTable.getGroup() == null) {
      //we know to add a table instead
      dropDownTable.add(newTable.getTable());
    } else {
      dropDownTable.add(newTable.getGroup());
    }
    newTable.setShowGroup(true);
    setDropDown(newTable);
  }

  private void switchTabListener(TextButton button, MenuTable parentTable, Group parentGroup) {
    parentTable.setButton(button);
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
    if (currentTab.getGroup() != null) {
      Group group = currentTab.getGroup();
      if (group.getChildren().size != 0) {
        //get current table from group
        keySwitcher(group, dropDownIndex, down);
      }
    }
  }

  void switchDropDownMouse(MenuTable newTable) {
    //get current vertical group for tab
    MenuTable currentTab = (MenuTable) horizontalGroup.getChildren().get(tabIndex);
    Group group = currentTab.getGroup();
    for (Actor a : group.getChildren()) {
      MenuTable menuTable = (MenuTable) a;
      if (menuTable.isShowGroup()) {
        menuTable.setShowGroup(false);
      }
    }
    //select new table
    dropDownIndex = group.getChildren().indexOf(newTable, true);
    newTable.setShowGroup(true);
  }

  private void enterPressed() {
    InputEvent inputEvent = new InputEvent();
    inputEvent.setType(InputEvent.Type.touchDown);
    if (popUpMultiplayer) {
      //get current menu table and fire button
      MenuTable menuTable = (MenuTable) popUpGroup.getChildren().get(popUpIndex);
      menuTable.getButton().fire(inputEvent);
    } else if (popUpLogin) {
      //get horizontal group
      HorizontalGroup horizontalGroup = (HorizontalGroup) popUpGroup.getChildren().get(0);
      MenuTable menuTable = (MenuTable) horizontalGroup.getChildren().get(popUpIndex);
      menuTable.getButton().fire(inputEvent);
    } else {
      //get current highlighted menu table
      for (int j = 0; j < horizontalGroup.getChildren().size; j++) {
        MenuTable menuTable = (MenuTable) horizontalGroup.getChildren().get(j);
        TextButton currentButton = (TextButton) menuTable.getButton();
        if (menuTable.isShowGroup() && !currentButton.getText().toString().equals("QUIT")) {
          //get the vertical group belonging to the drop down
          Group group = menuTable.getGroup();
          //get current highlighted menu table from drop down
          for (int i = 0; i < group.getChildren().size; i++) {
            MenuTable menuTable1 = (MenuTable) group.getChildren().get(i);
            if (menuTable1.isShowGroup()) {
              menuTable1.getButton().fire(inputEvent);
              break;
            }
          }
        } else if (menuTable.isShowGroup() && currentButton.getText().toString().equals("QUIT")) {
          menuTable.getButton().fire(inputEvent);
        }
      }
    }
  }

  void toggleButtonDeactivation(boolean toggle) {
    for (int j = 0; j < horizontalGroup.getChildren().size; j++) {
      MenuTable menuTable = (MenuTable) horizontalGroup.getChildren().get(j);
      if (toggle) {
        menuTable.getButton().setTouchable(Touchable.disabled);
      } else {
        menuTable.getButton().setTouchable(Touchable.enabled);
      }
      //if there are buttons in drop down also toggle
      if (menuTable.getGroup() != null) {
        for (int i = 0; i < menuTable.getGroup().getChildren().size; i++) {
          MenuTable dropDownTable = (MenuTable) menuTable.getGroup().getChildren().get(i);
          if (dropDownTable.getButton() != null) {
            if (toggle) {
              dropDownTable.getButton().setTouchable(Touchable.disabled);
            } else {
              dropDownTable.getButton().setTouchable(Touchable.enabled);
            }
          }
        }
      }
    }
  }

  private void keySwitcher(Group group, int index, boolean downOrRight) {
    if (popUpLogin) {
      group = (Group) group.getChildren().get(0);
    }
    MenuTable menuTable = (MenuTable) group.getChildren().get(index);
    menuTable.setShowGroup(false);
    index = super.applyIndex(group, index, downOrRight);
    if (popUpMultiplayer || popUpLogin) {
      popUpIndex = index;
    } else {
      dropDownIndex = index;
    }
    //highlight new menu table
    MenuTable newMenuTable = (MenuTable) group.getChildren().get(index);
    newMenuTable.setShowGroup(true);
  }

  //assigns first drop down element of the new tab highlighted
  private void setDropDown(MenuTable newTable) {
    if (newTable.getGroup() != null) {
      if (newTable.getGroup().getChildren().size != 0) {
        MenuTable menuTable = (MenuTable) newTable.getGroup().getChildren().get(dropDownIndex);
        menuTable.setShowGroup(true);
      }
    }
  }

  void loadInMainMenu() {
    //create points and username table
    createAssets();
    //create top half of screen
    createNavigationMenus();
    //create table to store lower part of screen info
    containerTable.row();
    createLockedTable();
    //create label for aid at bottom of screen
    createAidTable();
  }

  @Override
  public void refresh() {
    //unhighlight the drop down if we come back from server screen
    VerticalGroup currentDropDown = (VerticalGroup) dropDownTable.getChildren().get(0);
    MenuTable menuTable = (MenuTable) currentDropDown.getChildren().get(dropDownIndex);
    menuTable.setShowGroup(false);
    //set variables back to normal
    popUpMultiplayer = false;
    toggleButtonDeactivation(false);
    popUpIndex = 0;
    dropDownIndex = 0;
    tabIndex = 0;
    rootTable.removeActor(popUpRootTable);
    //highlight singleplayer as first on drop down
    MenuTable newDropDown = (MenuTable) currentDropDown.getChildren().get(dropDownIndex);
    newDropDown.setShowGroup(true);
  }

  @Override
  public void dispose() {
    super.dispose();
    aidBarBatch.dispose();
  }
}
