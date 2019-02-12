package com.aticatac.server.networking.authentication;

import com.aticatac.common.Constant;
import com.aticatac.common.model.*;
import com.aticatac.server.networking.Client;
import com.aticatac.server.networking.listen.CommandListener;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Authenticator.
 */
public class Authenticator implements Runnable {
    private final Logger logger;
    private final Socket client;
    private final ConcurrentHashMap<String, Client> clients;
    private final BlockingQueue<CommandModel> queue;
    private boolean authenticated;

    /**
     * Instantiates a new Authenticator.
     *
     * @param client  the client
     * @param clients the clients
     * @param queue   the queue
     */
    Authenticator(Socket client, ConcurrentHashMap<String, Client> clients, BlockingQueue<CommandModel> queue) {
        this.client = client;
        this.clients = clients;
        this.queue = queue;
        logger = Logger.getLogger(getClass());
        this.authenticated = false;
    }

    @Override
    public void run() {
        while (!this.authenticated) {
            try {
                Login login = getLoginRequest();
                if (clientExists(login)) {
                    this.logger.warn("Client already exists... Rejecting...");
                    reject(login);
                } else {
                    accept(login);
                    this.authenticated = true;
                }
            } catch (IOException e) {
                this.logger.error(e);
                this.logger.error("Authentication failed due to IO exception.");
            }
        }
    }

    private Login getLoginRequest() throws IOException {
        return expectModel(this.client, Login.class);
    }

    private boolean clientExists(Login clientModel) {
        //TODO add additional authenticationOLD for existing users.
        // also this is where security layers could be added.
        return clients.containsKey(clientModel.getId());
    }

    private void reject(Login login) throws IOException {
        this.logger.trace("Rejecting client...");
        OutputStream out = this.client.getOutputStream();
        login.setAuthenticated(false);
        byte[] bytes = ModelReader.toBytes(login);
        out.write(bytes);
        out.flush();
        this.client.shutdownOutput();
    }

    private void accept(Login login) throws IOException {
        this.logger.trace("Accepting client...");
        login.setAuthenticated(true);
        //TODO add correct map id
        login.setMapID(1);
//        InetAddress multicast = InetAddress.getByName(Constant.getMulticast());
        login.setMulticast(Constant.getMulticast());
        logger.info("Setting multicast address: " + login.getMulticast());
        OutputStream out = this.client.getOutputStream();
//        Logger.getLogger(ModelReader.class).setLevel(Level.TRACE);
        byte[] bytes = ModelReader.toBytes(login);
//        Logger.getLogger(ModelReader.class).setLevel(Level.FATAL);
        out.write(bytes);
        out.flush();
        this.client.shutdownOutput();
        addClient(login);
        this.logger.fatal("Client: " + login.getId() + " accepted.");
    }

    private <T extends Model> T expectModel(Socket socket, Class<T> type) throws IOException {
        this.logger.trace("Expecting " + type.getCanonicalName() + " from socket.");
        InputStream in = socket.getInputStream();
        byte[] fromClient = in.readAllBytes();
        this.logger.trace("Client sent " + fromClient.length + " bytes.");
        return ModelReader.toModel(fromClient, type);
    }

    private void addClient(Login login) {
        CommandListener listener = new CommandListener(this.client, this.queue);
        ClientModel model = new ClientModel(login.getId());
        Client client = new Client(listener, model);
        this.clients.put(model.getId(), client);
    }
}
