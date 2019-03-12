package com.aticatac.client.util;

import com.aticatac.client.networking.Servers;
import com.aticatac.client.screens.Screens;
import com.aticatac.client.screens.ServerScreen;
import com.aticatac.client.screens.UIFactory;
import com.aticatac.common.model.ServerInformation;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 * The type List servers.
 */
public class ListServers {
  private final Table serversTable;
  private final int tableSize;
  private final ArrayList<ServerButton> buttons;
  private final Logger logger;

  /**
   * Instantiates a new List servers.
   *
   * @param serverTable the server table
   */
  public ListServers(Table serverTable) {
    this.logger = Logger.getLogger(getClass());
    this.serversTable = serverTable;
    this.tableSize = 10;
    this.buttons = new ArrayList<>();
    serverTable.defaults().padLeft(100);
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
      this.serversTable.add(serverButton);
      this.serversTable.row();
      buttons.add(serverButton);
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
      } else {
        buttons.get(i).getLabel().setText("<EMPTY>");
      }
      buttons.get(i).getLabel().setStyle(Styles.INSTANCE.getBaseLabelStyle());
    }
  }
}
