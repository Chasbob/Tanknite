package com.aticatac.server.networking;

import com.aticatac.common.CommonData;
import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.ServerInformation;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InterfaceAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Discovery.
 */
public class Discovery extends Thread {
    private final List<DatagramPacket> packets;
    private final Logger logger;
    private final int port;

    /**
     * Instantiates a new Discovery.
     *
     * @param id the id
     */
    Discovery(String id, int port) {
        this.packets = buildPackets(id, Data.INSTANCE.getInterfaces());
        this.logger = Logger.getLogger(Discovery.class);
        this.port = port;
    }

    private List<DatagramPacket> buildPackets(String id, List<InterfaceAddress> interfaces) {
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
        DatagramSocket socket = new DatagramSocket();
        for (DatagramPacket packet : this.packets) {
            socket.send(packet);
            logger.warn("Sent packet to: " + packet.getAddress());
        }
    }
}