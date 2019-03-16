package com.aticatac.server.test;

import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.model.Updates.Update;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.IO.outputs.BulletOutput;
import com.aticatac.server.objectsystem.IO.outputs.PlayerOutput;
import com.aticatac.server.objectsystem.entities.Tank;
import java.util.ArrayList;

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
      ArrayList<BulletOutput> outputs = new ArrayList<>();
      bullets.parallelStream().forEach((bullet -> outputs.add(bullet.tick())));
      for (BulletOutput o : outputs) {
        if (o.hit.type != Entity.EntityType.NONE) {
          this.logger.info(o.hit);
        }
      }
//      for (int i = 0; i < bullets.size(); i++) {
//        Output output = bullets.get(i).tick();
//      }
      for (Tank t : playerMap.values()) {
        PlayerOutput output = t.tick();
        bullets.addAll(output.getNewBullets());
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
