package com.aticatac.server.networking;

import com.aticatac.common.Constant;
import com.aticatac.common.model.CommandModel;
import com.aticatac.common.model.ModelReader;
import com.aticatac.server.networking.authentication.NewClients;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Server.
 *
 * @author Charles de Freitas
 */
class Server implements Runnable {
    private final Logger logger;
    private final ConcurrentHashMap<String, Client> clients;
    private final BlockingQueue<CommandModel> requests;
    //    private final ServerSocket serverSocket;
    private final NewClients newClients;
    private final Updater multicaster;

    /**
     * Instantiates a new Server.
     *
     * @throws IOException the io exception
     */
    Server() throws IOException {
        this.logger = Logger.getLogger(getClass());
        this.clients = new ConcurrentHashMap<>();
        this.requests = new ArrayBlockingQueue<>(1024);
        newClients = new NewClients(this.clients, this.requests);
        multicaster = new Updater(InetAddress.getByName(Constant.getMulticast()));
        Logger.getLogger(ModelReader.class).setLevel(Level.FATAL);
    }

    @Override
    public void run() {
        this.logger.trace("Running...");
        this.multicaster.start();
        this.newClients.start();
        //TODO start broadcasting
        while (true) {
            try {
                Thread.sleep(1000);
                System.out.println("There are: " + this.clients.size() + " clients.");
            } catch (InterruptedException e) {
//                e.printStackTrace();
            }
        }
    }
}
