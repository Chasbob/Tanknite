package com.aticatac.server;

import com.aticatac.server.networking.Server;
import org.apache.log4j.Logger;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Server server;
        Logger logger = Logger.getLogger(Main.class);
        try {
            server = new Server();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        server.start();
    }
}
