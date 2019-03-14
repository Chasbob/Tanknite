package com.aticatac.client.screens;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * The type Settings screen.
 */
public class SettingsScreen extends AbstractScreen {

  private boolean sound;
  private boolean music;
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
    toggleTable.defaults().pad(10).center();
    TextButton soundButton = UIFactory.createButton("Toggle Sound: ");
    Label soundLabel = UIFactory.createSubtleLabel("ON");
    soundButton.addListener(buttonToChangeBool(soundButton, soundLabel));
    toggleTable.add(soundButton);
    toggleTable.add(soundLabel);
    toggleTable.row();
    TextButton musicButton = UIFactory.createButton("Toggle Music: ");
    Label musicLabel = UIFactory.createSubtleLabel("ON");
    musicButton.addListener(buttonToChangeBool(musicButton, musicLabel));
    toggleTable.add(musicButton);
    toggleTable.add(musicLabel);
  }

  private InputListener buttonToChangeBool(TextButton button, Label label){
    return UIFactory.newListenerEvent(()->{
      if (button.getText().equals("Toggle Sound: ")){
        if(sound){
          setSound(false);
          label.setText("ON");
        }else{
          setSound(true);
          label.setText("OFF");
        }
      }else{
        if(music){
          setMusic(false);
          label.setText("ON");
        }else{
          setMusic(true);
          label.setText("OFF");
        }
      }
      return false;
    });
  }

  @Override
  public void refresh() {
    sound = true;
    music = true;
  }

  private void setMusic(boolean music) {
    this.music = music;
  }

  private void setSound(boolean sound) {
    this.sound = sound;
  }
}
