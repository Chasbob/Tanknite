package com.aticatac.database;

import com.aticatac.common.model.DBlogin;
import com.aticatac.common.model.ModelReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import org.apache.log4j.Logger;

public class Client {
  public static void main(String[] args) {
    Logger logger = Logger.getLogger(Client.class);
    try {
      ModelReader modelReader = new ModelReader();
      Socket socket = new Socket("chasbob.co.uk", 6000);
      DBlogin dBlogin = new DBlogin("charlie", "charlie");
      String json = modelReader.toJson(dBlogin);
      logger.info(json);
      BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintStream printer = new PrintStream(socket.getOutputStream());
      printer.println(json);
      logger.info("wrote to stream");
      String json2 = reader.readLine();
      logger.info(json2);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
