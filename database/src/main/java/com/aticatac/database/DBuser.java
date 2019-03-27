package com.aticatac.database;

import com.aticatac.common.mappers.Player;
import com.aticatac.common.model.DBResponse;
import com.aticatac.common.model.DBlogin;
import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.LobbyPlayers;
import com.aticatac.common.model.ModelReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Optional;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.log4j.Logger;

public class DBuser extends Thread {
  private final BufferedReader reader;
  private final PrintStream printer;
  private final Logger logger;
  private final DBinterface dBinterface;
  private final ModelReader modelReader;
  private boolean run;

  public DBuser(final Socket socket) throws IOException {
    logger = Logger.getLogger(getClass());
    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    printer = new PrintStream(socket.getOutputStream());
    this.run = true;
    modelReader = new ModelReader();
    dBinterface = new DBinterface();
  }

  public void shutdown() {
    this.run = false;
    try {
      reader.close();
      printer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void checkUser(DBlogin dBlogin) {
    try {
      Optional<Player> op = dBinterface.getPlayer(dBlogin.getUsername());
      this.logger.info(op.toString());
      if (op.isPresent()) {
        this.logger.info("player exists");
        if (dBlogin.isRegister()) {
          this.logger.info("cant register a taken name");
          this.printer.println(modelReader.toJson(new DBResponse(DBResponse.Response.username_taken)));
          return;
        }
        Player p = op.get();
        if (p.username.equals(dBlogin.getUsername()) && p.password.equals(dBlogin.getPassword())) {
          this.logger.info("name and password match");
          this.printer.println(modelReader.toJson(new DBResponse(p, DBResponse.Response.accepted)));
          this.logger.info(p);
        } else {
          this.logger.info("password is wrong");
          this.printer.println(modelReader.toJson(new DBResponse(DBResponse.Response.wrong_password)));
        }
      } else {
        if (dBlogin.isRegister()) {
          this.logger.info("registering");
          try {
            Player newPlayer = registerUser(dBlogin);
            this.logger.info("registered");
            this.printer.println(modelReader.toJson(new DBResponse(newPlayer, DBResponse.Response.accepted)));
          } catch (PersistenceException e) {
            this.logger.info("name taken");
            this.printer.println(modelReader.toJson(new DBResponse(DBResponse.Response.username_taken)));
          }
        } else {
          this.printer.println(modelReader.toJson(new DBResponse(DBResponse.Response.no_user)));
        }
      }
    } catch (PersistenceException e) {
      this.logger.error(e);
      this.printer.println(modelReader.toJson(dBlogin));
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

  @Override
  public void run() {
    try {
      String json = reader.readLine();
      this.logger.info(json);
      DBlogin dBlogin = modelReader.fromJson(json, DBlogin.class);
      checkUser(dBlogin);
    } catch (IOException | InvalidBytes e) {
      try {
        printer.close();
        reader.close();
      } catch (IOException e1) {
        return;
      }
      return;
    }
    while (!this.isInterrupted() && run) {
      try {
        String json = reader.readLine();
        if (json.contains("LobbyPlayers")) {
          LobbyPlayers lobbyPlayers = modelReader.fromJson(json, LobbyPlayers.class);
          HashMap<String, Player> players = dBinterface.getLobby(lobbyPlayers.getNames());
          lobbyPlayers.setPlayers(players);
          printer.println(modelReader.toJson(lobbyPlayers));
        }
      } catch (IOException | InvalidBytes e) {
        e.printStackTrace();
      }
    }
  }
}
