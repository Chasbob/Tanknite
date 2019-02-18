package com.aticatac.client.networking;

import com.aticatac.common.model.ServerInformation;

import java.util.ArrayList;

public class PopulateServers extends Thread {
    private final ArrayList<ServerInformation> servers;

    public PopulateServers(ArrayList<ServerInformation> servers) {
        this.servers = servers;
    }

    @Override
    public void run() {
        super.run();
        try {
            ServerInformation newServer = new BroadcastListener().call();
            this.servers.add(newServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
