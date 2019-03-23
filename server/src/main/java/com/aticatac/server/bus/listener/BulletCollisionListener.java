package com.aticatac.server.bus.listener;

import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.server.bus.EventBusFactory;
import com.aticatac.server.bus.event.BulletCollisionEvent;
import com.aticatac.server.bus.event.BulletsChangedEvent;
import com.aticatac.server.objectsystem.entities.Bullet;
import com.aticatac.server.objectsystem.entities.Tank;
import com.google.common.eventbus.Subscribe;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.apache.log4j.Logger;

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
    if (event.getHit().getType() == EntityType.TANK) {
      playerMap.get(event.getHit().getName()).hit(event.getBullet().getDamage());
      this.logger.info(event);
    }
    bullets.remove(event.getBullet());
    EventBusFactory.getEventBus().post(new BulletsChangedEvent(BulletsChangedEvent.Action.REMOVE, event.getBullet().getContainer()));
  }
}
