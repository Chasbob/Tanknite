package com.aticatac.client.screens;

import com.aticatac.client.util.Data;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * The type Multiplayer screen.
 */
public class MultiplayerScreen extends AbstractScreen {
  /**
   * Instantiates a new Multiplayer screen.
   */
  MultiplayerScreen() {
    super();
    refresh();
  }

  @Override
  public void buildStage() {
    super.buildStage();
    //create table to store host/join buttons
    Table buttonTable = new Table();
    super.addToRoot(buttonTable);
    buttonTable.defaults().pad(10).width(100).center();
    //create button for hosting game
    TextButton hostButton = UIFactory.createButton("Host");
    hostButton.addListener(UIFactory.newListenerEvent(() -> {
      Data.INSTANCE.setHosting(true);
      //reload username screen and show
      UIFactory.newChangeScreenAndReloadEvent(UsernameScreen.class);
      return false;
    }));
    buttonTable.add(hostButton);
    //create button for joining
    TextButton joinButton = UIFactory.createButton("Join");
    joinButton.addListener(UIFactory.newListenerEvent(()->{
      Screens.INSTANCE.getScreen(ServerScreen.class).refresh();
      Screens.INSTANCE.showScreen(ServerScreen.class);
      return false;
    }));
    buttonTable.add(joinButton);
  }

  @Override
  public void refresh() {
    Data.INSTANCE.setHosting(false);
  }

}
