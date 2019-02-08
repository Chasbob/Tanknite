package com.aticatac.client.networking.singleplayer;

import com.aticatac.common.model.Command;
import com.aticatac.common.model.Model;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.ByteBuffer;

/**
 * The type Client.
 */
public class Client extends Thread {
    private final String id;
    private final Logger logger;
    private final DatagramSocket socket;
    private final int port;
    private final InetAddress address;
    private Model model;

    /**
     * Instantiates a new Client.
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
        this.model = new Model(id);
        this.logger = Logger.getLogger(getClass());
        logger.info("Starting");
        this.socket = new DatagramSocket();
        this.port = port;
        this.address = InetAddress.getByName(address);
    }

    /**
     * Gets model.
     *
     * @return the model
     */
    public Model getModel() {
        return model;
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
        this.model.setCommand(command);
    }

    @Override
    public void run() {
        sendModel();
    }

    private void sendModel() {
        while (!this.isInterrupted()) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(this.model);
                oos.flush();
                byte[] buf = baos.toByteArray();
                byte[] len = ByteBuffer.allocate(4).putInt(buf.length).array();
                byte[] out = ArrayUtils.addAll(len, buf);
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
