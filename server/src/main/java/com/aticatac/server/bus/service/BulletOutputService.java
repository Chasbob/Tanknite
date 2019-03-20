package com.aticatac.server.bus.service;

import com.aticatac.server.bus.EventBusFactory;
import com.aticatac.server.bus.event.BulletCollisionEvent;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.entities.Bullet;
import com.google.common.eventbus.EventBus;

public class BulletOutputService {
  private final EventBus bus;
  private final Bullet me;

  public BulletOutputService(final Bullet me) {
    this.me = me;
    this.bus = EventBusFactory.getEventBus();
  }

  public void onBulletCollision(Entity e) {
    bus.post(new BulletCollisionEvent(me, e));
  }
}
