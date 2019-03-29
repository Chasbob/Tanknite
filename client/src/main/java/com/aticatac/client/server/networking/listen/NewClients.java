package com.aticatac.client.server.networking.listen;

import com.aticatac.client.server.networking.Server;
import com.aticatac.client.server.networking.authentication.Authenticator;
import java.io.IOException;
import java.net.Socket;
import org.apache.log4j.Logger;

/**
 * The type New clients.
 */
public class NewClients implements Runnable {
  //    private final ServerSocket serverSocket;
  private final Logger logger;
  private boolean run;

  /**
   * Instantiates a new New clients.
   */
  public NewClients() {
    this.run = true;
    this.logger = Logger.getLogger(getClass());
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted() && run) {
      try {
        logger.info("Client count: " + Server.ServerData.INSTANCE.getClients().size());
        logger.trace("Waiting for client...");
        Socket client = Server.ServerData.INSTANCE.getServerSocket().accept();
        logger.trace("Client opened connection!");
        (new Thread(new Authenticator(client))).start();
      } catch (IOException e) {
        this.logger.error(e);
        this.logger.error("Failed to open connection with client.");
      }
    }
    this.logger.warn("Finished!");
  }

  /**
   * Shutdown.
   */
  public void shutdown() {
    this.run = false;
  }
}
