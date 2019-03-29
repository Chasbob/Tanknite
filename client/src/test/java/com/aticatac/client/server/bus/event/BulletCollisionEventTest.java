package com.aticatac.client.server.bus.event;

import com.aticatac.client.server.Position;
import com.aticatac.client.server.objectsystem.Entity;
import com.aticatac.client.server.objectsystem.entities.Bullet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BulletCollisionEventTest {

  Entity e = new Entity();
  Position p = new Position();
  Bullet b = new Bullet(e, p, 0, 0, false);

  BulletCollisionEvent bce = new BulletCollisionEvent(b,e);
  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  public void testGetReturnsBullet(){

    assertEquals(b, bce.getBullet());

  }

  @Test
  public void testGetReturnsHit(){

    assertEquals(e, bce.getHit());

  }

}