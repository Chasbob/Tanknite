package com.aticatac.client.util;

import com.aticatac.client.networking.Servers;
import com.aticatac.client.screens.Screens;
import com.aticatac.client.screens.ServerScreen;
import com.aticatac.client.screens.UIFactory;
import com.aticatac.common.model.ServerInformation;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
   * @param serverTable the server table
   */
  public ListServers(Table serverTable, Table playerTable) {
    this.logger = Logger.getLogger(getClass());
    int tableSize = 10;
    this.buttons = new ArrayList<>();
    this.labels = new ArrayList<>();
    for (int i = 0; i < tableSize; i++) {
      ServerButton serverButton = new ServerButton();
      serverButton.getLabel().setAlignment(Align.left);
      serverButton.setStyle(Styles.INSTANCE.getBaseButtonStyle());
      serverButton.addListener(UIFactory.newListenerEvent(() -> {
        this.logger.info("Server Button clicked");
        deselect();
        Screens.INSTANCE.getScreen(ServerScreen.class).setServerSelected(true);
        Data.INSTANCE.setCurrentInformation(serverButton.getServerInformation());
        serverButton.setStyle(Styles.INSTANCE.getSelectedButtonStyle());
        return false;
      }));
      serverTable.add(serverButton);
      serverTable.row();
      buttons.add(serverButton);
      Label playersLabel = UIFactory.createSubtleLabel("");
      playersLabel.setAlignment(Align.left);
      labels.add(playersLabel);
      playerTable.add(playersLabel);
      playerTable.row();
    }
  }

  private void deselect() {
    for (ServerButton b : buttons) {
      b.setStyle(Styles.INSTANCE.getBaseButtonStyle());
    }
  }

  public void update() {
    ArrayList<ServerInformation> servers = new ArrayList<>(Servers.INSTANCE.getServers().values());
    for (int i = 0; i < buttons.size(); i++) {
      if (i < servers.size()) {
        if (!buttons.get(i).getLabel().getText().toString().equals(servers.get(i).getId())) {
          buttons.get(i).setStyle(Styles.INSTANCE.getBaseButtonStyle());
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
      } else {
        buttons.get(i).getLabel().setText("<EMPTY>");
      }
      buttons.get(i).getLabel().setStyle(Styles.INSTANCE.getBaseLabelStyle());
    }
  }
}
