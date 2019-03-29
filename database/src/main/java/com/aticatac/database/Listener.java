package com.aticatac.database;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class Listener implements Runnable {
  private final ServerSocket serverSocket;
  private final Logger logger;
  private boolean shutdown;
  private ArrayList<DBuser> clients;

  public Listener() throws IOException {
    logger = Logger.getLogger(getClass());
    serverSocket = new ServerSocket(6000);
    this.clients = new ArrayList<>();
    shutdown = false;
  }

  @Override
  public void run() {
    this.logger.info("Run!");
    while (!Thread.currentThread().isInterrupted() && !shutdown) {
      this.logger.info("running");
      try {
        Socket socket = serverSocket.accept();
        DBuser user = new DBuser(socket);
        user.start();
        clients.add(user);
//        this.logger.info("connected!");
//        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        PrintStream printer = new PrintStream(socket.getOutputStream());
//        String json = reader.readLine();
//        this.logger.info(json);
//        Object object = modelReader.objectJson(json);
//        try {
//          this.logger.info("is login request.");
//          DBlogin dBlogin = modelReader.fromJson(json, DBlogin.class);
//          checkUser(dBlogin, printer);
//          this.logger.info(dBlogin);
//        } catch (Exception e) {
//          try {
//            this.logger.info("lobby request");
//            LobbyPlayers lobbyPlayers = modelReader.fromJson(json, LobbyPlayers.class);
//            HashMap<String, Player> players = dBinterface.getLobby(lobbyPlayers.getNames());
//            lobbyPlayers.setPlayers(players);
//            printer.println(modelReader.toJson(lobbyPlayers));
//          } catch (Exception ignored) {
//          }
//        }
      } catch (IOException io) {
        System.exit(123);
      }
    }
//        if (object instanceof DBlogin) {
//
//        } else if (object instanceof LobbyPlayers) {
//
//        }
//      } catch (InvalidBytes | IOException e) {
//        this.logger.error(e);
//        return;
//      }
//    }
  }

  public void shutdown() {
    this.logger.info("Shutdown!");
    shutdown = true;
    try {
      serverSocket.close();
      for (DBuser user : clients) {
        user.shutdown();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

