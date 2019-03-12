package com.aticatac.server.networking;

import com.aticatac.server.networking.authentication.Authenticator;
import java.io.IOException;
import java.net.Socket;
import org.apache.log4j.Logger;

public class NewHost implements Runnable {
  private final Logger logger;

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
      e.printStackTrace();
    }
  }
}

