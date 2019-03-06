package com.aticatac.server.networking.listen;

import com.aticatac.common.model.Command;
import com.aticatac.common.model.CommandModel;
import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.ModelReader;
import com.aticatac.server.networking.Server;
import java.io.BufferedReader;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * The type Command listener.
 */
public class CommandListener implements Runnable {
  private final Logger logger;
  private final BufferedReader reader;

  /**
   * Instantiates a new Command listener.
   *
   * @param reader the reader
   */
  public CommandListener(BufferedReader reader) {
    this.reader = reader;
    logger = Logger.getLogger(getClass());
  }

  @Override
  public void run() {
    listen();
  }

  /**
   * Gets reader.
   *
   * @return the reader
   */
  public BufferedReader getReader() {
    return reader;
  }

  private void listen() {
    this.logger.trace("Listening");
    while (!Thread.currentThread().isInterrupted()) {
      try {
        String json = this.reader.readLine();
        CommandModel commandModel = ModelReader.fromJson(json, CommandModel.class);
        this.logger.trace("JSON: " + json);
        Server.ServerData.INSTANCE.putCommand(commandModel);
        if (commandModel.getCommand() == Command.QUIT) {
          Thread.currentThread().interrupt();
        }
      } catch (IOException | InvalidBytes e) {
        this.logger.error(e);
        return;
      }
    }
  }
}
