package com.aticatac.client.networking;

import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.ServerInformation;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Callable;
import org.apache.log4j.Logger;

/**
 * The type Broadcast listener.
 */
class BroadcastListener implements Callable<ServerInformation> {
  private final ModelReader modelReader;
  private final Logger logger;
  private DatagramSocket socket;

  /**
   * Instantiates a new Broadcast listener.
   *
   * @param socket the socket
   */
  public BroadcastListener(DatagramSocket socket) {
    logger = Logger.getLogger(getClass());
    this.socket = socket;
    modelReader = new ModelReader();
  }

  @Override
  public ServerInformation call() throws Exception {
    return listen();
  }

  private ServerInformation listen() throws IOException, InvalidBytes {
    byte[] bytes = new byte[1000];
    DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
    socket.receive(packet);
    ServerInformation info = modelReader.toModel(bytes, ServerInformation.class);
    this.logger.trace(info);
    return info;
  }
}
