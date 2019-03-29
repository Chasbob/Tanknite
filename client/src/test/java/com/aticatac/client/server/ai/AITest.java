package com.aticatac.client.server.ai;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AITest {

  AI ai = new AI();
  ArrayList<PlayerState> players = new ArrayList<>();
  ArrayList<PowerUpState> powerups = new ArrayList<>();
  AIInput input = new AIInput(0, players, powerups);


  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  public void testDecisionDoesNotReturnNull(){

    assertNotNull(ai.getDecision(input));

  }

}