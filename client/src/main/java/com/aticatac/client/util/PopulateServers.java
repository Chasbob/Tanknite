package com.aticatac.client.util;

import com.aticatac.client.networking.Servers;
import com.aticatac.client.screens.Screens;
import com.aticatac.client.screens.ServerScreen;
import com.aticatac.common.model.ServerInformation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * The type List servers.
 */
public class PopulateServers {
  private final ArrayList<ServerButton> buttons;
  private final ArrayList<Label> labels;

  /**
   * Instantiates a new List servers.
   *
   * @param verticalGroup the group that stores horizontal group for each server
   */
  public PopulateServers(VerticalGroup verticalGroup) {
    int tableSize = 10;
    this.buttons = new ArrayList<>();
    this.labels = new ArrayList<>();
    for (int i = 0; i < tableSize; i++) {
      //create menu table to store horizontal group
      Table table = Styles.INSTANCE.createPopUpTable();
      //create a new horizontal group to store server and player count
      HorizontalGroup horizontalGroup = new HorizontalGroup();
      horizontalGroup.space(200);
      ServerButton serverButton = new ServerButton();
      serverButton.getLabel().setAlignment(Align.left);
      serverButton.setStyle(Styles.INSTANCE.createButtonStyle(Styles.INSTANCE.baseFont, Color.GRAY));
      table.add(serverButton);
      serverButton.addListener(ListenerFactory.newListenerEvent(() -> {
        //get current table and unhighlight them
        Screens.INSTANCE.getScreen(ServerScreen.class).unHighlight();
        if (!serverButton.getLabel().textEquals("<EMPTY>")) {
          //highlight table
          Styles.INSTANCE.addTableColour(table, new Color(0.973f, 0.514f, 0.475f, 0.25f));
          //set current info in data
          Data.INSTANCE.setCurrentInformation(serverButton.getServerInformation());
          //set server selected
          Screens.INSTANCE.getScreen(ServerScreen.class).setServerSelected(true);
        }
        return false;
      }));
      ListenerFactory.addHoverListener(serverButton, table);
      horizontalGroup.addActor(serverButton);
      buttons.add(serverButton);
      Label playersLabel = Styles.INSTANCE.createCustomLabelWithFont(Styles.INSTANCE.italicFont, "0/0", Color.GRAY);
      playersLabel.setAlignment(Align.left);
      labels.add(playersLabel);
      horizontalGroup.addActor(playersLabel);
      table.add(horizontalGroup);
      verticalGroup.addActor(table);
    }
  }

  /**
   * Update.
   */
  public void update() {
    ArrayList<ServerInformation> servers = new ArrayList<>(Servers.INSTANCE.getServers().values());
    for (int i = 0; i < buttons.size(); i++) {
      if (i < servers.size()) {
        buttons.get(i).getLabel().setText(servers.get(i).getId());
        buttons.get(i).setServerInformation(servers.get(i));
        int playerCount = buttons.get(i).getServerInformation().getPlayerCount();
        int maxPlayers = buttons.get(i).getServerInformation().getMaxPlayers();
        if (playerCount < maxPlayers) {
          labels.get(i).setText(playerCount + "/" + maxPlayers);
        } else {
          labels.get(i).setText("FULL");
        }
        labels.get(i).setStyle(Styles.INSTANCE.createLabelStyle(Styles.INSTANCE.italicFont, Color.LIME));
        buttons.get(i).setStyle(Styles.INSTANCE.createButtonStyle(Styles.INSTANCE.baseFont, Color.WHITE));
      } else {
        buttons.get(i).getLabel().setText("<EMPTY>");
      }
    }
  }
}
