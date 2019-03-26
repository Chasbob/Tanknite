package com.aticatac.client.screens;

import com.aticatac.client.util.Data;
import com.aticatac.client.util.ListenerFactory;
import com.aticatac.client.util.MenuTable;
import com.aticatac.client.util.Styles;
import com.aticatac.common.model.ServerInformation;
import com.aticatac.server.networking.Server;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;

class PopUp {

  static void createPopUp(boolean startUp) {
    Table popUpRootTable = new Table();
    Styles.INSTANCE.addTableColour(popUpRootTable, new Color(new Color(0f, 0f, 0f, 0.5f)));
    if (Screens.INSTANCE.getCurrentScreen() == MainMenuScreen.class) {
      Screens.INSTANCE.getScreen(MainMenuScreen.class).rootTable.addActor(popUpRootTable);
      Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpRootTable = popUpRootTable;
    } else {
      Screens.INSTANCE.getScreen(ServerScreen.class).popUp = popUpRootTable;
      Screens.INSTANCE.getScreen(ServerScreen.class).rootTable.addActor(popUpRootTable);
    }
    popUpRootTable.setFillParent(true);
    Table popUpTable = new Table();
    Styles.INSTANCE.addTableColour(popUpTable, Color.BLACK);
    if (startUp) {
      popUpTable.add(createLogin());
    } else {
      VerticalGroup multiplayerChildren = new VerticalGroup();
      multiplayerChildren.space(5);
      if (Screens.INSTANCE.getCurrentScreen() == ServerScreen.class) {
        manualJoin(multiplayerChildren);
        popUpTable.add(multiplayerChildren);
      } else {
        createMultiplayerChildren(multiplayerChildren);
        popUpTable.add(multiplayerChildren);
      }
    }
    popUpTable.padTop(20).padBottom(20).padLeft(60).padRight(60);
    popUpRootTable.add(popUpTable);
  }

  static void createMultiplayerChildren(VerticalGroup multiplayerChildren) {
    MenuTable hostTable = Styles.INSTANCE.createMenuTable(true, false);
    //create button for hosting game
    TextButton hostButton = Styles.INSTANCE.createButton("HOST");
    hostButton.addListener(ListenerFactory.newListenerEvent(() -> {
      host(multiplayerChildren);
      return false;
    }));
    hostTable.setButton(hostButton);
    multiplayerChildren.addActor(hostTable);
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
    multiplayerChildren.addActor(joinTable);
    //create button for going back
    multiplayerChildren.addActor(createBackButton(true, multiplayerChildren));
    multiplayerChildren.pack();
    Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpGroup = multiplayerChildren;
  }

  private static void host(VerticalGroup multiplayerChildren) {
    Data.INSTANCE.setHosting(true);
    //clear the pop up
    multiplayerChildren.clear();
    VerticalGroup bodyGroup = new VerticalGroup();
    bodyGroup.space(10).columnLeft();
    multiplayerChildren.addActor(bodyGroup);
    //create actors
    Label serverNameLabel = Styles.INSTANCE.createLabel("SERVER NAME");
    TextField serverNameField = Styles.INSTANCE.createTextField("");
    //add actors
    bodyGroup.addActor(serverNameLabel);
    bodyGroup.addActor(serverNameField);
    //new horizontal group to store host and back
    HorizontalGroup buttonGroup = new HorizontalGroup();
    buttonGroup.align(Align.left);
    buttonGroup.space(5);
    multiplayerChildren.addActor(buttonGroup);
    buttonGroup.addActor(createBackButton(false, multiplayerChildren));
    TextButton startButton = Styles.INSTANCE.createButton("HOST");
    startButton.addListener(ListenerFactory.newListenerEvent(() -> {
      //create custom server
      Server server = new Server(false, serverNameField.getText());
      server.start();
      Data.INSTANCE.setSingleplayer(false);
      //TODO make getter for port and ip
      Data.INSTANCE.setCurrentInformation(new ServerInformation(Data.INSTANCE.getUsername(), InetAddress.getByName("127.0.0.1"), 5500, Server.ServerData.INSTANCE.getMaxPlayers(), Server.ServerData.INSTANCE.playerCount()));
      Data.INSTANCE.connect(Data.INSTANCE.getUsername(), false);
      Screens.INSTANCE.getScreen(MainMenuScreen.class).refresh();
      ListenerFactory.newChangeScreenAndReloadEvent(LobbyScreen.class);
      serverNameField.setText("");
      return false;
    }));
    buttonGroup.addActor(startButton);
  }

  private static void manualJoin(VerticalGroup multiplayerChildren) {
    //create actors
    Label ipLabel = Styles.INSTANCE.createLabel("SERVER IP");
    TextField ipField = Styles.INSTANCE.createTextField("");
    TextButton joinButton = Styles.INSTANCE.createButton("JOIN");
    //create listener
    joinButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Data.INSTANCE.connect(Data.INSTANCE.getUsername(), false, ipField.getText());
      ListenerFactory.newChangeScreenAndReloadEvent(LobbyScreen.class);
      ipField.setText("");
      return false;
    }));
    //add actors
    multiplayerChildren.addActor(ipLabel);
    multiplayerChildren.addActor(ipField);
    //create horizontal groups to store buttons
    HorizontalGroup buttonGroup = new HorizontalGroup();
    buttonGroup.space(10);
    buttonGroup.addActor(createBackButton(false, multiplayerChildren));
    buttonGroup.addActor(joinButton);
    multiplayerChildren.addActor(buttonGroup);
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
      return resetMainMenu();
    }));
    //TODO register player and give errors
    registerButton.addListener(ListenerFactory.newListenerEvent(PopUp::resetMainMenu));
    InputEvent clickEvent = new InputEvent();
    clickEvent.setType(InputEvent.Type.touchDown);
    return rootGroup;
  }

  @NotNull
  private static Boolean resetMainMenu() {
    Screens.INSTANCE.getScreen(MainMenuScreen.class).rootTable.removeActor(Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpRootTable);
    Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpLogin = false;
    Screens.INSTANCE.getScreen(MainMenuScreen.class).loadInMainMenu();
    Screens.INSTANCE.getScreen(MainMenuScreen.class).toggleButtonDeactivation(false);
    return false;
  }

  static MenuTable createBackButton(boolean mainPopUp, VerticalGroup parentGroup) {
    MenuTable backTable = Styles.INSTANCE.createMenuTable(false, false);
    TextButton backButton = Styles.INSTANCE.createButton("BACK");
    backButton.addListener(ListenerFactory.newListenerEvent(() -> {
      if (mainPopUp) {
        Screens.INSTANCE.getScreen(MainMenuScreen.class).rootTable.removeActor(Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpRootTable);
        Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpMultiplayer = false;
        Screens.INSTANCE.getScreen(MainMenuScreen.class).toggleButtonDeactivation(false);
      } else if (Screens.INSTANCE.getCurrentScreen() == ServerScreen.class) {
        //we want to remove pop up
        Screens.INSTANCE.getScreen(ServerScreen.class).removePopUp();
        Screens.INSTANCE.getScreen(ServerScreen.class).popUpPresent = false;
        Screens.INSTANCE.getScreen(ServerScreen.class).toggleButtons(false);
      } else {
        //we just want to go back to the previous pop up
        parentGroup.clear();
        createMultiplayerChildren(parentGroup);
      }
      return false;
    }));
    backTable.setButton(backButton);
    return backTable;
  }

}
