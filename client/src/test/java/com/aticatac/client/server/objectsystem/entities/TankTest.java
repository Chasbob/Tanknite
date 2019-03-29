package com.aticatac.client.server.objectsystem.entities;

import com.aticatac.client.server.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TankTest {

  Position p = new Position (0,0);
  Tank t = new Tank ("tank", p, 100, 30, 1);



  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  public void testHit(){
    t.hit(10, false);
    assert (t.getHealth() == 90);

  }

  @Test
  public void testHeal(){
    t.hit(10, false);
    t.heal(50);
    assert (t.getHealth() == t.getMaxHealth());
  }

  @Test
  public void testAmmo(){
    t.setAmmo(20);
    assert (t.getAmmo() == 20);
  }



}