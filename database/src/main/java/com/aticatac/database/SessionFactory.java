package com.aticatac.database;

import java.io.InputStream;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SessionFactory {
  private static final InputStream in = Main.class.getResourceAsStream("/mybatis/mybatis-config.xml");
  private static final SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);

  public static SqlSession getSession() {
    return factory.openSession();
  }
}
