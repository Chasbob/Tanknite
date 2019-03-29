package com.aticatac.client.server.networking.listen;

import com.aticatac.client.server.bus.event.UnexpectedDisconnect;
import com.aticatac.client.server.networking.Server;
import com.aticatac.common.model.Command;
import com.aticatac.common.model.CommandModel;
import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.Login;
import com.aticatac.common.model.ModelReader;
import java.io.BufferedReader;
import java.io.IOException;
import org.apache.log4j.Logger;

import static com.aticatac.client.bus.EventBusFactory.serverEventBus;

/**
 * The type Command listener.
 */
public class CommandListener implements Runnable {
  private final Logger logger;
  private final BufferedReader reader;
  private final ModelReader modelReader;
  private final Login login;
  private boolean run;

  /**
   * Instantiates a new Command listener.
   *
   * @param reader the reader
   * @param login  the login
   */
  public CommandListener(BufferedReader reader, final Login login) {
    this.reader = reader;
    this.login = login;
    logger = Logger.getLogger(getClass());
    this.modelReader = new ModelReader();
    this.run = true;
  }

  @Override
  public void run() {
    listen();
  }

  private void listen() {
    this.logger.trace("Listening");
    while (!Thread.currentThread().isInterrupted() && run) {
      try {
        String json = this.reader.readLine();
        CommandModel commandModel = modelReader.fromJson(json, CommandModel.class);
        this.logger.trace("JSON: " + json);
        Server.ServerData.INSTANCE.putCommand(commandModel);
        if (commandModel.getCommand() == Command.QUIT) {
          Thread.currentThread().interrupt();
        }
      } catch (IOException | InvalidBytes e) {
        this.logger.error(e);
        this.logger.error("Client died.");
        serverEventBus.post(new UnexpectedDisconnect(login.getId()));
        return;
      }
    }
  }

  /**
   * Shutdown.
   */
  public void shutdown() {
    this.run = false;
  }
}
