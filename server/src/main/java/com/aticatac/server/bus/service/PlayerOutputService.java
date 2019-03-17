package com.aticatac.server.bus.service;

import com.aticatac.server.bus.EventBusFactory;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.IO.outputs.PlayerOutput;
import com.aticatac.server.objectsystem.entities.Bullet;
import com.google.common.eventbus.EventBus;
import java.util.ArrayList;

/**
 * The type Player output service.
 */
public class PlayerOutputService {
  private final PlayerOutput output;
  private final EventBus bus;

  /**
   * Instantiates a new Player output service.
   */
  public PlayerOutputService() {
    output = new PlayerOutput();
    bus = EventBusFactory.getEventBus();
  }

  public void addBullet(Bullet bullet) {
    output.addBullet(bullet);
  }

  public void setHit(Entity hit) {
    output.setHit(hit);
  }

  /**
   * On player output.
   *
   * @param entity  the entity
   * @param bullets the bullets
   */
  public void onPlayerOutput(Entity entity, ArrayList<Bullet> bullets) {
    output.reset();
    output.addBullets(bullets);
    output.setHit(entity);
    output.finalise();
    bus.post(output);
  }

  public void send() {
    output.finalise();
    bus.post(output);
  }
}
