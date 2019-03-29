package com.aticatac.client.util;

import com.aticatac.client.screens.MainMenuScreen;
import com.aticatac.client.screens.Screens;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * The type Menu table.
 */
public class MenuTable extends Table {

  private Group group;
  private Table table;
  private Label label;
  private Button button;
  private boolean showGroup;
  private boolean tab;

  /**
   * Instantiates a new Menu table.
   *
   * @param tab the tab
   */
  MenuTable(boolean tab) {
    super();
    this.group = null;
    this.button = null;
    this.label = null;
    this.table = null;
    this.showGroup = false;
    this.tab = tab;
  }

  /**
   * Gets group.
   *
   * @return the group
   */
  public Group getGroup() {
    return group;
  }

  /**
   * Sets group.
   *
   * @param group the group
   */
  public void setGroup(Group group) {
    this.group = group;
  }

  /**
   * Is show group boolean.
   *
   * @return the boolean
   */
  public boolean isShowGroup() {
    return showGroup;
  }

  /**
   * Sets show group.
   *
   * @param showGroup the show group
   */
  public void setShowGroup(boolean showGroup) {
    if (showGroup) {
      if (tab) {
        Styles.INSTANCE.addTableColour(this, Color.BLACK);
      } else {
        Styles.INSTANCE.addTableColour(this, new Color(0.973f, 0.514f, 0.475f, 0.25f));
      }
    } else {
      Styles.INSTANCE.addTableColour(this, Styles.INSTANCE.getTransparentColour());
    }
    this.showGroup = showGroup;
  }

  /**
   * Gets button.
   *
   * @return the button
   */
  public Button getButton() {
    return button;
  }

  /**
   * Sets button.
   *
   * @param button the button
   */
  public void setButton(Button button) {
    this.button = button;
    this.add(button);
  }

  /**
   * Gets table.
   *
   * @return the table
   */
  public Table getTable() {
    return table;
  }

  /**
   * Sets table.
   *
   * @param table the table
   */
  public void setTable(Table table) {
    this.table = table;
  }

  /**
   * Gets label.
   *
   * @return the label
   */
  public Label getLabel() {
    return label;
  }

  /**
   * Sets label.
   *
   * @param label the label
   */
  public void setLabel(Label label) {
    this.label = label;
  }
}
