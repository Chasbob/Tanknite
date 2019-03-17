package com.aticatac.server.bus.listener;

import com.aticatac.server.bus.event.TankCollisionEvent;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.entities.Bullet;
import com.aticatac.server.objectsystem.entities.Tank;
import com.google.common.eventbus.Subscribe;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.apache.log4j.Logger;

public class PlayerCollisionListener {
  private final Logger logger;
  private final ConcurrentHashMap<String, Tank> playerMap;
  private final CopyOnWriteArraySet<Bullet> bullets;

  public PlayerCollisionListener(final ConcurrentHashMap<String, Tank> playerMap, final CopyOnWriteArraySet<Bullet> bullets) {
    this.playerMap = playerMap;
    this.bullets = bullets;
    this.logger = Logger.getLogger(getClass());
  }

  @Subscribe
  private void collision(TankCollisionEvent e) {
    if (e.getHit().type != Entity.EntityType.NONE) {
      this.logger.info(e);
    }
  }
}
