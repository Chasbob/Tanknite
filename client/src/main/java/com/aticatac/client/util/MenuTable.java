package com.aticatac.client.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

public class MenuTable extends Table {

  private Group group;
  private TextButton button;
  private boolean showGroup;
  private boolean tab;

  public MenuTable(boolean tab) {
    super();
    this.group = new VerticalGroup();
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
        Styles.INSTANCE.addTableColour(this, new Color(0.973f, 0.514f, 0.475f, 0.25f));
      }
    } else {
      Styles.INSTANCE.addTableColour(this, new Color(0f, 0f, 0f, 0.25f));
    }
    this.showGroup = showGroup;
  }

  public TextButton getButton() {
    return button;
  }

  public void setButton(TextButton button) {
    this.button = button;
    this.add(button);
  }
}
