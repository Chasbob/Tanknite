package com.aticatac.client.screens;

import com.aticatac.client.util.Styles;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * The type Hud.
 */
class HUD {

  /**
   * Create hud top left table.
   *
   * @param playerCount the player count
   * @param killCount   the kill count
   * @return the table
   */
  static Table createHudTopLeft(Label playerCount, Label killCount) {
    Table topLeftTable = new Table();
    topLeftTable.top().left();
    topLeftTable.defaults().padTop(10).padLeft(10).left();
    Table aliveTable = new Table();
    Table aliveLabelTable = createTable();
    Styles.INSTANCE
      .addTableColour(aliveLabelTable, Color.GRAY);
    Label aliveLabel = Styles.INSTANCE.createLabel("Alive");
    aliveLabelTable.add(aliveLabel);
    Table playerCountTable = createTable();
    playerCountTable.add(playerCount).center();
    Styles.INSTANCE
      .addTableColour(playerCountTable, new Color(0f, 0f, 0f, 0.5f));
    aliveTable.add(playerCountTable);
    aliveTable.add(aliveLabelTable);
    Table killTable = new Table();
    Table killLableTable = createTable();
    ;
    Styles.INSTANCE
      .addTableColour(killLableTable, Color.GRAY);
    Label killLabel = Styles.INSTANCE.createLabel("Killed");
    killLableTable.add(killLabel);
    Table killCountTable = createTable();
    ;
    Styles.INSTANCE
      .addTableColour(killCountTable, new Color(0f, 0f, 0f, 0.5f));
    killCountTable.add(killCount);
    killTable.add(killCountTable);
    killTable.add(killLableTable);
    topLeftTable.add(aliveTable);
    topLeftTable.add(killTable);
    return topLeftTable;
  }

  /**
   * Create hud bottom left table.
   *
   * @return the table
   */
  static Table createHudBottomLeft() {
    Table killLogTable = new Table();
    killLogTable.bottom().left();
    killLogTable.defaults().padTop(10).padLeft(10).padBottom(20).left();
    Label tempKill = Styles.INSTANCE.createLabel("");
    killLogTable.add(tempKill);
    return killLogTable;
  }

  /**
   * Create hud bottom right table.
   *
   * @param ammoValue the ammo value
   * @return the table
   */
  static Table createHudBottomRight(Label ammoValue) {
    Table bottomRightTable = new Table();
    bottomRightTable.bottom().right();
    bottomRightTable.defaults().padRight(10).padTop(10).padBottom(20).left();
    Table ammoTable = new Table();
    Table ammoValueTable = createTable();
    Styles.INSTANCE
      .addTableColour(ammoValueTable, new Color(0f, 0f, 0f, 0.5f));
    ammoValueTable.add(ammoValue);
    Label ammoLabel = Styles.INSTANCE.createLabel("Ammo");
    Table ammoLabelTable = createTable();
    Styles.INSTANCE
      .addTableColour(ammoLabelTable, Color.GRAY);
    ammoLabelTable.add(ammoLabel);
    ammoTable.add(ammoValueTable);
    ammoTable.add(ammoLabelTable);
    bottomRightTable.add(ammoTable);
    return bottomRightTable;
  }

  /**
   * Create hud alert table table.
   *
   * @return the table
   */
  static Table createHudAlertTable() {
    Table alertTable = new Table();
    alertTable.bottom();
    alertTable.defaults().padBottom(60);
    Label alertLabel = Styles.INSTANCE.createLabel("TRACTION DISABLED");
    alertTable.add(alertLabel);
    alertTable.setVisible(false);
    return alertTable;
  }

  private static Table createTable() {
    Table table = new Table();
    table.defaults().pad(2.5f);
    return table;
  }
}
