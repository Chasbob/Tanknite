package com.aticatac.client.util;

import com.aticatac.client.networking.Servers;
import com.aticatac.client.screens.Screens;
import com.aticatac.client.screens.ServerScreen;
import com.aticatac.client.screens.UIFactory;
import com.aticatac.common.model.ServerInformation;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

public class ListServers extends Thread {
    private final Table serversTable;

    public ListServers(Table serverTable) {
        this.serversTable = serverTable;
    }

    @Override
    public void run() {
        super.run();
        while (!this.isInterrupted()) {
            try {
                list();
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void list() {
        this.serversTable.clearChildren();
        final Servers s = Servers.getInstance();
        ArrayList<ServerInformation> servers = s.getServers();
        for (ServerInformation server : servers) {
            ServerButton serverButton = UIFactory.createServerButton(server.getAddress().getHostAddress(), server);
            serverButton.addListener(UIFactory.newListenerEvent(() -> {
                if (!Screens.INSTANCE.getScreen(ServerScreen.class).getServerSelected()) {
                    Screens.INSTANCE.getScreen(ServerScreen.class).setServerSelected(true);
                } else {
                    if (Screens.INSTANCE.getScreen(ServerScreen.class).getCurrentServer() != null) {
                        Screens.INSTANCE.getScreen(ServerScreen.class).getCurrentServer().setStyle(Styles.INSTANCE.getButtonStyle());
                    }
                    serverButton.setStyle(Styles.INSTANCE.getSelectedButtonStyle());
                    Screens.INSTANCE.getScreen(ServerScreen.class).setCurrentServer(serverButton);
                    Screens.INSTANCE.setCurrentInformation(serverButton.getServerInformation());
                }
                return false;
            }));
            serverButton.getLabel().setAlignment(Align.left);
            this.serversTable.add(serverButton);
            this.serversTable.row();
        }
    }
}
