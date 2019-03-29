package com.aticatac.client.server.objectsystem.entities;

import com.aticatac.client.server.Position;
import com.aticatac.client.server.objectsystem.entities.Bullet;
import com.aticatac.client.server.objectsystem.entities.Tank;
import com.aticatac.common.objectsystem.EntityType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.aticatac.common.objectsystem.containers.Container;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BulletTest {

  Position p = new Position(0,0);
  Tank t = new Tank ("tank", p, 100, 30, 1);
  Bullet b = new Bullet(t, p, 180, 10, false);
  Container c = new Container(0,0,180, "b", EntityType.BULLET);

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  public void testBulletDamage() {
    assert (b.getDamage() == 10);
  }

  @Test
  public void testGetContainer()  {
    assertEquals(c, b.getContainer());
  }

  @Test
  public void testBearing() {
    assertEquals(180, b.getBearing());
  }
}
