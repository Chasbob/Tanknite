package com.aticatac.client.util;

import com.aticatac.client.screens.Screens;
import com.aticatac.client.screens.UIFactory;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class PopulatePlayers extends Thread {
    private final Table playerTable;
    private final Label playerCount;
    private final Logger logger;

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
        this.logger.info("Listing...");
        //TODO
        ArrayList<String> players;
//        try {
        players = new ArrayList<>(Screens.INSTANCE.getUpdate().getPlayers());
        this.logger.info("Players: " + players.toString());
        this.playerTable.clearChildren();
        this.logger.info("Got update! " + players.size());
//        } catch (InterruptedException e) {
//            this.logger.error(e);
//        }
        this.logger.trace("Listing");
        for (String player : players) {
            this.logger.info("Player: " + player);
            Label playerLabel = UIFactory.createLabel(player);
            this.playerTable.add(playerLabel);
            this.playerTable.row();
        }
        this.playerCount.setText(players.size());
    }
}
