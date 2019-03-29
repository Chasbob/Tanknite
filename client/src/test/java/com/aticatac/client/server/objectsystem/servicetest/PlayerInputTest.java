package com.aticatac.client.server.objectsystem.servicetest;

import com.aticatac.client.networking.Client;
import com.aticatac.client.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlayerInputTest{

  Client c = new Client();
  GameScreen g = new GameScreen();

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  public void checkClient() {
    assertNotNull(c);
  }

  @Test
  public void checkGameScreen() {
    assertNotNull(g);
  }

}


