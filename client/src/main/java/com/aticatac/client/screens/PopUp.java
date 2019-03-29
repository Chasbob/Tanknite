package com.aticatac.client.screens;

import com.aticatac.client.game.GDXGame;
import com.aticatac.client.util.Data;
import com.aticatac.client.util.ListenerFactory;
import com.aticatac.client.util.Styles;
import com.aticatac.common.model.DBResponse;
import com.aticatac.common.model.DBlogin;
import com.aticatac.common.model.ServerInformation;
import com.aticatac.common.model.Updates.Response;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import java.net.InetAddress;
import org.jetbrains.annotations.NotNull;

/**
 * The type Pop up.
 */
class PopUp {
  /**
   * Create pop up.
   *
   * @param startUp     the start up
   * @param endGame     the end game
   * @param multiplayer the multiplayer
   */
  static void createPopUp(boolean startUp, boolean endGame, boolean multiplayer) {
    Table popUpRootTable = new Table();
    Styles.INSTANCE.addTableColour(popUpRootTable, new Color(new Color(0f, 0f, 0f, 0.5f)));
    if (Screens.INSTANCE.getCurrentScreen() == MainMenuScreen.class) {
      Screens.INSTANCE.getScreen(MainMenuScreen.class).rootTable.addActor(popUpRootTable);
      Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpRootTable = popUpRootTable;
    } else if (Screens.INSTANCE.getCurrentScreen() == ServerScreen.class) {
      Screens.INSTANCE.getScreen(ServerScreen.class).popUp = popUpRootTable;
      Screens.INSTANCE.getScreen(ServerScreen.class).rootTable.addActor(popUpRootTable);
    } else {
      if (Data.INSTANCE.isIso()) {
        Screens.INSTANCE.getScreen(GameScreenIsometric.class).popUpTable = popUpRootTable;
        Screens.INSTANCE.getScreen(GameScreenIsometric.class).rootTable.addActor(popUpRootTable);
        Screens.INSTANCE.getScreen(GameScreenIsometric.class).popUpTable.setVisible(false);
      } else {
        Screens.INSTANCE.getScreen(GameScreen.class).popUpTable = popUpRootTable;
        Screens.INSTANCE.getScreen(GameScreen.class).rootTable.addActor(popUpRootTable);
        Screens.INSTANCE.getScreen(GameScreen.class).popUpTable.setVisible(false);
      }
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
      } else if (Screens.INSTANCE.getCurrentScreen() == MainMenuScreen.class) {
        if (Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpChoice) {
          createGameChoice(multiplayerChildren, multiplayer);
        }
      } else if (endGame) {
        endGame(multiplayerChildren);
      } else {
        createGameSettings(multiplayerChildren);
      }
      popUpTable.add(multiplayerChildren);
    }
    popUpTable.padTop(20).padBottom(20).padLeft(60).padRight(60);
    popUpRootTable.add(popUpTable);
  }

  private static void createGameSettings(VerticalGroup multiplayerChildren) {
    //create button for resuming
    Table resumeTable = Styles.INSTANCE.createPopUpTable();
    TextButton resumeButton = Styles.INSTANCE.createButton("RESUME");
    resumeButton.addListener(ListenerFactory.newListenerEvent(() -> {
      if (Data.INSTANCE.isIso()) {
        Screens.INSTANCE.getScreen(GameScreenIsometric.class).tractionPopUp = true;
        Screens.INSTANCE.getScreen(GameScreenIsometric.class).popUpTable.setVisible(false);
      } else {
        Screens.INSTANCE.getScreen(GameScreen.class).tractionPopUp = true;
        Screens.INSTANCE.getScreen(GameScreen.class).popUpTable.setVisible(false);
      }
      return false;
    }));
    resumeTable.add(resumeButton);
    ListenerFactory.addHoverListener(resumeButton, resumeTable);
    multiplayerChildren.addActor(resumeTable);
    //create button to get settings
    Table settingsTable = Styles.INSTANCE.createPopUpTable();
    TextButton settingsButton = Styles.INSTANCE.createButton("SETTINGS");
    settingsButton.addListener(ListenerFactory.newListenerEvent(() -> {
      multiplayerChildren.clear();
      Settings.createSettings();
      //create back button
      Table backTable = Styles.INSTANCE.createPopUpTable();
      TextButton backButton = Styles.INSTANCE.createButton("BACK");
      backButton.addListener(ListenerFactory.newListenerEvent(() -> {
        multiplayerChildren.clear();
        createGameSettings(multiplayerChildren);
        return false;
      }));
      backTable.add(backButton);
      ListenerFactory.addHoverListener(backButton, backTable);
      multiplayerChildren.addActor(backTable);
      return false;
    }));
    settingsTable.add(settingsButton);
    ListenerFactory.addHoverListener(settingsButton, settingsTable);
    multiplayerChildren.addActor(settingsTable);
    //create button for quitting
    Table quitTable = Styles.INSTANCE.createPopUpTable();
    TextButton quitButton = Styles.INSTANCE.createButton("QUIT");
    quitButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Screens.INSTANCE.getScreen(MainMenuScreen.class).refresh();
      Screens.INSTANCE.showScreen(MainMenuScreen.class);
      Data.INSTANCE.quit();
      return false;
    }));
    quitTable.add(quitButton);
    ListenerFactory.addHoverListener(quitButton, quitTable);
    multiplayerChildren.addActor(quitTable);
    multiplayerChildren.pack();
    Screens.INSTANCE.getScreen(GameScreen.class).verticalGroup = multiplayerChildren;
  }

  private static void createGameChoice(VerticalGroup multiplayerChildren, boolean multiplayer) {
    //2D
    Table twoDTable = Styles.INSTANCE.createPopUpTable();
    //create button for playing game in 2d
    TextButton twoDButton = Styles.INSTANCE.createButton("2D");
    twoDButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Data.INSTANCE.setIso(false);
      if (multiplayer) {
        //load multiplayer pop up
        multiplayerChildren.clear();
        createMultiplayerChildren(multiplayerChildren);
      } else {
        //go single player
        Data.INSTANCE.setSingleplayer(true);
        GDXGame.createServer(true, "Single-Player");
        Screens.INSTANCE.getScreen(MainMenuScreen.class).refresh();
        //join single player server
        Data.INSTANCE.connect(Data.INSTANCE.getUsername(), true);
        Screens.INSTANCE.reloadScreen(GameScreen.class);
        Screens.INSTANCE.showScreen(GameScreen.class);
      }
      return false;
    }));
    twoDTable.add(twoDButton);
    ListenerFactory.addHoverListener(twoDButton, twoDTable);
    //ISO
    Table isoTable = Styles.INSTANCE.createPopUpTable();
    //create button for playing game in isometric
    TextButton isoButton = Styles.INSTANCE.createButton("ISOMETRIC");
    isoButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Data.INSTANCE.setIso(true);
      if (multiplayer) {
        //load multiplayer pop up
        multiplayerChildren.clear();
        createMultiplayerChildren(multiplayerChildren);
      } else {
        //go single player
        Data.INSTANCE.setSingleplayer(true);
        GDXGame.createServer(true, "Single-Player");
        Screens.INSTANCE.getScreen(MainMenuScreen.class).refresh();
        //join single player server
        Data.INSTANCE.connect(Data.INSTANCE.getUsername(), true);
        Screens.INSTANCE.reloadScreen(GameScreenIsometric.class);
        Screens.INSTANCE.showScreen(GameScreenIsometric.class);
      }
      return false;
    }));
    isoTable.add(isoButton);
    ListenerFactory.addHoverListener(isoButton, isoTable);
    popUpHelper(twoDTable, isoTable, multiplayerChildren);
  }

  private static void createMultiplayerChildren(VerticalGroup multiplayerChildren) {
    Table hostTable = Styles.INSTANCE.createPopUpTable();
    //create button for hosting game
    TextButton hostButton = Styles.INSTANCE.createButton("HOST");
    hostButton.addListener(ListenerFactory.newListenerEvent(() -> {
      host(multiplayerChildren);
      return false;
    }));
    hostTable.add(hostButton);
    ListenerFactory.addHoverListener(hostButton, hostTable);
    //create button for joining
    Table joinTable = Styles.INSTANCE.createPopUpTable();
    TextButton joinButton = Styles.INSTANCE.createButton("JOIN");
    joinButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Screens.INSTANCE.getScreen(MainMenuScreen.class).refresh();
      Screens.INSTANCE.getScreen(ServerScreen.class).refresh();
      Screens.INSTANCE.showScreen(ServerScreen.class);
      return false;
    }));
    joinTable.add(joinButton);
    ListenerFactory.addHoverListener(joinButton, joinTable);
    popUpHelper(hostTable, joinTable, multiplayerChildren);
  }

  private static void popUpHelper(Table firstTable, Table secondTable, VerticalGroup multiplayerChildren) {
    multiplayerChildren.addActor(firstTable);
    multiplayerChildren.addActor(secondTable);
    multiplayerChildren.addActor(createBackButton(true, multiplayerChildren, false));
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
    buttonGroup.addActor(createBackButton(false, multiplayerChildren, false));
    Table hostTable = Styles.INSTANCE.createPopUpTable();
    TextButton hostButton = Styles.INSTANCE.createButton("HOST");
    hostTable.add(hostButton);
    hostButton.addListener(ListenerFactory.newListenerEvent(() -> {
      if (serverNameField.getText().length() > 0) {
        //create custom server
        GDXGame.createServer(false, serverNameField.getText());
        Data.INSTANCE.setSingleplayer(false);
        //TODO make getter for port and ip
        Data.INSTANCE.setCurrentInformation(new ServerInformation(Data.INSTANCE.getUsername(), InetAddress.getByName("127.0.0.1"), 5000));
        Data.INSTANCE.connect(Data.INSTANCE.getUsername(), false);
        Screens.INSTANCE.getScreen(MainMenuScreen.class).refresh();
        ListenerFactory.newChangeScreenAndReloadEvent(LobbyScreen.class);
        serverNameField.setText("");
      } else {
        //print out an error
        if (bodyGroup.getChildren().size == 2) {
          //TODO do we need to add any other error messages here?
          //if an error message isnt already there
          Label errorLabel = Styles.INSTANCE.createCustomLabel("FIELD CAN'T BE BLANK", Color.RED);
          bodyGroup.addActorAt(0, errorLabel);
        }
      }
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
    Table joinTable = Styles.INSTANCE.createPopUpTable();
    TextButton joinButton = Styles.INSTANCE.createButton("JOIN");
    joinTable.add(joinButton);
    //create listener
    joinButton.addListener(ListenerFactory.newListenerEvent(() -> {
      //TODO catch exception if cant connect to server
      Response response = Data.INSTANCE.connect(Data.INSTANCE.getUsername(), ipField.getText());
      switch (response) {
        case ACCEPTED:
          ListenerFactory.newChangeScreenAndReloadEvent(LobbyScreen.class);
          ipField.setText("");
          break;
        case TAKEN:
        case NO_SERVER:
        case INVALID_NAME:
        case INVALID_RESPONSE:
        case UNKNOWN_HOST:
        case FULL:
          if (bodyGroup.getChildren().size == 2) {
            //create error label
            Label errorLabel = Styles.INSTANCE.createCustomLabel("CAN'T CONNECT TO SERVER", Color.RED);
            bodyGroup.addActorAt(0, errorLabel);
          }
          break;
      }
      return false;
    }));
    ListenerFactory.addHoverListener(joinButton, joinTable);
    //add actors
    bodyGroup.addActor(ipLabel);
    bodyGroup.addActor(ipField);
    //create horizontal groups to store buttons
    HorizontalGroup buttonGroup = new HorizontalGroup();
    buttonGroup.space(10).padTop(10);
    buttonGroup.addActor(createBackButton(false, multiplayerChildren, false));
    buttonGroup.addActor(joinTable);
    multiplayerChildren.addActor(buttonGroup);
  }

  private static Group createLogin() {
    VerticalGroup rootGroup = new VerticalGroup();
    rootGroup.pad(10).columnLeft().space(10);
    if (Data.INSTANCE.isDbConnected()) {
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
      //tabs
      HorizontalGroup tabGroup = new HorizontalGroup();
      tabGroup.space(5);
      rootGroup.addActor(tabGroup);
      //create tab buttons in menu tables
      Table loginTable = Styles.INSTANCE.createPopUpTable();
      Table registerTable = Styles.INSTANCE.createPopUpTable();
      //create buttons to store in these tables
      TextButton loginButton = Styles.INSTANCE.createButton("LOGIN");
      TextButton registerButton = Styles.INSTANCE.createButton("REGISTER");
      loginTable.add(loginButton);
      registerTable.add(registerButton);
      //add hover listeners for both buttons
      ListenerFactory.addHoverListener(loginButton, loginTable);
      ListenerFactory.addHoverListener(registerButton, registerTable);
      tabGroup.addActor(loginTable);
      tabGroup.addActor(registerTable);
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

  private static void createErrorListener(VerticalGroup group, boolean register, TextButton button, TextField usernameText, TextField passwordText) {
    button.addListener(ListenerFactory.newListenerEvent(() -> {
      //create table to store error message
      Label errorLabel;
      if (group.getChildren().size == 5) {
        //error label not present yet
        errorLabel = Styles.INSTANCE.createCustomLabelWithFont(Styles.INSTANCE.baseFont, "", Color.RED);
        group.addActorAt(0, errorLabel);
      } else {
        //error label already showing
        errorLabel = (Label) group.getChildren().get(0);
      }
      //get username and password
      String username = usernameText.getText();
      String password = passwordText.getText();
      if (username.length() > 0 && password.length() > 0) {
        DBResponse response;
        if (register) {
          response = Data.INSTANCE.login(new DBlogin(usernameText.getText(), passwordText.getText(), true));
        } else {
          response = Data.INSTANCE.login(new DBlogin(usernameText.getText(), passwordText.getText(), false));
        }
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
      } else {
        errorLabel.setText("FIELD CAN'T BE EMPTY.");
      }
      return false;
    }));
  }

  private static void playOffline(VerticalGroup rootGroup) {
    //clear the group
    rootGroup.clear();
    rootGroup.columnCenter();
    Table okButtonTable = Styles.INSTANCE.createPopUpTable();
    TextButton okButton = Styles.INSTANCE.createButton("PLAY");
    okButton.addListener(ListenerFactory.newListenerEvent(() -> {
      resetMainMenu();
      return false;
    }));
    okButtonTable.add(okButton);
    ListenerFactory.addHoverListener(okButton, okButtonTable);
    //create actors
    Table labelTable = new Table();
    Label label = Styles.INSTANCE.createCustomLabelWithFont(Styles.INSTANCE.baseFont, "NO DATABASE CONNECTION - OFFLINE MODE", Color.GRAY);
    labelTable.add(label);
    rootGroup.addActor(labelTable);
    rootGroup.addActor(okButtonTable);
  }

  private static void endGame(VerticalGroup multiplayerChildren) {
    VerticalGroup rootGroup = new VerticalGroup();
    //create result of game
    Table resultTable = new Table();
    rootGroup.addActor(resultTable);
    Label resultLabel;
    if (!Data.INSTANCE.isConnectedToGame()) {
      resultLabel = Styles.INSTANCE.createCustomLabelWithFont(Styles.INSTANCE.bigMenuFont, "LOST CONNECTION", Color.GREEN);
    } else if (Data.INSTANCE.isWon()) {
      resultLabel = Styles.INSTANCE.createCustomLabelWithFont(Styles.INSTANCE.bigMenuFont, "VICTORY", Color.GREEN);
    } else {
      resultLabel = Styles.INSTANCE.createCustomLabelWithFont(Styles.INSTANCE.bigMenuFont, "DEFEAT", Color.RED);
    }
    resultTable.add(resultLabel);
    //create quit button
    rootGroup.addActor(createBackButton(true, rootGroup, true));
    multiplayerChildren.addActor(rootGroup);
  }

  @NotNull
  private static Boolean resetMainMenu() {
    Screens.INSTANCE.getScreen(MainMenuScreen.class).rootTable.removeActor(Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpRootTable);
    Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpLogin = false;
    Screens.INSTANCE.getScreen(MainMenuScreen.class).loadInMainMenu(Data.INSTANCE.isDbConnected());
    Screens.INSTANCE.getScreen(MainMenuScreen.class).toggleButtonDeactivation(false);
    return false;
  }

  private static Table createBackButton(boolean mainPopUp, VerticalGroup parentGroup, boolean gameover) {
    Table backTable = Styles.INSTANCE.createPopUpTable();
    TextButton backButton;
    if (gameover) {
      backButton = Styles.INSTANCE.createButton("QUIT");
    } else {
      backButton = Styles.INSTANCE.createButton("BACK");
    }
    backButton.addListener(ListenerFactory.newListenerEvent(() -> {
      if (mainPopUp) {
        Screens.INSTANCE.getScreen(MainMenuScreen.class).rootTable.removeActor(Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpRootTable);
        Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpMultiplayer = false;
        Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpChoice = false;
        Screens.INSTANCE.getScreen(MainMenuScreen.class).toggleButtonDeactivation(false);
        if (gameover) {
          Screens.INSTANCE.showScreen(MainMenuScreen.class);
        }
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
    backTable.add(backButton);
    return backTable;
  }
}
