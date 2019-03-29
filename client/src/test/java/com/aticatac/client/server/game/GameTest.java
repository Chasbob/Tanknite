package com.aticatac.client.server.game;

import com.aticatac.client.server.Position;
import com.aticatac.client.server.game.BaseGame;
import com.aticatac.client.server.objectsystem.entities.Bullet;
import com.aticatac.client.server.objectsystem.entities.Tank;
import com.aticatac.common.objectsystem.EntityType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameTest {
  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  BaseGame game = new BaseGame();

  @Test
  public void checkCreateTank() {
    assertNotNull(game.createTank("tank", false, 0, 0));
  }
}
