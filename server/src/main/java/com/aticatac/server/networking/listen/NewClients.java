package com.aticatac.server.networking.listen;

import com.aticatac.common.model.Command;
import com.aticatac.common.model.CommandModel;
import com.aticatac.server.networking.Client;
import com.aticatac.server.networking.Data;
import com.aticatac.server.networking.authentication.Authenticator;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type New clients.
 */
public class NewClients extends Thread {
    private final ConcurrentHashMap<String, Client> clients;
    private final BlockingQueue<CommandModel> requests;
    private final ServerSocket serverSocket;
    private final Logger logger;

    /**
     * Instantiates a new New clients.
     *
     * @param clients  the clients
     * @param requests the requests
     * @throws IOException the io exception
     */
    public NewClients(ConcurrentHashMap<String, Client> clients, BlockingQueue<CommandModel> requests) throws IOException {
        this.clients = clients;
        this.serverSocket = new ServerSocket(Data.INSTANCE.getPort());
        this.logger = Logger.getLogger(getClass());
        this.requests = requests;
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            try {
                logger.info("Client count: " + this.clients.size());
                logger.trace("Waiting for client...");
                Socket client = this.serverSocket.accept();
                logger.trace("Client opened connection!");
                (new Thread(new Authenticator(client, this.clients, this.requests))).start();
            } catch (IOException e) {
                this.logger.error(e);
                this.logger.error("Failed to open connection with client.");
            }
        }
    }
}
