package com.aticatac.server.networking;

import com.aticatac.common.model.ClientModel;
import com.aticatac.server.networking.listen.CommandListener;
import java.io.IOException;
import org.apache.log4j.Logger;

public class Client {
  private final CommandListener commandListener;
  private final ClientModel model;
  private final String id;
  private final Logger logger;

  public Client(CommandListener commandListener, ClientModel model) {
    this.logger = Logger.getLogger(getClass());
    this.commandListener = commandListener;
    this.model = model;
    this.id = this.model.getId();
    (new Thread(commandListener)).start();
  }

  public String getId() {
    return id;
  }

  void shutdown() {
    try {
      this.commandListener.getReader().close();
    } catch (IOException e) {
      this.logger.error(e);
    }
  }
  //TODO add access to threads along with information about client.
}
