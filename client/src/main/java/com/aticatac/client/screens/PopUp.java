package com.aticatac.client.screens;

import com.aticatac.client.util.Data;
import com.aticatac.client.util.ListenerFactory;
import com.aticatac.client.util.MenuTable;
import com.aticatac.client.util.Styles;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

class PopUp {

  static void createPopUp() {
    Table popUpRootTable = new Table();
    Styles.INSTANCE.addTableColour(popUpRootTable, Styles.INSTANCE.getTransparentColour());
    Screens.INSTANCE.getScreen(MainMenuScreen.class).rootTable.addActor(popUpRootTable);
    Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpRootTable = popUpRootTable;
    popUpRootTable.setFillParent(true);
    Table popUpTable = new Table();
    Styles.INSTANCE.addTableColour(popUpTable, Color.BLACK);
    popUpTable.add(createMultiplayerChildren());
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

  static MenuTable createBackButton() {
    MenuTable backTable = Styles.INSTANCE.createMenuTable(false, false);
    TextButton backButton = Styles.INSTANCE.createButton("BACK");
    backButton.addListener(ListenerFactory.newListenerEvent(() -> {
      Screens.INSTANCE.getScreen(MainMenuScreen.class).rootTable.removeActor(Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpRootTable);
      Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpPresent = false;
      Screens.INSTANCE.getScreen(MainMenuScreen.class).toggleButtonDeactivation(false);
      return false;
    }));
    backTable.setButton(backButton);
    return backTable;
  }


}
