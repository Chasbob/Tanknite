package com.aticatac.server.networking;

import com.aticatac.common.model.ClientModel;
import com.aticatac.server.networking.listen.CommandListener;

public class Client {
    private final CommandListener commandListener;
    private final ClientModel model;
    private final String id;

    public Client(CommandListener commandListener, ClientModel model) {
        this.commandListener = commandListener;
        this.model = model;
        this.id = this.model.getId();
    }
}
