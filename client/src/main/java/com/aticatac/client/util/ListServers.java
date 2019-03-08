package com.aticatac.client.util;

import com.aticatac.client.networking.Servers;
import com.aticatac.client.screens.Screens;
import com.aticatac.client.screens.ServerScreen;
import com.aticatac.client.screens.UIFactory;
import com.aticatac.common.model.ServerInformation;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import java.util.ArrayList;

/**
 * The type List servers.
 */
public class ListServers extends Thread {
  private final Table serversTable;

  /**
   * Instantiates a new List servers.
   *
   * @param serverTable the server table
   */
  public ListServers(Table serverTable) {
    this.serversTable = serverTable;
  }

  @Override
  public void run() {
    super.run();
    list();
  }

  private void list() {
    this.serversTable.clearChildren();
    final Servers s = Servers.getInstance();
    ArrayList<ServerInformation> servers = s.getServers();
    for (ServerInformation server : servers) {
      ServerButton serverButton = UIFactory.createServerButton(server.getAddress().getHostAddress(), server);
      serverButton.addListener(UIFactory.newListenerEvent(() -> {
        TextButton currentButton = Screens.INSTANCE.getScreen(ServerScreen.class).getCurrentServer();
        if (!Screens.INSTANCE.getScreen(ServerScreen.class).getServerSelected()) {
          Screens.INSTANCE.getScreen(ServerScreen.class).setServerSelected(true);
          serverButton.setStyle(Styles.INSTANCE.getSelectedButtonStyle());
        } else {
          currentButton.setStyle(Styles.INSTANCE.getButtonStyle());
        }
        Screens.INSTANCE.getScreen(ServerScreen.class).setCurrentServer(serverButton);
        Data.INSTANCE.setCurrentInformation(serverButton.getServerInformation());
        serverButton.setStyle(Styles.INSTANCE.getSelectedButtonStyle());
        return false;
      }));
      serverButton.getLabel().setAlignment(Align.left);
      this.serversTable.add(serverButton);
      this.serversTable.row();
    }
  }
}
