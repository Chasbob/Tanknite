package com.aticatac.client.util;

import com.aticatac.client.networking.Servers;
import com.aticatac.common.model.ServerInformation;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

public class ListServers extends Thread {
    private final Table serversTable;
    private final UIFactory uiFactory;

    public ListServers(Table serverTable, UIFactory uiFactory) {
        this.serversTable = serverTable;
        this.uiFactory = uiFactory;
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
            TextButton serverButton = this.uiFactory.createButton(server.getAddress().getHostAddress());
            serverButton.getLabel().setAlignment(Align.left);
            this.serversTable.add(serverButton);
            serverButton.addListener(this.uiFactory.createServerButtonListener(serverButton));
            this.serversTable.row();
        }
    }
}
