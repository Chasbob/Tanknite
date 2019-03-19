package com.aticatac.server.bus.listener;

import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.bus.event.ShootEvent;
import com.aticatac.server.bus.event.TankCollisionEvent;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.entities.Bullet;
import com.google.common.eventbus.Subscribe;
import java.util.concurrent.CopyOnWriteArraySet;
import org.apache.log4j.Logger;

public class PlayerOutputListener {
  private final CopyOnWriteArraySet<Bullet> bullets;
  private final Logger logger;

  public PlayerOutputListener(final CopyOnWriteArraySet<Bullet> bullets) {
    this.bullets = bullets;
    logger = Logger.getLogger(getClass());
  }

  @Subscribe
  public void processPlayerCollision(TankCollisionEvent tankCollisionEvent) {
    if (tankCollisionEvent.getHit().type != EntityType.NONE) {
      logger.info(tankCollisionEvent);
    }
  }

  @Subscribe
  public void processPlayerOutput(ShootEvent output) {
    bullets.add(output.getBullet());
  }
}
