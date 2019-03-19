package com.aticatac.server.bus.listener;

import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.bus.event.BulletCollisionEvent;
import com.aticatac.server.objectsystem.entities.Bullet;
import com.aticatac.server.objectsystem.entities.Tank;
import com.google.common.eventbus.Subscribe;
import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class BulletCollisionListener {
  private final Logger logger;
  private final ConcurrentHashMap<String, Tank> playerMap;
  private final CopyOnWriteArraySet<Bullet> bullets;

  public BulletCollisionListener(final ConcurrentHashMap<String, Tank> playerMap, final CopyOnWriteArraySet<Bullet> bullets) {
    this.playerMap = playerMap;
    this.bullets = bullets;
    this.logger = Logger.getLogger(getClass());
  }

  @Subscribe
  public void bulletCollision(BulletCollisionEvent event) {
    this.logger.info(event);
    if (event.getHit().type == EntityType.TANK) {
      playerMap.get(event.getHit().name).hit(event.getBullet().getDamage());
      this.logger.info(event);
    }
    bullets.remove(event.getBullet());
  }
}
