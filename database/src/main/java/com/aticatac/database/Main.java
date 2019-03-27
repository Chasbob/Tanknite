package com.aticatac.database;

import com.aticatac.database.mappers.Player;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

public class Main {
  private static Logger logger = Logger.getLogger(Main.class.getName());
  private static Server server;

  public static void main(String[] args) {
    logger.info("Start");
    server = new Server();
    server.run();
//    while (true) {
//      server.run();
//    }
  }

  private static void addPlayer(Player p) {
    SqlSession session = SessionFactory.getSession();
    try {
      session.insert("Player.insert", p);
      session.commit();
      session.close();
    } catch (Exception e) {
      logger.error(e);
    }
  }
}
