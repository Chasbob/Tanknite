package com.aticatac.client.networking;

import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.Updates.Update;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;

/**
 * The type Update listener.
 */
class UpdateListener extends Thread {
  //    final BlockingQueue<Update> updates;
  private final MulticastSocket multicastSocket;
  private final Logger logger;
  private final ConcurrentLinkedQueue<Update> queue;

  /**
   * Instantiates a new Update listener.
   *
   * @param multicastSocket the multicast socket
   * @param queue           the queue
   */
  UpdateListener(MulticastSocket multicastSocket, ConcurrentLinkedQueue<Update> queue) {
    this.logger = Logger.getLogger(getClass());
    this.multicastSocket = multicastSocket;
    this.queue = queue;
  }

  @Override
  public void run() {
    logger.trace("Running...");
    super.run();
    while (!this.isInterrupted()) {
      try {
        listen();
      } catch (IOException e) {
        logger.error(e);
      } catch (InvalidBytes invalidBytes) {
        invalidBytes.printStackTrace();
      }
    }
  }

  private void listen() throws IOException, InvalidBytes {
    logger.trace("Listening...");
    byte[] bytes = new byte[8000];
    DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
    this.multicastSocket.receive(packet);
    logger.trace("Packet received!");
    Update update = ModelReader.toModel(bytes, Update.class);
    //TODO refactor to use queue all the way down
    this.logger.trace("Player count: " + update.getPlayers().size());
    if (update.isChanged()) {
      if (this.queue.size() > 5) {
        this.queue.clear();
      }
      this.queue.add(update);
      this.logger.trace("added update to queue.");
    }
  }

  /**
   * Quit.
   */
  public void quit() {
    multicastSocket.close();
    this.interrupt();
  }
}
