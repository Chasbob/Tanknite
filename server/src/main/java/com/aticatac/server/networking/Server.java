package com.aticatac.server.networking;

import com.aticatac.common.model.CommandModel;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.common.objectsystem.Converter;
import com.aticatac.server.gameManager.Manager;
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
public class Server extends Thread {
    private final Logger logger;
    private final ConcurrentHashMap<String, Client> clients;
    private final BlockingQueue<CommandModel> requests;
    private final NewClients newClients;
    private final Updater multicaster;
    private final Discovery discovery;

    /**
     * Instantiates a new Server.
     *
     * @throws IOException the io exception
     */
    public Server() throws IOException {
        //TODO check if additional users are allowed.
        this.logger = Logger.getLogger(getClass());
        this.clients = new ConcurrentHashMap<>();
        this.requests = new ArrayBlockingQueue<>(1024); //TODO select an appropriate queue size.
        newClients = new NewClients(this.clients, this.requests);
        multicaster = new Updater(Data.INSTANCE.getMulticast(), this.clients);
        discovery = new Discovery("Server", Data.INSTANCE.getPort());
    }

    public ConcurrentHashMap<String, Client> getClients() {
        return clients;
    }

    @Override
    public void run() {
        this.logger.trace("Running...");
        this.multicaster.start();
        this.newClients.start();
        this.discovery.start();
        (new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep((long) 16.66666666);
                    this.multicaster.addObject(Converter.Deconstructor(Manager.INSTANCE.getRoot()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        })).start();
        (new Thread(() -> {
            //TODO remove testing thread
            while (true) {
                try {
//                    System.out.println(this.requests.take());
                    CommandModel model = this.requests.take();
                    Manager.INSTANCE.playerInput(model.getId(), model.getCommand());
                } catch (InterruptedException ignored) {
                }
            }
        })).start();
        while (!this.isInterrupted()) {
            try {
                Thread.sleep(5000);
//                System.out.println("There are: " + this.clients.size() + " clients.");
                this.logger.info("There are: " + this.requests.size() + " requests in the queue.");
            } catch (InterruptedException e) {
                this.multicaster.interrupt();
                this.newClients.interrupt();
                this.discovery.interrupt();
                this.interrupt();
                return;
            }
        }
        this.logger.warn("Interrupted");
    }

    public synchronized void setUpdateModel(Update update) {
        //TODO optimise setting new model by calculating a differential
        // as to send less data.
        this.multicaster.setUpdateModel(update);
    }

    /**
     * Next command command.
     *
     * @return the command
     *
     * @throws InterruptedException the interrupted exception
     */
    public CommandModel nextCommand() throws InterruptedException {
        this.logger.warn("Taking command");
        return this.requests.take();
    }
}
