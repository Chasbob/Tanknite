package com.aticatac.server.networking;

import com.aticatac.common.CommonData;
import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.ServerInformation;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InterfaceAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Discovery.
 */
public class Discovery extends Thread {
    private final List<DatagramPacket> packets;
    private final Logger logger;
    private final int port;
    private final DatagramSocket socket;

    /**
     * Instantiates a new Discovery.
     *
     * @param id the id
     */
    Discovery(String id, int port) throws IOException {
        this.packets = buildPackets(id, Data.INSTANCE.getInterfaces());
        this.logger = Logger.getLogger(Discovery.class);
        this.port = port;
        this.socket = new DatagramSocket();
    }

    private List<DatagramPacket> buildPackets(String id, List<InterfaceAddress> interfaces) throws IOException {
        List<DatagramPacket> output = new ArrayList<>();
        for (InterfaceAddress current : interfaces) {
            if (current.getBroadcast() == null) {
                continue;
            }
            ServerInformation information = new ServerInformation(id, current.getAddress(), Data.INSTANCE.getPort());
            byte[] bytes = ModelReader.toBytes(information);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, current.getBroadcast(), CommonData.INSTANCE.getDiscoveryPort());
            output.add(packet);
        }
        return output;
    }

    @Override
    public void run() {
        this.logger.trace("Running...");
        while (!this.isInterrupted()) {
            try {
                Thread.sleep(5000);
                broadcast();
            } catch (InterruptedException | IOException ignored) {
            }
        }
    }

    private void broadcast() throws IOException {
        for (DatagramPacket packet : this.packets) {
            socket.send(packet);
            logger.trace("Sent packet to: " + packet.getAddress());
        }
    }
}
