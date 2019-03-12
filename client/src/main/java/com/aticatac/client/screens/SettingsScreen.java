package com.aticatac.client.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * The type Settings screen.
 */
public class SettingsScreen extends AbstractScreen {
  private boolean sound;
  /**
   * Instantiates a new Settings screen.
   */
  SettingsScreen() {
    super();
    refresh();
  }

  @Override
  public void buildStage() {
    super.buildStage();
    //create table to store setting toggles
    Table toggleTable = new Table();
    super.addToRoot(toggleTable);
    TextButton soundButton = UIFactory.createButton("Toggle Sound: ");
    Label soundLabel = UIFactory.createLabel("ON");
    soundButton.addListener(UIFactory.newListenerEvent(()->{
      if(sound){
        sound = false;
        soundLabel.setText("OFF");
      }else{
        sound = true;
        soundLabel.setText("ON");
      }
      return false;
    }));
    toggleTable.add(soundButton);
    toggleTable.add(soundLabel);
  }

  @Override
  public void refresh() {
    sound = true;
  }
}
