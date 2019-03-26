package com.aticatac.database;

import com.aticatac.database.mappers.Player;
import java.util.ArrayList;
import java.util.Optional;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import static com.aticatac.database.bus.EventBusFactory.eventBus;

public class DBinterface {
  public DBinterface() {
    eventBus.register(this);
  }

  public Optional<Player> getPlayer(String id) {
    SqlSession session = SessionFactory.getSession();
    Player player = session.selectOne("Player.getByUsername", id);
    session.close();
    if (player != null) {
      return Optional.of(player);
    } else {
      return Optional.empty();
    }
  }

  public Player addPlayer(final Player player) throws PersistenceException {
    SqlSession session = SessionFactory.getSession();
    session.insert("Player.insert", player);
    session.commit();
    session.close();
    Player player1 = new Player();
    session.selectOne("Player.getByUsername", player1);
    return player1;
  }

  public void applyGame(ArrayList<Player> players) {
    SqlSession session = SessionFactory.getSession();
    for (Player p : players) {
      session.insert("Player.updateStats", p);
    }
    session.commit();
    session.close();
  }
}
