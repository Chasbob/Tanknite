package com.aticatac.client.util;

import com.aticatac.client.screens.MainMenuScreen;
import com.aticatac.client.screens.Screens;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MenuTable extends Table {

  private Group group;
  private Table table;
  private Label label;
  private Button button;
  private boolean showGroup;
  private boolean tab;

  MenuTable(boolean tab) {
    super();
    this.group = null;
    this.button = null;
    this.label = null;
    this.table = null;
    this.showGroup = false;
    this.tab = tab;
  }

  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
  }

  public boolean isShowGroup() {
    return showGroup;
  }

  public void setShowGroup(boolean showGroup) {
    if (showGroup) {
      if (tab) {
        Styles.INSTANCE.addTableColour(this, Color.BLACK);
      } else {
        if (Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpMultiplayer || Screens.INSTANCE.getScreen(MainMenuScreen.class).popUpLogin) {
          //from pop up menu
          Styles.INSTANCE.addTableColour(this, new Color(1, 1, 1, 0.25f));
        } else {
          Styles.INSTANCE.addTableColour(this, new Color(0.973f, 0.514f, 0.475f, 0.25f));
        }
      }
    } else {
      Styles.INSTANCE.addTableColour(this, Styles.INSTANCE.getTransparentColour());
    }
    this.showGroup = showGroup;
  }

  public Button getButton() {
    return button;
  }

  public void setButton(Button button) {
    this.button = button;
    this.add(button);
  }

  public Table getTable() {
    return table;
  }

  public void setTable(Table table) {
    this.table = table;
  }

  public Label getLabel() {
    return label;
  }

  public void setLabel(Label label) {
    this.label = label;
  }
}
