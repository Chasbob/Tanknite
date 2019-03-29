package com.aticatac.client.server.objectsystem.objecttest;

import com.aticatac.client.server.Position;
import com.aticatac.client.server.game.BaseGame;
import com.aticatac.client.server.objectsystem.entities.Bullet;
import com.aticatac.client.server.objectsystem.entities.Tank;
import com.aticatac.common.objectsystem.EntityType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PositionTest {
  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  Position p = new Position(0,0);

  @Test
  public void checkCoordinates(){
    assert(p.getX()==0 && p.getY()==0);
  }
}
