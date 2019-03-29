package com.aticatac.client.server.networking;

import com.aticatac.client.server.networking.authentication.Authenticator;
import java.io.IOException;
import java.net.Socket;
import org.apache.log4j.Logger;

/**
 * The type New host.
 */
public class NewHost implements Runnable {
  private final Logger logger;

  /**
   * Instantiates a new New host.
   */
  NewHost() {
    this.logger = Logger.getLogger(getClass());
  }

  @Override
  public void run() {
    try {
      Socket client = Server.ServerData.INSTANCE.getServerSocket().accept();
      logger.trace("Client opened connection!");
      new Authenticator(client).run();
    } catch (IOException e) {
      this.logger.error(e);
    }
  }
}

