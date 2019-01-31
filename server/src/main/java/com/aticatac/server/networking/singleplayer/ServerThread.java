package com.aticatac.server.networking.singleplayer;

import com.aticatac.common.model.Command;
import com.aticatac.common.model.Model;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * The type Server thread.
 */
public class ServerThread extends Thread {
    private final DatagramSocket serverSocket;
    private final Logger logger;

    /**
     * Instantiates a new Server thread.
     *
     * @param serverSocket the server socket
     */
    ServerThread(DatagramSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.logger = Logger.getLogger(ServerThread.class);
    }

    @Override
    public void run() {
        logger.info("ServerThread running");
        listen();
    }

    private void listen() {
        // For tracking a change in commands, for more useful logging.
        Command previous = Command.UPDATE;
        while (!this.isInterrupted()) {
            byte[] packetBytes = new byte[8000];
            DatagramPacket receivePacket = new DatagramPacket(packetBytes, packetBytes.length);
            try {
                this.serverSocket.receive(receivePacket);
                byte[] lengthBytes = Arrays.copyOfRange(packetBytes, 0, 4);
                ByteBuffer wrapped = ByteBuffer.wrap(lengthBytes);
                int length = wrapped.getInt();
                byte[] modelBytes = Arrays.copyOfRange(packetBytes, 4, length + 4);
                ByteArrayInputStream baos = new ByteArrayInputStream(modelBytes);
                ObjectInputStream oos = new ObjectInputStream(baos);
                Model model = (Model) oos.readObject();
                if (model.getCommand() != previous) logger.info(model.getCommand().toString());
                previous = model.getCommand();
                if (!placeHolder(model.getCommand())) {
                    model.setCommand(Command.SHOOT);
                }
                if (placeHolder(model.getCommand(), model.getId())) {
                    sendModel(model, receivePacket);
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.error(e.getMessage());
                System.exit(1);
            }
        }
    }

    private boolean placeHolder(Command command, String id) {
        //TODO implement with logic
//       return checkLogic(command);
        return true;
    }

    private void sendModel(Model model, DatagramPacket client) {
        try {
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
            oos.writeObject(model);
            oos.flush();
            byte[] buf = baos.toByteArray();
            byte[] len = ByteBuffer.allocate(4).putInt(buf.length).array();
            byte[] out = org.apache.commons.lang3.ArrayUtils.addAll(len, buf);
            DatagramPacket packetSize = new DatagramPacket(out, out.length, client.getAddress(), client.getPort());
            this.serverSocket.send(packetSize);
            //TODO handel IOException for DatagramPacket
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
