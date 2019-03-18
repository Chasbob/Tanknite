package com.aticatac.server.test;

import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.server.objectsystem.entities.Bullet;
import com.aticatac.server.objectsystem.entities.Tank;

public class Survival extends GameMode implements Runnable {
  private volatile boolean run;

  public Survival() throws InvalidClassInstance, ComponentExistsException {
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
    run = true;
    this.logger.trace("Running...");
    while (run) {
      this.logger.trace("tick");
      double nanoTime = System.nanoTime();
      bullets.parallelStream().forEach(Bullet::tick);
//      for (int i = 0; i < bullets.size(); i++) {
//        Output output = bullets.get(i).tick();
//      }
      for (Tank t : playerMap.values()) {
//        PlayerOutput output = t.tick();
        t.tick();
//        bullets.addAll(output.getNewBullets());
//        if (output.isHit()) {
//          for (Entity e : output.getTurretOutput().collisions) {
//            if (e.type == Entity.EntityType.TANK) {
//            }
//            this.logger.info(e);
//          }
//        }
//        switch (output.hit.type){
//          case TANK:
//            playerMap.get(output.hit.name).destroy();
//        }
      }
      while (System.nanoTime() - nanoTime < 1000000000 / 60) {
        try {
          Thread.sleep(0);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
