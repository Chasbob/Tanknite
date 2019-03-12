package com.aticatac.client.networking;

import com.aticatac.common.CommonData;
import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.ServerInformation;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Callable;

/**
 * The type Broadcast listener.
 */
class BroadcastListener implements Callable<ServerInformation> {
  private DatagramSocket socket;

  public BroadcastListener(DatagramSocket socket) {
    this.socket = socket;
  }

  @Override
  public ServerInformation call() throws Exception {
    return listen();
  }

  private ServerInformation listen() throws IOException, InvalidBytes {

    byte[] bytes = new byte[1000];
    DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
    socket.receive(packet);
    return ModelReader.toModel(bytes, ServerInformation.class);
  }
}
