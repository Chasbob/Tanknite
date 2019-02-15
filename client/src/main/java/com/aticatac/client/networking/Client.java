package com.aticatac.client.networking;

import com.aticatac.common.model.*;
import com.aticatac.common.model.Exception.InvalidBytes;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

/**
 * The type Client.
 */
public class Client {
    private final Logger logger;
    private Login login;
    private PrintStream printer;

    /**
     * Instantiates a new Client.
     */
    public Client() {
        this.logger = Logger.getLogger(getClass());
//        login = new Login(id);
    }

    /**
     * Connect.
     *
     * @param server the server
     * @param id     the id
     * @throws IOException  the io exception
     * @throws InvalidBytes the invalid bytes
     */
    public boolean connect(ServerInformation server, String id) throws IOException, InvalidBytes {
        boolean boolOut = false;
        Login login = new Login(id);
        this.logger.trace("ID: " + id);
        this.logger.trace("login: " + ModelReader.toJson(login));
        this.logger.trace("Trying to connect to: " + server.getAddress() + ":" + server.getPort());
        Socket socket = new Socket(server.getAddress(), server.getPort());
        this.logger.trace("Connected to server at " + socket.getInetAddress());
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.printer = new PrintStream(socket.getOutputStream());
        this.printer.println(ModelReader.toJson(login));
        String json = reader.readLine();
        this.logger.trace("Waiting for response...");
        Login output = ModelReader.fromJson(json, Login.class);
        this.logger.trace("Authenticated = " + output.isAuthenticated());
        if (output.isAuthenticated()) {
            this.logger.trace("Multicast address: " + output.getMulticast());
            initUpdateSocket(InetAddress.getByName(output.getMulticast()), server.getPort());
            boolOut = true;
        }
        this.login = output;
        this.logger.info("Exiting 'connect' cleanly.");
        return boolOut;
    }

    private void initUpdateSocket(InetAddress address, int port) throws IOException {
        this.logger.trace("Initialising update socket...");
        this.logger.trace("Joining multicast: " + address + ":" + port);
        MulticastSocket multicastSocket = new MulticastSocket(port);
        multicastSocket.joinGroup(address);
        UpdateListener updateListener = new UpdateListener(multicastSocket);
        updateListener.start();
        this.logger.trace("Started update listener!");
    }

    /**
     * Send command.
     *
     * @param command the command
     */
    void sendCommand(Command command) {
        if (this.login == null) {
            return;
        }
        this.logger.trace("Sending command: " + command);
        CommandModel commandModel = new CommandModel(this.login.getId(), command);
        this.logger.trace("Writing command to output stream.");
        String json = ModelReader.toJson(commandModel);
        this.printer.println(json);
        this.logger.trace("Sent command: " + command);
    }
}
