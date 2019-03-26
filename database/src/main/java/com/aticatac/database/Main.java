package com.aticatac.database;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

public class Main {
  private static Logger logger = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) {
    logger.info("Start");
    logger.debug("Debug info");
    addPlayer(new Player(1, "1", "!", 1, 1, 1, 1, 1, 1));
    addPlayer(new Player(1123, "111", "!", 1, 1, 1, 1, 1, 1));
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
