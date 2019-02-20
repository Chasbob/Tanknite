package com.aticatac.server;

import com.aticatac.server.gameManager.GameManager;
import com.aticatac.server.networking.Server;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        Logger logger = Logger.getLogger(Main.class);
        var server = new Server();
        server.start();
        ArrayList<String> client = new ArrayList<>(server.getClients().keySet());
        var gameManager = new GameManager(client);
        (new Thread(() -> {
            while (true) {
                try {
                    logger.warn("Getting command");
                    gameManager.playerInput("username", server.nextCommand());
                    logger.warn("Getting command");
                } catch (InterruptedException ignored) {
                }
            }
        })).start();
    }
}
