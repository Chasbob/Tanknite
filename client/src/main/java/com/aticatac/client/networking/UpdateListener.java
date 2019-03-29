package com.aticatac.client.networking;

import com.aticatac.common.Stoppable;
import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.Shutdown;
import com.aticatac.common.model.Updates.Update;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.MulticastSocket;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;

import static com.aticatac.client.bus.EventBusFactory.eventBus;

/**
 * The type Update listener.
 */
class UpdateListener extends Thread implements Stoppable {
  //    final BlockingQueue<Update> updates;
  private final MulticastSocket multicastSocket;
  private final Logger logger;
  private final ConcurrentLinkedQueue<Update> queue;
  private final BufferedReader reader;
  private final ModelReader modelReader;
  private boolean run;

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
    this.modelReader = new ModelReader();
    this.reader = reader;
  }

  /**
   * Instantiates a new Update listener.
   *
   * @param queue  the queue
   * @param reader the reader
   */
  UpdateListener(ConcurrentLinkedQueue<Update> queue, BufferedReader reader) {
    this.logger = Logger.getLogger(getClass());
    try {
      this.multicastSocket = new MulticastSocket();
    } catch (IOException e) {
      this.logger.error(e);
      throw new ExceptionInInitializerError(e);
    }
    this.queue = queue;
    this.modelReader = new ModelReader();
    this.reader = reader;
  }

  @Override
  public void run() {
    logger.trace("Running...");
    this.run = true;
    while (!this.isInterrupted() && run) {
      double nanoTime = System.nanoTime();
      try {
        if (this.queue.size() > 5) {
          this.queue.clear();
        }
        tcpListen();
      } catch (IOException e) {
        eventBus.post(new Shutdown());
        return;
      } catch (InvalidBytes ignored) {
      }
      while (System.nanoTime() - nanoTime < 1000000000 / 60) {
        try {
          Thread.sleep(0);
        } catch (InterruptedException ignored) {
        }
      }
    }
  }

  private void tcpListen() throws IOException, InvalidBytes {
    logger.trace("Listening...");
    String json = this.reader.readLine();
    Update update = modelReader.fromJson(json, Update.class);
    this.queue.add(update);
  }

  @Override
  public void shutdown() {
    multicastSocket.close();
    this.run = false;
    this.queue.clear();
    try {
      this.reader.close();
    } catch (IOException ignored) {
    }
  }
}
