package com.aticatac.client.server.objectsystem.physics;

import com.aticatac.client.server.Position;
import com.aticatac.client.server.objectsystem.Entity;
import com.aticatac.common.objectsystem.EntityType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;


class PhysicsTest {

  //test a wall position 0,0
  Position p = new Position(0,0);
  Physics testPhysics = new Physics(EntityType.TANK, "test");
  Position newPosition = new Position(5,0);
  HashSet<Entity> collisions = new HashSet<>();
  PhysicsResponse response = new PhysicsResponse(collisions, newPosition);

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  public void testMoveReturnsPhysicsResponse(){

    assertEquals(response, testPhysics.move(90,p,-1 ));

  }

}