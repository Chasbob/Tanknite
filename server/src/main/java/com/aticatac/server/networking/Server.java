package com.aticatac.server.networking;

import com.aticatac.common.model.Command;
import com.aticatac.server.networking.listen.NewClients;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Server.
 *
 * @author Charles de Freitas
 */
class Server extends Thread {
    private final Logger logger;
    private final ConcurrentHashMap<String, Client> clients;
    private final BlockingQueue<Command> requests;
    private final NewClients newClients;
    private final Updater multicaster;
    private final Thread discovery;

    /**
     * Instantiates a new Server.
     *
     * @throws IOException the io exception
     */
    Server() throws IOException {
        this.logger = Logger.getLogger(getClass());
        this.clients = new ConcurrentHashMap<>();
        this.requests = new ArrayBlockingQueue<>(1024); //TODO select an appropriate queue size.
        newClients = new NewClients(this.clients, this.requests);
        multicaster = new Updater(Data.INSTANCE.getMulticast());
        discovery = new Discovery("Server", Data.INSTANCE.getPort());
    }

    @Override
    public void run() {
        this.logger.trace("Running...");
        this.multicaster.start();
        this.newClients.start();
        this.discovery.start();
//        (new Thread(() -> {
//            //TODO remove testing thread
//            while (true) {
//                try {
//                    this.requests.take();
//                } catch (InterruptedException ignored) {
//                }
//            }
//        })).start();
        while (!this.isInterrupted()) {
            try {
                Thread.sleep(5000);
//                System.out.println("There are: " + this.clients.size() + " clients.");
                this.logger.info("There are: " + this.requests.size() + " requests in the queue.");
            } catch (InterruptedException e) {
                this.logger.error(e);
            }
        }
    }

    /**
     * Next command command.
     *
     * @return the command
     * @throws InterruptedException the interrupted exception
     */
    public Command nextCommand() throws InterruptedException {
        return this.requests.take();
    }
}
