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
public class ListServers {
  private final ArrayList<ServerButton> buttons;
  private final ArrayList<Label> labels;
  private final Logger logger;

  /**
   * Instantiates a new List servers.
   *
   * @param verticalGroup the group that stores horizontal group for each server
   */
  public ListServers(VerticalGroup verticalGroup) {
    this.logger = Logger.getLogger(getClass());
    int tableSize = 10;
    this.buttons = new ArrayList<>();
    this.labels = new ArrayList<>();
    for (int i = 0; i < tableSize; i++) {
      //create menu table to store horizontal group
      MenuTable menuTable;
      if (i == 0) {
        //if the first server we want to select it
        menuTable = Styles.INSTANCE.createMenuTable(true, false);
      } else {
        menuTable = Styles.INSTANCE.createMenuTable(false, false);
      }
      //create a new horizontal group to store server and player count
      HorizontalGroup horizontalGroup = new HorizontalGroup();
      horizontalGroup.space(200);
      ServerButton serverButton = new ServerButton();
      serverButton.getLabel().setAlignment(Align.left);
      serverButton.setStyle(buttonStyle());
      menuTable.setButton(serverButton);
      serverButton.addListener(ListenerFactory.newListenerEvent(() -> {
        this.logger.info("Server Button clicked");
        //if(!serverButton.getLabel().textEquals("<EMPTY>")){
        menuTable.getLabel().setStyle(Styles.INSTANCE.createLabelStyle(Styles.INSTANCE.italicFont, Color.LIME));
        serverButton.setStyle(Styles.INSTANCE.createButtonStyle(Styles.INSTANCE.baseFont, Color.WHITE));
        menuTable.setShowGroup(true);
        //}
        return false;
      }));
      horizontalGroup.addActor(serverButton);
      buttons.add(serverButton);
      Label playersLabel = Styles.INSTANCE.createCustomLabelWithFont(Styles.INSTANCE.italicFont, "0/0", Color.GRAY);
      playersLabel.setAlignment(Align.left);
      labels.add(playersLabel);
      horizontalGroup.addActor(playersLabel);
      menuTable.add(horizontalGroup);
      menuTable.setLabel(playersLabel);
      verticalGroup.addActor(menuTable);
    }
  }

  public void update() {
    ArrayList<ServerInformation> servers = new ArrayList<>(Servers.INSTANCE.getServers().values());
    for (int i = 0; i < buttons.size(); i++) {
      if (i < servers.size()) {
        if (!buttons.get(i).getLabel().getText().toString().equals(servers.get(i).getId())) {
          buttons.get(i).setStyle(buttonStyle());
        }
        buttons.get(i).getLabel().setText(servers.get(i).getId());
        buttons.get(i).setServerInformation(servers.get(i));
        int playerCount = buttons.get(i).getServerInformation().getPlayerCount();
        int maxPlayers = buttons.get(i).getServerInformation().getMaxPlayers();
        if (playerCount<maxPlayers){
          labels.get(i).setText(playerCount+"/"+maxPlayers);
        }else{
          labels.get(i).setText("FULL");
        }
        labels.get(i).setStyle(Styles.INSTANCE.createLabelStyle(Styles.INSTANCE.italicFont, Color.LIME));
        buttons.get(i).setTouchable(Touchable.enabled);
      } else {
        buttons.get(i).getLabel().setText("<EMPTY>");
        //buttons.get(i).setTouchable(Touchable.disabled);
      }
      buttons.get(i).getLabel().setStyle(Styles.INSTANCE.createLabelStyle(Styles.INSTANCE.baseFont, Color.WHITE));
    }
  }

  private Button.ButtonStyle buttonStyle() {
    return Styles.INSTANCE.createButtonStyle(Styles.INSTANCE.baseFont, Color.GRAY);
  }
}
