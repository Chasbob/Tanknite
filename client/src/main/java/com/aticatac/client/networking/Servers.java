package com.aticatac.client.networking;

import com.aticatac.common.model.ServerInformation;

import java.util.ArrayList;

public enum Servers {
    INSTANCE;
    final ArrayList<ServerInformation> servers;
    private final PopulateServers pop;

    Servers() {
        System.out.println("Servers enum starting");
        this.servers = new ArrayList<>();
        this.pop = new PopulateServers(this.servers);
        this.pop.start();
    }

    public static Servers getInstance() {
        return INSTANCE;
    }

    public ArrayList<ServerInformation> getServers() {
        return servers;
    }
}
