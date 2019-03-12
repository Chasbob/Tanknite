package com.aticatac.client.util;

import com.aticatac.client.screens.UIFactory;
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
  private final Logger logger;

  /**
   * Instantiates a new Populate players.
   *
   * @param serverTable the server table
   * @param playerCount the player count
   */
  public PopulatePlayers(Table serverTable, Label playerCount) {
    this.playerTable = serverTable;
    this.playerCount = playerCount;
    this.logger = Logger.getLogger(getClass());
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
    this.logger.trace("Listing...");
    //TODO
    ArrayList<String> players;
    players = new ArrayList<>(Data.INSTANCE.getClients());
    this.logger.trace("Players: " + players.toString());
    this.playerTable.clearChildren();
    this.logger.trace("Got update! " + players.size());
    this.logger.trace("Listing");
    for (String player : players) {
      this.logger.trace("Player: " + player);
      Label playerLabel = UIFactory.createLabel(player);
      this.playerTable.add(playerLabel);
      this.playerTable.row();
    }
    this.playerCount.setText(players.size());
  }
}
