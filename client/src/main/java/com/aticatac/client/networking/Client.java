package com.aticatac.client.networking;

import com.aticatac.common.Constant;
import com.aticatac.common.model.Command;
import com.aticatac.common.model.CommandModel;
import com.aticatac.common.model.Login;
import com.aticatac.common.model.ModelReader;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

/**
 * The type Client.
 */
class Client {
    private final Logger logger;
    private final Login login;
    private Socket commandSocket;
    private MulticastSocket multicastSocket;
    private UpdateListener updateListener;

    /**
     * Instantiates a new Client.
     *
     * @param id the id
     */
    Client(String id) {
        this.logger = Logger.getLogger(getClass());
        Logger.getLogger(getClass()).setLevel(Level.TRACE);
        login = new Login(id);
    }

    /**
     * Connect.
     *
     * @param serverAddress the server address
     * @throws IOException the io exception
     */
    void connect(String serverAddress) throws IOException {
        logger.info("Connecting...");
        Socket socket = new Socket(serverAddress, Constant.getPort());
//            socket = new Socket(InetAddress.getByName("server.lan"), Constant.getAuthPort());
        logger.info("Connected to server at " + socket.getInetAddress());
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();
        byte[] bytes = ModelReader.toBytes(this.login);
        logger.info("Writing " + bytes.length + " bytes...");
        out.write(bytes);
        logger.info("Closing output stream.");
        socket.shutdownOutput();
        logger.info("Waiting for response...");
        byte[] response = in.readAllBytes();
        logger.info("Server responded with " + response.length + " bytes.");
        Logger.getLogger(ModelReader.class).setLevel(Level.TRACE);
        Login output = ModelReader.toModel(response, Login.class);
        Logger.getLogger(ModelReader.class).setLevel(Level.FATAL);
        logger.info("Authenticated = " + output.isAuthenticated());
//        this.login.setAuthenticated(output.isAuthenticated());
        if (output.isAuthenticated()) {
//        initCommandSocket(serverAddress);
            this.logger.info("Multicast address: " + output.getMulticast());
            initUpdateSocket(InetAddress.getByName(output.getMulticast()));
        }
        logger.info("Exiting 'connect' cleanly.");
    }

    private void initUpdateSocket(InetAddress address) throws IOException {
        logger.info("Initialising update socket...");
        logger.info("Joining multicast " + address);
        this.multicastSocket = new MulticastSocket(Constant.getPort());
        this.multicastSocket.joinGroup(address);
        this.updateListener = new UpdateListener(this.multicastSocket);
        this.updateListener.start();
        logger.info("Started update listener!");
    }

    private void initCommandSocket(InetAddress address) throws IOException {
        logger.info("Initialising command socket...");
        logger.info(address.toString() + ":" + Constant.getPort());
        this.commandSocket = new Socket(address, Constant.getPort());
        logger.info("Command socket connected!");
    }

    /**
     * Send command.
     *
     * @param command the command
     */
    public void sendCommand(Command command) {
        CommandModel commandModel = new CommandModel(this.login.getId(), command);
        //TODO implement sending commands
    }
}
