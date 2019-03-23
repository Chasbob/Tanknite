package com.aticatac.client.screens;

import com.aticatac.client.util.AudioEnum;
import com.aticatac.client.util.ListenerFactory;
import com.aticatac.client.util.MenuTable;
import com.aticatac.client.util.Styles;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;

class Settings {

  static void createSettings() {
    //create table to store setting toggles
    MenuTable soundTable = createToggle("TOGGLE SOUND: ");
    MenuTable musicTable = createToggle("TOGGLE MUSIC: ");
    if (Screens.INSTANCE.getCurrentScreen() == MainMenuScreen.class) {
      VerticalGroup verticalGroup = Styles.INSTANCE.createVerticalGroup();
      Screens.INSTANCE.getScreen(MainMenuScreen.class).soundTable = soundTable;
      Screens.INSTANCE.getScreen(MainMenuScreen.class).musicTable = musicTable;
      verticalGroup.addActor(soundTable);
      verticalGroup.addActor(musicTable);
      verticalGroup.pack();
      Screens.INSTANCE.getScreen(MainMenuScreen.class).settingsTable.setGroup(verticalGroup);
    } else {
      VerticalGroup verticalGroup = Screens.INSTANCE.getScreen(GameScreen.class).verticalGroup;
      verticalGroup.addActor(soundTable);
      verticalGroup.addActor(musicTable);
      verticalGroup.pack();
    }
  }

  private static MenuTable createToggle(String labelString) {
    MenuTable table;
    if (labelString.equals("TOGGLE SOUND: ")) {
      table = Screens.INSTANCE.getScreen(MainMenuScreen.class).createMenuTable(true, false);
    } else {
      table = Screens.INSTANCE.getScreen(MainMenuScreen.class).createMenuTable(false, false);
    }
    TextButton button = Screens.INSTANCE.getCurrentScreen() == MainMenuScreen.class ? Styles.INSTANCE.createItalicButton(labelString) : Styles.INSTANCE.createButton(labelString);
    Label label;
    if ((labelString.equals("TOGGLE SOUND: ") && AudioEnum.INSTANCE.isSound()) || (labelString.equals("TOGGLE MUSIC: ") && AudioEnum.INSTANCE.isMusic())) {
      label = Screens.INSTANCE.getCurrentScreen() == MainMenuScreen.class ? Styles.INSTANCE.createSmallLabel("ON") : Styles.INSTANCE.createLabel("ON");
    } else {
      label = Screens.INSTANCE.getCurrentScreen() == MainMenuScreen.class ? Styles.INSTANCE.createSmallLabel("OFF") : Styles.INSTANCE.createLabel("OFF");
    }
    button.addListener(buttonToChangeBool(button, label, table));
    table.setButton(button);
    table.add(label);
    return table;
  }

  private static InputListener buttonToChangeBool(TextButton button, Label label, MenuTable table) {
    return ListenerFactory.newListenerEvent(() -> {
      if (Screens.INSTANCE.getCurrentScreen() == MainMenuScreen.class) {
        Screens.INSTANCE.getScreen(MainMenuScreen.class).switchDropDownMouse(table);
      }
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
