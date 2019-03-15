package com.aticatac.client.util;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 * The type Populate players.
 */
public class PopulatePlayers extends Thread {
  private final Table playerTable;
  private final Label playerCount;
  private final Label header;
  private final Logger logger;
  private final ArrayList<Label> labels;

  /**
   * Instantiates a new Populate players.
   *
   * @param serverTable the server table
   * @param playerCount the player count
   */
  public PopulatePlayers(Table serverTable, Label playerCount, Label header) {
    this.playerTable = serverTable;
    this.playerCount = playerCount;
    this.header = header;
    this.logger = Logger.getLogger(getClass());
    labels = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      Label label = new Label("", Styles.INSTANCE.getBaseLabelStyle());
      this.labels.add(label);
      this.playerTable.add(label);
      this.playerTable.row();
    }
  }

  @Override
  public void run() {
    this.logger.info("Run");
    super.run();
    while (!this.isInterrupted()) {
      try {
        Thread.sleep(1000);
        list();
      } catch (InterruptedException e) {
        this.logger.error(e);
      }
    }
  }

  private void list() {
    ArrayList<String> players = Data.INSTANCE.getClients();
    for (int i = 0; i < labels.size(); i++) {
      if (i < players.size()) {
        labels.get(i).setText(players.get(i));
      } else {
        labels.get(i).setText("<EMPTY>");
      }
    }
    this.playerCount.setText(players.size());
    if(players.size() == 10){
      header.setText("Lobby Full ");
    }else{
      header.setText("Looking for players.. ");
    }
  }
}
