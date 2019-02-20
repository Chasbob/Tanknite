package com.aticatac.server;

import com.aticatac.server.gameManager.GameManager;
import com.aticatac.server.networking.Server;
import org.apache.log4j.Logger;

public class Main {
    public static void main(String[] args) throws Exception {
        Logger logger = Logger.getLogger(Main.class);

        var server = new Server();
        server.start();

        var gameManager = new GameManager();


    }
}
