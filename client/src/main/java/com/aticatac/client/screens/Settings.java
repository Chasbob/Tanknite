package com.aticatac.client.screens;

import com.aticatac.client.util.AudioEnum;
import com.aticatac.client.util.ListenerFactory;
import com.aticatac.client.util.Styles;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

class Settings {

  static void createSettings() {
    //create table to store setting toggles
    Table soundTable = createToggle("TOGGLE SOUND: ");
    Table musicTable = createToggle("TOGGLE MUSIC: ");
    if (Screens.INSTANCE.getCurrentScreen() == MainMenuScreen.class) {
      VerticalGroup verticalGroup = Screens.INSTANCE.getScreen(MainMenuScreen.class).verticalGroup;
      int offsetForPosition = Screens.INSTANCE.getScreen(MainMenuScreen.class).offsetForPosition;
      Screens.INSTANCE.getScreen(MainMenuScreen.class).soundTable = soundTable;
      Screens.INSTANCE.getScreen(MainMenuScreen.class).musicTable = musicTable;
      verticalGroup.addActorAt(offsetForPosition + 5, soundTable);
      verticalGroup.addActorAt(offsetForPosition + 6, musicTable);
    } else {
      VerticalGroup verticalGroup = Screens.INSTANCE.getScreen(GameScreen.class).verticalGroup;
      verticalGroup.addActor(soundTable);
      verticalGroup.addActor(musicTable);
    }
  }

  private static Table createToggle(String labelString) {
    Table table = new Table().padLeft(20);
    table.defaults().padRight(10);
    TextButton button = Screens.INSTANCE.getCurrentScreen() == MainMenuScreen.class ? Styles.INSTANCE.createPopButton(labelString) : Styles.INSTANCE.createButton(labelString);
    Label label;
    if ((labelString.equals("TOGGLE SOUND: ") && AudioEnum.INSTANCE.isSound()) || (labelString.equals("TOGGLE MUSIC: ") && AudioEnum.INSTANCE.isMusic())) {
      label = Screens.INSTANCE.getCurrentScreen() == MainMenuScreen.class ? Styles.INSTANCE.createSubtleLabel("ON") : Styles.INSTANCE.createLabel("ON");
    } else {
      label = Screens.INSTANCE.getCurrentScreen() == MainMenuScreen.class ? Styles.INSTANCE.createSubtleLabel("OFF") : Styles.INSTANCE.createLabel("OFF");
    }
    button.addListener(buttonToChangeBool(button, label));
    table.add(button);
    table.add(label);
    return table;
  }

  private static InputListener buttonToChangeBool(TextButton button, Label label) {
    return ListenerFactory.newListenerEvent(() -> {
      if (button.getText().toString().equals("TOGGLE SOUND: ")) {
        if (AudioEnum.INSTANCE.isSound()) {
          AudioEnum.INSTANCE.setSound(false);
          label.setText("OFF");
        } else {
          AudioEnum.INSTANCE.setSound(true);
          label.setText("ON");
        }
      } else {
        if (AudioEnum.INSTANCE.isMusic()) {
          AudioEnum.INSTANCE.setMusic(false);
          label.setText("OFF");
        } else {
          AudioEnum.INSTANCE.setMusic(true);
          label.setText("ON");
        }
      }
      return false;
    });
  }

}
