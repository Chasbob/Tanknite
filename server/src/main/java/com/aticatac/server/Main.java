package com.aticatac.server;

import com.aticatac.common.model.Command;
import com.aticatac.common.model.CommandModel;
import com.aticatac.server.networking.Server;
import java.util.Arrays;
import java.util.Scanner;
import org.apache.log4j.Logger;

public class Main {
  public static void main(String[] args) {
    Server server;
    Logger logger = Logger.getLogger(Main.class);
    server = new Server(false, "Server Main", "admin");
    server.start();
    Scanner scanner = new Scanner(System.in);
    while (true) {
      String[] input = scanner.nextLine().split(" ");
      try {
        commmand commmand = Main.commmand.valueOf(input[0]);
        System.out.println(Arrays.toString(input));
        switch (commmand) {
          case list:
            System.out.println(Server.ServerData.INSTANCE.getGame().getPlayerMap().values());
            break;
          case kill:
            Server.ServerData.INSTANCE.getGame().getPlayerMap().get(input[1]).hit(100000);
            break;
          case move:
//            Server.ServerData.INSTANCE.getGame().getPlayerMap().get(input[1]).move(Integer.valueOf(input[2]), Integer.valueOf(input[3]), false);
            break;
          case start:
            Server.ServerData.INSTANCE.putCommand(new CommandModel("admin", Command.START));
            break;
        }
      } catch (Exception e) {
        System.out.println("invalid command");
      }
    }
  }

  enum commmand {
    list, kill, move, start
  }
}
