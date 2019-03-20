package com.aticatac.server.test;

import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.server.objectsystem.interfaces.Tickable;
import com.google.common.collect.Streams;

public class Survival extends GameMode implements Runnable {
  //  private final AIUpdateService aiUpdateService;
  private volatile boolean run;
  private int counter;

  public Survival() throws InvalidClassInstance, ComponentExistsException {
//    aiUpdateService = new AIUpdateService(playerMap,powerups);
  }

  @Override
  public void initialise() {
  }

  @Override
  public Update getFrame() {
    return frame;
  }

  public void stop() {
    run = false;
  }

  @Override
  public void run() {
    counter = 0;
    run = true;
    this.logger.trace("Running...");
    while (run) {
      //checkPowerup();
      this.logger.trace("tick");
      double nanoTime = System.nanoTime();
//      aiUpdateService.update();
      Streams.concat(bullets.stream(), playerMap.values().stream()).forEach(Tickable::tick);
//      playerMap.values().parallelStream().forEach(Tank::tick);
//      Streams.concat(bullets.stream(), playerMap.values().stream()).forEach(Tickable::tick);
      while (System.nanoTime() - nanoTime < 1000000000 / 60) {
        try {
          Thread.sleep(0);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void checkPowerup() {
    counter++;
    if (counter == 60) {
      createPowerUps();
      counter = 0;
    }
  }
}
