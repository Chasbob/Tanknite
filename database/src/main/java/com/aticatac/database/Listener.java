package com.aticatac.database;

import com.aticatac.common.GameResult;
import com.aticatac.common.model.DBlogin;
import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.ModelReader;
import com.aticatac.database.mappers.Player;
import com.google.common.eventbus.Subscribe;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.log4j.Logger;

import static com.aticatac.database.bus.EventBusFactory.eventBus;

public class Listener implements Runnable {
  private final ServerSocket serverSocket;
  private final Logger logger;
  private final ModelReader modelReader;
  private final DBinterface dBinterface;
  private boolean shutdown;

  public Listener() throws IOException {
    logger = Logger.getLogger(getClass());
    serverSocket = new ServerSocket(6000);
    shutdown = false;
    modelReader = new ModelReader();
    dBinterface = new DBinterface();
  }

  @Override
  public void run() {
    this.logger.info("Run!");
    while (!Thread.currentThread().isInterrupted() && !shutdown) {
      this.logger.info("running");
      try {
        Socket socket = serverSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintStream printer = new PrintStream(socket.getOutputStream());
        String json = reader.readLine();
        this.logger.info(json);
        DBlogin dBlogin = modelReader.fromJson(json, DBlogin.class);
        checkUser(dBlogin, printer);
        this.logger.info(dBlogin);
      } catch (InvalidBytes | IOException e) {
        this.logger.error(e);
        return;
      }
    }
  }

  private void checkUser(DBlogin dBlogin, PrintStream printer) {
    Optional<Player> op = dBinterface.getPlayer(dBlogin.getUsername());
    this.logger.info(op.toString());
    if (op.isPresent()) {
      Player p = op.get();
      if (p.getUsername().equals(dBlogin.getUsername()) && p.getPassword().equals(dBlogin.getPassword())) {
        printer.println(modelReader.toJson(p));
      } else {
        printer.println(modelReader.toJson(dBlogin));
      }
    } else {
      try {
        Player newPlayer = registerUser(dBlogin);
        printer.println(modelReader.toJson(newPlayer));
      } catch (PersistenceException e) {
        this.logger.error(e);
        printer.println(modelReader.toJson(dBlogin));
      }
    }
  }
  private Player registerUser(DBlogin dBlogin) throws PersistenceException {
    this.logger.info("registering player...");
    this.logger.info(dBlogin.toString());
    Player player = new Player(dBlogin.getUsername(), dBlogin.getPassword());
    return dBinterface.addPlayer(player);
  }

  public void shutdown() {
    this.logger.info("Shutdown!");
    shutdown = true;
    try {
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Subscribe
  private void processGame(GameResult result) {
    for (String username : result.getKd().keySet()) {
      Player player = new Player();
      player.setUsername(username);
      player.applyKillDeath(result.getKd().get(username));
      if (result.getWinners().contains(username)) {
        player.addWin();
      }
    }
  }
}

