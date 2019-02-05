package com.aticatac.client.networking.singleplayer;

import com.aticatac.common.model.Command;
import com.aticatac.common.model.GeneralModel;
import com.aticatac.common.model.ModelReader;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.*;

/**
 * The type ClientModel.
 */
public class Client extends Thread {
    private final String id;
    private final Logger logger;
    private final DatagramSocket socket;
    private final int port;
    private final InetAddress address;
    private GeneralModel generalModel;

    /**
     * Instantiates a new ClientModel.
     *
     * @param id      the id
     * @param address the address
     * @param port    the port
     *
     * @throws UnknownHostException the unknown host exception
     * @throws SocketException      the socket exception
     */
    public Client(String id, String address, int port) throws UnknownHostException, SocketException {
        this.id = id;
        this.generalModel = new GeneralModel(id);
        this.logger = Logger.getLogger(getClass());
        logger.info("Starting");
        this.socket = new DatagramSocket();
        this.port = port;
        this.address = InetAddress.getByName(address);
    }

    /**
     * Gets generalModel.
     *
     * @return the generalModel
     */
    public GeneralModel getGeneralModel() {
        return generalModel;
    }

    /**
     * Can change boolean.
     *
     * @return the boolean
     */
    public Boolean canChange() {
        return true;
    }

    /**
     * Sets command.
     *
     * @param command the command
     */
    public void setCommand(Command command) {
        logger.info("Setting command to: " + command.toString());
        this.generalModel.setCommand(command);
    }

    @Override
    public void run() {
        sendModel();
    }

    private void sendModel() {
        while (!this.isInterrupted()) {
            try {
                byte[] out = ModelReader.getByteArray(this.generalModel);
                DatagramPacket packetSize = new DatagramPacket(out, out.length, this.address, this.port);
                this.socket.send(packetSize);
                //TODO handel IOException for DatagramPacket
            } catch (IOException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
