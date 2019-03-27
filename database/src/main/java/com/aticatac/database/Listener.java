package com.aticatac.database;

import com.aticatac.common.mappers.Player;
import com.aticatac.common.model.DBResponse;
import com.aticatac.common.model.DBlogin;
import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.ModelReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.log4j.Logger;

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
        this.logger.info("connected!");
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
    try {
      Optional<Player> op = dBinterface.getPlayer(dBlogin.getUsername());
      this.logger.info(op.toString());
      if (op.isPresent()) {
        this.logger.info("player exists");
        if (dBlogin.isRegister()) {
          this.logger.info("cant register a taken name");
          printer.println(modelReader.toJson(new DBResponse(DBResponse.Response.username_taken)));
          return;
        }
        Player p = op.get();
        if (p.username.equals(dBlogin.getUsername()) && p.password.equals(dBlogin.getPassword())) {
          this.logger.info("name and password match");
          printer.println(modelReader.toJson(new DBResponse(p, DBResponse.Response.accepted)));
          this.logger.info(p);
        } else {
          this.logger.info("password is wrong");
          printer.println(modelReader.toJson(new DBResponse(DBResponse.Response.wrong_password)));
        }
      } else {
        if (dBlogin.isRegister()) {
          this.logger.info("registering");
          try {
            Player newPlayer = registerUser(dBlogin);
            this.logger.info("registered");
            printer.println(modelReader.toJson(new DBResponse(newPlayer, DBResponse.Response.accepted)));
          } catch (PersistenceException e) {
            this.logger.info("name taken");
            printer.println(modelReader.toJson(new DBResponse(DBResponse.Response.username_taken)));
          }
        } else {
          printer.println(modelReader.toJson(new DBResponse(DBResponse.Response.no_user)));
        }
      }
    } catch (PersistenceException e) {
      this.logger.error(e);
      printer.println(modelReader.toJson(dBlogin));
    } catch (NullPointerException e) {
      this.logger.error(e);
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
}

