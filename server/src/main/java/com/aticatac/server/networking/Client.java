package com.aticatac.server.networking;

import com.aticatac.common.model.ClientModel;
import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.server.networking.listen.CommandListener;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.log4j.Logger;

public class Client {
  private final CommandListener commandListener;
  private final ClientModel model;
  private final String id;
  private final Logger logger;
  private final PrintStream printer;
  private final ModelReader modelReader;

  public Client(CommandListener commandListener, ClientModel model, PrintStream printer) {
    this.logger = Logger.getLogger(getClass());
    this.commandListener = commandListener;
    this.model = model;
    this.id = this.model.getId();
    this.printer = printer;
    this.modelReader=new ModelReader();
    (new Thread(commandListener)).start();
  }

  public void sendUpdate(Update update) {
    String json = modelReader.toJson(update);
    this.printer.println(json);
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
