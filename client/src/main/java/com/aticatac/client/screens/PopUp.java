package com.aticatac.client.screens;

import com.aticatac.client.util.Data;
import com.aticatac.client.util.ListenerFactory;
import com.aticatac.client.util.MenuTable;
import com.aticatac.client.util.Styles;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;

class PopUp {

  static void createPopUp(boolean startUp) {
    Table popUpRootTable = new Table();
    Styles.INSTANCE.addTableColour(popUpRootTable, new Color(new Color(0f, 0f, 0f, 0.5f)));
    Screens.INSTANCE.getScreen(MainMenuScreen.class).rootTable.addActor(popUpRootTable);
    Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpRootTable = popUpRootTable;
    popUpRootTable.setFillParent(true);
    Table popUpTable = new Table();
    Styles.INSTANCE.addTableColour(popUpTable, Color.BLACK);
    if (startUp) {
      popUpTable.add(createLogin());
    } else {
      popUpTable.add(createMultiplayerChildren());
    }
    popUpTable.padTop(20).padBottom(20).padLeft(60).padRight(60);
    popUpRootTable.add(popUpTable);
  }

  static Group createMultiplayerChildren() {
    VerticalGroup multplayerChildren = new VerticalGroup();
    multplayerChildren.space(5);
    MenuTable hostTable = Styles.INSTANCE.createMenuTable(true, false);
    //create button for hosting game
    TextButton hostButton = Styles.INSTANCE.createButton("HOST");
    hostButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Data.INSTANCE.setHosting(true);
      //reload username screen and show
      ListenerFactory.newChangeScreenAndReloadEvent(UsernameScreen.class);
      return false;
    }));
    hostTable.setButton(hostButton);
    multplayerChildren.addActor(hostTable);
    //create button for joining
    MenuTable joinTable = Styles.INSTANCE.createMenuTable(false, false);
    TextButton joinButton = Styles.INSTANCE.createButton("JOIN");
    joinButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Screens.INSTANCE.getScreen(MainMenuScreen.class).refresh();
      Screens.INSTANCE.getScreen(ServerScreen.class).refresh();
      Screens.INSTANCE.showScreen(ServerScreen.class);
      return false;
    }));
    joinTable.setButton(joinButton);
    multplayerChildren.addActor(joinTable);
    //create button for going back
    multplayerChildren.addActor(createBackButton());
    multplayerChildren.pack();
    Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpGroup = multplayerChildren;
    return multplayerChildren;
  }

  static Group createLogin() {
    //create group container
    VerticalGroup rootGroup = new VerticalGroup();
    rootGroup.pad(10).columnLeft().space(10);
    //tabs
    HorizontalGroup tabGroup = new HorizontalGroup();
    tabGroup.space(5);
    rootGroup.addActor(tabGroup);
    //create tab buttons in menu tables
    MenuTable loginTable = Styles.INSTANCE.createMenuTable(true, false);
    MenuTable registerTable = Styles.INSTANCE.createMenuTable(false, false);
    //create buttons to store in these tables
    TextButton loginButton = Styles.INSTANCE.createButton("LOGIN");
    TextButton registerButton = Styles.INSTANCE.createButton("REGISTER");
    loginTable.setButton(loginButton);
    registerTable.setButton(registerButton);
    //add hover listeners for both buttons
    ListenerFactory.addHoverListener(loginButton, loginTable);
    ListenerFactory.addHoverListener(registerButton, registerTable);
    tabGroup.addActor(loginTable);
    tabGroup.addActor(registerTable);
    //create actors for table
    Label usernameLabel = Styles.INSTANCE.createLabel("USERNAME");
    Label passwordLabel = Styles.INSTANCE.createLabel("PASSWORD");
    TextField usernameText = Styles.INSTANCE.createTextField("");
    TextField passwordText = Styles.INSTANCE.createTextField("");
    passwordText.setPasswordMode(true);
    //add to tables
    rootGroup.addActor(usernameLabel);
    rootGroup.addActor(usernameText);
    rootGroup.addActor(passwordLabel);
    rootGroup.addActor(passwordText);
    rootGroup.pack();
    Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpGroup = rootGroup;
    //add main listeners for buttons
    loginButton.addListener(ListenerFactory.newListenerEvent(() -> {
      //TODO sign in the player and give errors
      Data.INSTANCE.setUsername(usernameText.getText());
      Screens.INSTANCE.getScreen(MainMenuScreen.class).loadInMainMenu();
      Screens.INSTANCE.getScreen(MainMenuScreen.class).rootTable.removeActor(Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpRootTable);
      Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpLogin = false;
      Screens.INSTANCE.getScreen(MainMenuScreen.class).toggleButtonDeactivation(false);
      return false;
    }));
    registerButton.addListener(ListenerFactory.newListenerEvent(() -> {
      //TODO register player and give errors
      Screens.INSTANCE.getScreen(MainMenuScreen.class).loadInMainMenu();
      Screens.INSTANCE.getScreen(MainMenuScreen.class).rootTable.removeActor(Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpRootTable);
      Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpLogin = false;
      Screens.INSTANCE.getScreen(MainMenuScreen.class).toggleButtonDeactivation(false);
      return false;
    }));
    InputEvent clickEvent = new InputEvent();
    clickEvent.setType(InputEvent.Type.touchDown);
    return rootGroup;
  }

  static MenuTable createBackButton() {
    MenuTable backTable = Styles.INSTANCE.createMenuTable(false, false);
    TextButton backButton = Styles.INSTANCE.createButton("BACK");
    backButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Screens.INSTANCE.getScreen(MainMenuScreen.class).rootTable.removeActor(Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpRootTable);
      Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpMultiplayer = false;
      Screens.INSTANCE.getScreen(MainMenuScreen.class).toggleButtonDeactivation(false);
      return false;
    }));
    backTable.setButton(backButton);
    return backTable;
  }

}
