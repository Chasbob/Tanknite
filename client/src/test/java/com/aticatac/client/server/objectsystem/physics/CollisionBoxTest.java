package com.aticatac.client.server.objectsystem.physics;

import com.aticatac.client.server.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CollisionBoxTest {

  Position p = new Position(0,0);
  CollisionBox box = new CollisionBox(p, 0);
  private HashSet<Position> boxSet = new HashSet<>();

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  public void testGetBoxReturnsBox(){

    assertEquals(boxSet, box.getBox());

  }

  @Test
  public void testGetPositionReturnsPosition(){

    assertEquals(p,box.getPosition());

  }

}