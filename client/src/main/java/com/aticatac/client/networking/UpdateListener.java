package com.aticatac.client.networking;

import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.Updates.Update;
import java.io.BufferedReader;
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
  private final BufferedReader reader;

  /**
   * Instantiates a new Update listener.
   *
   * @param multicastSocket the multicast socket
   * @param queue           the queue
   * @param reader          the reader
   */
  UpdateListener(MulticastSocket multicastSocket, ConcurrentLinkedQueue<Update> queue, BufferedReader reader) {
    this.logger = Logger.getLogger(getClass());
    this.multicastSocket = multicastSocket;
    this.queue = queue;
    this.reader = reader;
  }

  @Override
  public void run() {
    logger.trace("Running...");
    super.run();
    while (!this.isInterrupted()) {
      try {
//        listen();
        if (this.queue.size() > 5) {
          this.queue.clear();
        }
        tcpListen();
      } catch (IOException e) {
        logger.error(e);
      } catch (InvalidBytes invalidBytes) {
        invalidBytes.printStackTrace();
      }
    }
  }

  private void tcpListen() throws IOException, InvalidBytes {
    logger.trace("Listening...");
    String json = this.reader.readLine();
    Update update = ModelReader.fromJson(json, Update.class);
    this.queue.add(update);
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
      //todo figure out what to do instead of clearing.
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
