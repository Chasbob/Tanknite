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
  private boolean shutdown;

  /**
   * Instantiates a new New clients.
   */
  public NewClients() {
    this.shutdown = false;
//        this.serverSocket = new ServerSocket(Server.ServerData.INSTANCE.getPort());
    this.logger = Logger.getLogger(getClass());
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted() && !shutdown) {
      try {
        logger.info("Client count: " + Server.ServerData.INSTANCE.getClients().size());
        logger.trace("Waiting for client...");
        Socket client = Server.ServerData.INSTANCE.getServerSocket().accept();
        logger.trace("Client opened connection!");
        (new Thread(new Authenticator(client))).start();
      } catch (IOException e) {
        this.logger.error(e);
        this.logger.error("Failed to open connection with client.");
        return;
      }
    }
    this.logger.warn("Finished!");
  }

  public void shutdown() {
    this.shutdown = true;
  }
}
