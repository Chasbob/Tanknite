package com.aticatac.client.screens;

import com.aticatac.client.game.GDXGame;
import com.aticatac.client.util.Data;
import com.aticatac.client.util.ListenerFactory;
import com.aticatac.client.util.MenuTable;
import com.aticatac.client.util.Styles;
import com.aticatac.common.model.DBResponse;
import com.aticatac.common.model.DBlogin;
import com.aticatac.common.model.ServerInformation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;

public class PopUp {


  static void createPopUp(boolean startUp) {
    Table popUpRootTable = new Table();
    Styles.INSTANCE.addTableColour(popUpRootTable, new Color(new Color(0f, 0f, 0f, 0.5f)));
    if (Screens.INSTANCE.getCurrentScreen() == MainMenuScreen.class) {
      Screens.INSTANCE.getScreen(MainMenuScreen.class).rootTable.addActor(popUpRootTable);
      Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpRootTable = popUpRootTable;
    } else if (Screens.INSTANCE.getCurrentScreen() == ServerScreen.class) {
      Screens.INSTANCE.getScreen(ServerScreen.class).popUp = popUpRootTable;
      Screens.INSTANCE.getScreen(ServerScreen.class).rootTable.addActor(popUpRootTable);
    } else {
      Screens.INSTANCE.getScreen(GameScreen.class).popUpTable = popUpRootTable;
      Screens.INSTANCE.getScreen(GameScreen.class).rootTable.addActor(popUpRootTable);
      Screens.INSTANCE.getScreen(GameScreen.class).popUpTable.setVisible(false);
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
      } else if (Screens.INSTANCE.getCurrentScreen() == MainMenuScreen.class) {
        createMultiplayerChildren(multiplayerChildren);
        popUpTable.add(multiplayerChildren);
      } else {
        createGameSettings(multiplayerChildren);
        popUpTable.add(multiplayerChildren);
      }
    }
    popUpTable.padTop(20).padBottom(20).padLeft(60).padRight(60);
    popUpRootTable.add(popUpTable);
  }

  private static void createGameSettings(VerticalGroup multiplayerChildren) {
    //create button for resuming
    MenuTable resumeTable = Styles.INSTANCE.createMenuTable(true, false);
    TextButton resumeButton = Styles.INSTANCE.createButton("RESUME");
    resumeButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Screens.INSTANCE.getScreen(GameScreen.class).tractionPopUp = true;
      Screens.INSTANCE.getScreen(GameScreen.class).popUpTable.setVisible(false);
      return false;
    }));
    resumeTable.setButton(resumeButton);
    ListenerFactory.addHoverListener(resumeButton, resumeTable);
    multiplayerChildren.addActor(resumeTable);
    //create button to get settings
    MenuTable settingsTable = Styles.INSTANCE.createMenuTable(false, false);
    TextButton settingsButton = Styles.INSTANCE.createButton("SETTINGS");
    settingsButton.addListener(ListenerFactory.newListenerEvent(() -> {
      multiplayerChildren.clear();
      Settings.createSettings();
      //create back button
      TextButton backButton = Styles.INSTANCE.createButton("Back");
      backButton.addListener(ListenerFactory.newListenerEvent(() -> {
        multiplayerChildren.clear();
        createGameSettings(multiplayerChildren);
        return false;
      }));
      multiplayerChildren.addActor(backButton);
      return false;
    }));
    settingsTable.setButton(settingsButton);
    ListenerFactory.addHoverListener(settingsButton, settingsTable);
    multiplayerChildren.addActor(settingsTable);
    //create button for quitting
    MenuTable quitTable = Styles.INSTANCE.createMenuTable(false, false);
    TextButton quitButton = Styles.INSTANCE.createButton("QUIT");
    quitButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Screens.INSTANCE.showScreen(MainMenuScreen.class);
      Data.INSTANCE.quit();
      Screens.INSTANCE.getScreen(GameScreen.class).refresh();
      return false;
    }));
    quitTable.setButton(quitButton);
    ListenerFactory.addHoverListener(quitButton, quitTable);
    multiplayerChildren.addActor(quitTable);
  }

  private static void createMultiplayerChildren(VerticalGroup multiplayerChildren) {
    MenuTable hostTable = Styles.INSTANCE.createMenuTable(true, false);
    //create button for hosting game
    TextButton hostButton = Styles.INSTANCE.createButton("HOST");
    hostButton.addListener(ListenerFactory.newListenerEvent(() -> {
      host(multiplayerChildren);
      return false;
    }));
    hostTable.setButton(hostButton);
    ListenerFactory.addHoverListener(hostButton, hostTable);
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
    ListenerFactory.addHoverListener(joinButton, joinTable);
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
    buttonGroup.space(10).padTop(10);
    multiplayerChildren.addActor(buttonGroup);
    buttonGroup.addActor(createBackButton(false, multiplayerChildren));
    MenuTable hostTable = Styles.INSTANCE.createMenuTable(false, false);
    TextButton hostButton = Styles.INSTANCE.createButton("HOST");
    hostTable.setButton(hostButton);
    hostButton.addListener(ListenerFactory.newListenerEvent(() -> {
      //create custom server
      GDXGame.createServer(false, serverNameField.getText());
      Data.INSTANCE.setSingleplayer(false);
      //TODO make getter for port and ip
      Data.INSTANCE.setCurrentInformation(new ServerInformation(Data.INSTANCE.getUsername(), InetAddress.getByName("127.0.0.1"), 5500));
      Data.INSTANCE.connect(Data.INSTANCE.getUsername(), false);
      Screens.INSTANCE.getScreen(MainMenuScreen.class).refresh();
      ListenerFactory.newChangeScreenAndReloadEvent(LobbyScreen.class);
      serverNameField.setText("");
      return false;
    }));
    ListenerFactory.addHoverListener(hostButton, hostTable);
    buttonGroup.addActor(hostTable);
  }

  private static void manualJoin(VerticalGroup multiplayerChildren) {
    VerticalGroup bodyGroup = new VerticalGroup();
    bodyGroup.space(10).columnLeft();
    multiplayerChildren.addActor(bodyGroup);
    //create actors
    Label ipLabel = Styles.INSTANCE.createLabel("SERVER IP");
    TextField ipField = Styles.INSTANCE.createTextField("");
    MenuTable joinTable = Styles.INSTANCE.createMenuTable(false, false);
    TextButton joinButton = Styles.INSTANCE.createButton("JOIN");
    joinTable.setButton(joinButton);
    //create listener
    joinButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Data.INSTANCE.connect(Data.INSTANCE.getUsername(), false, ipField.getText());
      ListenerFactory.newChangeScreenAndReloadEvent(LobbyScreen.class);
      ipField.setText("");
      return false;
    }));
    ListenerFactory.addHoverListener(joinButton, joinTable);
    //add actors
    bodyGroup.addActor(ipLabel);
    bodyGroup.addActor(ipField);
    //create horizontal groups to store buttons
    HorizontalGroup buttonGroup = new HorizontalGroup();
    buttonGroup.space(10).padTop(10);
    buttonGroup.addActor(createBackButton(false, multiplayerChildren));
    buttonGroup.addActor(joinTable);
    multiplayerChildren.addActor(buttonGroup);
  }

  private static Group createLogin() {
    VerticalGroup rootGroup = new VerticalGroup();
    rootGroup.pad(10).columnLeft().space(10);
    if (Data.INSTANCE.isConnected()) {
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
      //add main listeners for buttons
      createErrorListener(rootGroup, false, loginButton, usernameText, passwordText);
      //TODO register player and give errors
      createErrorListener(rootGroup, true, registerButton, usernameText, passwordText);
      InputEvent clickEvent = new InputEvent();
      clickEvent.setType(InputEvent.Type.touchDown);
    } else {
      Data.INSTANCE.setUsername("OFFLINE");
      playOffline(rootGroup);
    }
    Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpGroup = rootGroup;
    return rootGroup;
  }

  @NotNull
  public static Boolean resetMainMenu() {
    Screens.INSTANCE.getScreen(MainMenuScreen.class).rootTable.removeActor(Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpRootTable);
    Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpLogin = false;
    Screens.INSTANCE.getScreen(MainMenuScreen.class).loadInMainMenu(Data.INSTANCE.isConnected());
    Screens.INSTANCE.getScreen(MainMenuScreen.class).toggleButtonDeactivation(false);
    return false;
  }

  private static MenuTable createBackButton(boolean mainPopUp, VerticalGroup parentGroup) {
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
    ListenerFactory.addHoverListener(backButton, backTable);
    backTable.setButton(backButton);
    return backTable;
  }

  private static void createErrorListener(VerticalGroup group, boolean register, TextButton button, TextField usernameText, TextField passwordText) {
    button.addListener(ListenerFactory.newListenerEvent(() -> {
      DBResponse response;
      if (register) {
        response = Data.INSTANCE.login(new DBlogin(usernameText.getText(), passwordText.getText(), true));
      } else {
        response = Data.INSTANCE.login(new DBlogin(usernameText.getText(), passwordText.getText(), false));
      }
      //create table to store error message
      Table errorTable = new Table();
      Label errorLabel = Styles.INSTANCE.createCustomLabelWithFont(Styles.INSTANCE.baseFont, "", Color.RED);
      errorTable.add(errorLabel);
      group.addActorAt(1, errorLabel);
      switch (response.getResponse()) {
        case accepted:
          Data.INSTANCE.setUsername(usernameText.getText());
          resetMainMenu();
          break;
        case wrong_password:
          errorLabel.setText("WRONG PASSWORD");
          break;
        case username_taken:
          errorLabel.setText("NAME TAKEN");
          break;
        case no_user:
          errorLabel.setText("NO USER");
          break;
      }
      return false;
    }));
  }

  private static void playOffline(VerticalGroup rootGroup) {
    //clear the group
    rootGroup.clear();
    MenuTable okButtonTable = Styles.INSTANCE.createMenuTable(true, false);
    TextButton okButton = Styles.INSTANCE.createButton("PLAY");
    okButton.addListener(ListenerFactory.newListenerEvent(() -> {
      resetMainMenu();
      return false;
    }));
    okButtonTable.setButton(okButton);
    ListenerFactory.addHoverListener(okButton, okButtonTable);
    rootGroup.addActor(okButtonTable);
    //create actors
    Table labelTable = new Table();
    Label label = Styles.INSTANCE.createCustomLabelWithFont(Styles.INSTANCE.baseFont, "NO DATABASE CONNECTION - OFFLINE MODE", Color.WHITE);
    labelTable.add(label);
    rootGroup.addActor(labelTable);
  }
}
