package com.aticatac.client.server.networking;

/**
 * The type Main.
 */
public class Main {
  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    try {
      Server server = new Server(false, "Networking Main");
      server.run();
      Thread.sleep(5000);
      server.shutdown();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
