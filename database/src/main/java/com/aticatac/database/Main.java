package com.aticatac.database;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

public class Main {
  private static Logger log = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) throws Exception {
    log.info("Start");
    log.debug("Debug info");
    try (InputStream in = Main.class.getResourceAsStream("/mybatis-config.xml")) {
      SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
      try (SqlSession session = factory.openSession()) {
        List<Map<String, Object>> result = session.selectList("sample.mybatis.selectTest");
        result.forEach(row -> {
          System.out.println("---------------");
          row.forEach((columnName, value) -> {
            System.out.printf("columnName=%s, value=%s%n", columnName, value);
          });
        });
      }
    }
  }
}
