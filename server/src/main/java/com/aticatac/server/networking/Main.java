package com.aticatac.server.networking;

public class Main {
  public static void main(String[] args) {
    try {
      Server server = new Server();
      server.run();
      Thread.sleep(5000);
      server.shutdown();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
