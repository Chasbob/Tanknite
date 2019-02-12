package com.aticatac.server.networking.listen;

import com.aticatac.common.model.CommandModel;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class CommandListener extends Thread {
    private final Socket socket;
    private final BlockingQueue<CommandModel> queue;
    private final Logger logger;

    public CommandListener(Socket socket, BlockingQueue<CommandModel> queue) {
        this.socket = socket;
        this.queue = queue;
        logger = Logger.getLogger(getClass());
    }

    @Override
    public void run() {
        super.run();
        listen();
    }

    private void listen() {
        InputStream in;
        byte[] bytes;
        while (!this.isInterrupted()) {
            try {
                in = this.socket.getInputStream();
                while (!this.isInterrupted()) {
                    bytes = in.readAllBytes();
//                    CommandModel commandModel = ModelReader.toModel(bytes, CommandModel.class);
//                    this.queue.add(commandModel);
                }
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }
}
