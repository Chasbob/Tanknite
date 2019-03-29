package com.aticatac.client.util;

import com.badlogic.gdx.scenes.scene2d.ui.*;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * The type Populate players.
 */
public class PopulatePlayers extends Thread {
  private final Label playerCount;
  private final Label header;
  private final Logger logger;
  private final ArrayList<TextButton> buttons;
  private final ArrayList<Label> labels;

  /**
   * Instantiates a new Populate players.
   *
   * @param playersGroup the server table
   * @param playerCount  the player count
   * @param header       the header
   */
  public PopulatePlayers(VerticalGroup playersGroup, Label playerCount, Label header) {
    this.playerCount = playerCount;
    this.header = header;
    this.logger = Logger.getLogger(getClass());
    buttons = new ArrayList<>();
    labels = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      //create menu table to store horizontal player group
      MenuTable playerTable = Styles.INSTANCE.createMenuTable(false, false);
      //playerTable.setFillParent(true);
      HorizontalGroup playerGroup = new HorizontalGroup();
      playerGroup.space(200);
      //create actors
      TextButton playerNameButton = Styles.INSTANCE.createButton("");
      Label playerRankingLabel = Styles.INSTANCE.createLabel("");
      this.buttons.add(playerNameButton);
      this.labels.add(playerRankingLabel);
      playerGroup.addActor(playerNameButton);
      playerGroup.addActor(playerRankingLabel);
      playerTable.add(playerGroup);
      playersGroup.addActor(playerTable);
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
    for (int i = 0; i < 10; i++) {
      if (i < players.size()) {
        buttons.get(i).setText(players.get(i));
        //TODO get ranking from database
        labels.get(i).setText("1");
      } else {
        buttons.get(i).setText("<EMPTY>");
        labels.get(i).setText("N/A");
      }
    }
    this.playerCount.setText(players.size());
    if(players.size() == 10){
      header.setText("LOBBY FULL ");
    }else{
      header.setText("LOOKING FOR PLAYERS.. ");
    }
  }
}
