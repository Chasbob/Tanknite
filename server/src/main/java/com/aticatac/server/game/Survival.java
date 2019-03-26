package com.aticatac.server.game;

import com.aticatac.common.model.CommandModel;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.server.bus.service.AIUpdateService;
import com.aticatac.server.objectsystem.interfaces.Tickable;
import com.google.common.collect.Streams;

public class Survival extends GameMode implements Runnable {
  private final AIUpdateService aiUpdateService;
  private volatile boolean run;
  private int counter;

  public Survival() {
    aiUpdateService = new AIUpdateService();
  }

  @Override
  public void initialise() {
  }

  @Override
  public Update getFrame() {
    return frame;
  }

  @Override
  public void playerInput(final CommandModel model) {
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
      //createPowerUps();
      this.logger.trace("tick");
      double nanoTime = System.nanoTime();
      aiUpdateService.update(playerMap, powerups);
      Streams.concat(bullets.stream(), playerMap.values().stream()).forEach(Tickable::tick);
//      playerMap.values().parallelStream().forEach(Tank::tick);
//      Streams.concat(bullets.stream(), playerMap.values().stream()).forEach(Tickable::tick);
//      if(playerMap.containsKey("asd")){
//        playerMap.get("asd").move(1000,1920);
//      }
      while (System.nanoTime() - nanoTime < 1000000000 / 1000) {
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
    if (counter == 200000000) {
      createPowerUps();
      counter = 0;
    }
  }
}
