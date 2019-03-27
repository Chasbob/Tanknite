package com.aticatac.database;

import com.aticatac.common.mappers.Player;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

public class DBinterface {
  public Optional<Player> getPlayer(String id) {
    stats(id);
    SqlSession session = SessionFactory.getSession();
    Player player = session.selectOne("Player.getByUsername", id);
    session.close();
    if (player != null) {
      return Optional.of(player);
    } else {
      return Optional.empty();
    }
  }

  public void stats(String id) {
    SqlSession session = SessionFactory.getSession();
    Player player = session.selectOne("Player.getByUsername", id);
    session.update("Player.updateStats", player);
    SessionFactory.returnSession(session);
  }

  public List<Player> getLeaderboard() {
    SqlSession session = SessionFactory.getSession();
    List<Player> leaderboard = session.selectList("Player.getLeaderboard");
    SessionFactory.returnSession(session);
    return leaderboard;
  }

  public Player addPlayer(final Player player) throws PersistenceException {
    SqlSession session = SessionFactory.getSession();
    session.insert("Player.insert", player);
    session.commit();
    Player player1 = new Player();
    session.selectOne("Player.getByUsername", player1);
    SessionFactory.returnSession(session);
    return player1;
  }
}
