package com.aticatac.client.server.objectsystem.physics;

import com.aticatac.client.server.Position;
import com.aticatac.client.server.objectsystem.Entity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;


class PhysicsResponseTest {

  Position p = new Position(0,0);
  HashSet<Entity> e = new HashSet<Entity>();
  PhysicsResponse response = new PhysicsResponse(e,p);

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  public void testGetCollisionReturnsHashSet(){

    assertEquals(e, response.getCollisions());
  }

  @Test
  public void testGetPosition(){

    assertEquals(p, response.getPosition());

  }

}