package com.aticatac.database;

import com.aticatac.common.model.DBinfo;
import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.ModelReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.Callable;
import org.apache.log4j.Logger;

public class DiscoveryListener implements Callable<DBinfo> {
  private final ModelReader modelReader;
  private final Logger logger;
  private DatagramSocket socket;

  public DiscoveryListener() throws SocketException {
    logger = Logger.getLogger(getClass());
    this.socket = new DatagramSocket(6000);
    modelReader = new ModelReader();
  }

  @Override
  public DBinfo call() throws Exception {
    return listen();
  }

  private DBinfo listen() throws IOException, InvalidBytes {
    this.logger.info("listening");
    byte[] bytes = new byte[1000];
    DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
    socket.receive(packet);
    this.logger.info("got packet!");
    return modelReader.toModel(bytes, DBinfo.class);
  }
}
