package com.aticatac.client.server.networking;

import com.aticatac.client.server.networking.listen.CommandListener;
import com.aticatac.common.model.ClientModel;
import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.Updates.Update;
import java.io.PrintStream;
import org.apache.log4j.Logger;

/**
 * The type Client.
 */
public class Client {
  private final CommandListener commandListener;
  private final ClientModel model;
  private final String id;
  private final Logger logger;
  private final PrintStream printer;
  private final ModelReader modelReader;

  /**
   * Instantiates a new Client.
   *
   * @param commandListener the command listener
   * @param model           the model
   * @param printer         the printer
   */
  public Client(CommandListener commandListener, ClientModel model, PrintStream printer) {
    this.logger = Logger.getLogger(getClass());
    this.commandListener = commandListener;
    this.model = model;
    this.id = this.model.getId();
    this.printer = printer;
    this.modelReader = new ModelReader();
    (new Thread(commandListener)).start();
  }

  /**
   * Send update.
   *
   * @param update the update
   */
  public void sendUpdate(Update update) {
    String json = modelReader.toJson(update);
    this.printer.println(json);
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * Shutdown.
   */
  void shutdown() {
    this.commandListener.shutdown();
  }
  //TODO add access to threads along with information about client.
}
