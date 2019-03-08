package com.aticatac.server;

import com.aticatac.server.networking.Server;
import org.apache.log4j.Logger;

public class Main {
  public static void main(String[] args) {
    Server server;
    Logger logger = Logger.getLogger(Main.class);
    server = new Server();
    server.start();
  }
}
