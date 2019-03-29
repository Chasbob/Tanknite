package com.aticatac.client.screens;

import com.aticatac.client.util.AudioEnum;
import com.aticatac.client.util.ListenerFactory;
import com.aticatac.client.util.MenuTable;
import com.aticatac.client.util.Styles;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import org.w3c.dom.Text;

/**
 * The type Settings.
 */
class Settings {

  /**
   * Create settings.
   */
  static void createSettings() {
    //create table to store setting toggles
    Table soundTable = createToggle("TOGGLE SOUND: ");
    Table musicTable = createToggle("TOGGLE MUSIC: ");
    if (Screens.INSTANCE.getCurrentScreen() == MainMenuScreen.class) {
      VerticalGroup verticalGroup = Styles.INSTANCE.createVerticalGroup();
      Screens.INSTANCE.getScreen(MainMenuScreen.class).soundTable = soundTable;
      Screens.INSTANCE.getScreen(MainMenuScreen.class).musicTable = musicTable;
      verticalGroup.addActor(soundTable);
      verticalGroup.addActor(musicTable);
      verticalGroup.pack();
      Screens.INSTANCE.getScreen(MainMenuScreen.class).settingsTable.setGroup(verticalGroup);
    } else {
      VerticalGroup verticalGroup = Screens.INSTANCE.getScreen(GameScreenIsometric.class).verticalGroup;
      verticalGroup.addActor(soundTable);
      verticalGroup.addActor(musicTable);
      verticalGroup.pack();
    }
  }

  private static Table createToggle(String labelString) {
    TextButton button;
    Label label;
    if (Screens.INSTANCE.getCurrentScreen() == GameScreen.class) {
      Table table = Styles.INSTANCE.createPopUpTable();
      button = Styles.INSTANCE.createButton(labelString);
      if ((labelString.equals("TOGGLE SOUND: ") && AudioEnum.INSTANCE.isSound()) || (labelString.equals("TOGGLE MUSIC: ") && AudioEnum.INSTANCE.isMusic())) {
        label = Styles.INSTANCE.createLabel("ON");
      } else {
        label = Styles.INSTANCE.createLabel("OFF");
      }
      table.add(button);
      table.add(label);
      button.addListener(buttonToChangeBool(button, label, table));
      ListenerFactory.addHoverListener(button, table);
      return table;
    } else {
      MenuTable table;
      if (labelString.equals("TOGGLE SOUND: ")) {
        table = Styles.INSTANCE.createMenuTable(true, false);
      } else {
        table = Styles.INSTANCE.createMenuTable(false, false);
      }
      button = Styles.INSTANCE.createItalicButton(labelString);
      if ((labelString.equals("TOGGLE SOUND: ") && AudioEnum.INSTANCE.isSound()) || (labelString.equals("TOGGLE MUSIC: ") && AudioEnum.INSTANCE.isMusic())) {
        label = Styles.INSTANCE.createSmallLabel("ON");
      } else {
        label = Styles.INSTANCE.createSmallLabel("OFF");
      }
      table.setButton(button);
      table.add(label);
      button.addListener(buttonToChangeBool(button, label, table));
      ListenerFactory.addHoverListener(button, table);
      return table;
    }
  }

  private static InputListener buttonToChangeBool(TextButton button, Label label, Table table) {
    return ListenerFactory.newListenerEvent(() -> {
      if (Screens.INSTANCE.getCurrentScreen() == MainMenuScreen.class) {
        MenuTable table1 = (MenuTable) table;
        Screens.INSTANCE.getScreen(MainMenuScreen.class).switchDropDownMouse(table1);
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
          AudioEnum.INSTANCE.stopMain();
          label.setText("OFF");
        } else {
          AudioEnum.INSTANCE.setMusic(true);
          if (Screens.INSTANCE.getCurrentScreen().equals(MainMenuScreen.class)) {
            AudioEnum.INSTANCE.getMain();
          }
         label.setText("ON");
        }
      }
      return false;
    });
  }


}
