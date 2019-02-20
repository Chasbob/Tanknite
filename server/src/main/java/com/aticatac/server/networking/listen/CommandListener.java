package com.aticatac.server.networking.listen;

import com.aticatac.common.model.Command;
import com.aticatac.common.model.CommandModel;
import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.ModelReader;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class CommandListener extends Thread {
    private final BlockingQueue<CommandModel> queue;
    private final Logger logger;
    private final BufferedReader reader;

    public CommandListener(BufferedReader reader, BlockingQueue<CommandModel> queue) {
        this.queue = queue;
        this.logger = Logger.getLogger(getClass());
        this.reader = reader;
    }

    @Override
    public void run() {
        super.run();
        listen();
    }

    private void listen() {
        this.logger.trace("Listening");
        while (!this.isInterrupted()) {
            try {
                while (!this.isInterrupted()) {
                    String json = this.reader.readLine();
                    CommandModel commandModel = ModelReader.fromJson(json, CommandModel.class);
                    this.logger.trace("JSON: " + json);
                    this.queue.add(commandModel);
                }
            } catch (IOException | InvalidBytes e) {
                logger.error(e);
            }
        }
    }
}
