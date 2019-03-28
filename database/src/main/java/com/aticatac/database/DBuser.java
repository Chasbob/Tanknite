package com.aticatac.database;

import com.aticatac.common.mappers.Player;
import com.aticatac.common.model.DBResponse;
import com.aticatac.common.model.DBlogin;
import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.Leaderboard;
import com.aticatac.common.model.LobbyPlayers;
import com.aticatac.common.model.ModelReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
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

  private boolean checkUser(DBlogin dBlogin) {
    try {
      Optional<Player> op = dBinterface.getPlayer(dBlogin.getUsername());
      this.logger.info(op.toString());
      if (op.isPresent()) {
        this.logger.info("player exists");
        if (dBlogin.isRegister()) {
          this.logger.info("cant register a taken name");
          this.printer.println(modelReader.toJson(new DBResponse(DBResponse.Response.username_taken)));
          return false;
        }
        Player p = op.get();
        if (p.username.equals(dBlogin.getUsername()) && p.password.equals(dBlogin.getPassword())) {
          this.logger.info("name and password match");
          this.printer.println(modelReader.toJson(new DBResponse(p, DBResponse.Response.accepted)));
          this.logger.info(p);
          return true;
        } else {
          this.logger.info("password is wrong");
          this.printer.println(modelReader.toJson(new DBResponse(DBResponse.Response.wrong_password)));
          return false;
        }
      } else {
        if (dBlogin.isRegister()) {
          this.logger.info("registering");
          try {
            Optional<Player> optnewPlayer = registerUser(dBlogin);
            if (optnewPlayer.isPresent()) {
              this.logger.info("registered");
              this.printer.println(modelReader.toJson(new DBResponse(optnewPlayer.get(), DBResponse.Response.accepted)));
              return true;
            } else {
              this.printer.println(modelReader.toJson(new DBResponse(DBResponse.Response.username_taken)));
              return false;
            }
          } catch (PersistenceException e) {
            this.logger.info("name taken");
            this.printer.println(modelReader.toJson(new DBResponse(DBResponse.Response.username_taken)));
            return false;
          }
        } else {
          this.printer.println(modelReader.toJson(new DBResponse(DBResponse.Response.no_user)));
          return false;
        }
      }
    } catch (PersistenceException e) {
      this.logger.error(e);
      this.printer.println(modelReader.toJson(dBlogin));
    } catch (NullPointerException e) {
      this.logger.error(e);
    }
    return false;
  }

  private Optional<Player> registerUser(DBlogin dBlogin) throws PersistenceException {
    this.logger.info("registering player...");
    this.logger.info(dBlogin.toString());
    Player player = new Player(dBlogin.getUsername(), dBlogin.getPassword());
    dBinterface.addPlayer(player);
    Optional<Player> out = dBinterface.getPlayer(dBlogin.getUsername());
    return out;
  }

  @Override
  public void run() {
    this.logger.info("Running...");
    int counter = 0;
    boolean authenticated = false;
    try {
      while (!this.isInterrupted() && !authenticated) {
        this.logger.info("login attempt: " + counter);
        String json = reader.readLine();
        this.logger.info(json);
        if (json.contains("DBlogin")) {
          DBlogin dBlogin = modelReader.fromJson(json, DBlogin.class);
          authenticated = checkUser(dBlogin);
        } else {
          this.logger.info("not a login request");
        }
        this.logger.info("authenticated = " + authenticated);
        counter++;
      }
    } catch (IOException | InvalidBytes e) {
      try {
        this.logger.info("Shit broke");
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
        this.logger.info(json);
        if (json.contains("LobbyPlayers")) {
          LobbyPlayers lobbyPlayers = modelReader.fromJson(json, LobbyPlayers.class);
          HashMap<String, Player> players = dBinterface.getLobby(lobbyPlayers.getNames());
          lobbyPlayers.setPlayers(players);
          printer.println(modelReader.toJson(lobbyPlayers));
        } else if (json.contains("Leaderboard")) {
          Leaderboard leaderboard = new Leaderboard(new ArrayList<>(dBinterface.getLeaderboard()));
          printer.println(modelReader.toJson(leaderboard));
        }
      } catch (IOException | InvalidBytes e) {
        e.printStackTrace();
      }
    }
  }
}
