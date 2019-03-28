package com.aticatac.client.util;

import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.objectsystem.containers.KillLogEvent;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.apache.log4j.Logger;

public class HudUpdate {
  private Update update;
  private Table killLog;
  private Label ammoLabel;
  private Label playerCountLabel;
  private Label killCountLabel;
  private float health;
  private Logger logger;

  public HudUpdate(Table killLog, Label ammoLabel, Label playerCountLabel, Label killCountLabel) {
    this.health = 1;
    this.logger = Logger.getLogger(getClass());
    this.killLog = killLog;
    this.ammoLabel = ammoLabel;
    this.playerCountLabel = playerCountLabel;
    this.killCountLabel = killCountLabel;
  }

  public float getHealth() {
    return health;
  }

  public void update(Update update) {
    this.update = update;
    updateKillLog();
    updateAmmoLabel();
    updatePlayerCount();
    updateKillCount();
    updateHealth();
  }

  private void updateKillLog() {
  }

  private void updateAmmoLabel() {
    this.ammoLabel.setText(" " + update.getMe(Data.INSTANCE.getID()).getHealth() + " ");
  }

  private void updatePlayerCount() {
  }

  private void updateKillCount() {
    killLog.clear();
    for (KillLogEvent event : update.getKillLogEvents()) {
      Label killLabel = Styles.INSTANCE.createCustomLabelWithFont(Styles.INSTANCE.baseFont, event.getKiller() + " killed: " + event.getKilled(), Color.GOLD);
      killLog.add(killLabel);
      killLog.row();
    }
  }

  private void updateHealth() {
    this.health = update.getMe(Data.INSTANCE.getID()).getHealth() / 100f;
  }
}
