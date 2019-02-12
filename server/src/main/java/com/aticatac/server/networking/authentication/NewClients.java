package com.aticatac.server.networking.authentication;

import com.aticatac.common.Constant;
import com.aticatac.common.model.CommandModel;
import com.aticatac.server.networking.Client;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class NewClients extends Thread {
    private final ConcurrentHashMap<String, Client> clients;
    private final BlockingQueue<CommandModel> requests;
    private final ServerSocket serverSocket;
    private final Logger logger;

    public NewClients(ConcurrentHashMap<String, Client> clients, BlockingQueue<CommandModel> requests) throws IOException {
        this.clients = clients;
        this.serverSocket = new ServerSocket(Constant.getPort());
        this.logger = Logger.getLogger(getClass());
        this.requests = requests;
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            try {
                logger.fatal("Client count: " + this.clients.size());
                logger.trace("Waiting for client...");
                Socket client = this.serverSocket.accept();
                logger.info("Client connected!");
                (new Thread(new Authenticator(client, this.clients, this.requests))).start();
            } catch (IOException e) {
                this.logger.error(e);
                this.logger.error("Failed to open connection with client.");
            }
        }
    }
}
