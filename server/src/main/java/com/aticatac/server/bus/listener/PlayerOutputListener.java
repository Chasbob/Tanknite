package com.aticatac.server.bus.listener;

import com.aticatac.server.bus.EventBusFactory;
import com.aticatac.server.bus.event.PowerupsChangedEvent;
import com.aticatac.server.bus.event.ShootEvent;
import com.aticatac.server.bus.event.TankCollisionEvent;
import com.aticatac.server.objectsystem.DataServer;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.entities.Bullet;
import com.google.common.eventbus.Subscribe;
import java.util.concurrent.CopyOnWriteArraySet;
import org.apache.log4j.Logger;

public class PlayerOutputListener {
  private final CopyOnWriteArraySet<Bullet> bullets;
  private final CopyOnWriteArraySet<Entity> powerups;
  private final Logger logger;

  public PlayerOutputListener(final CopyOnWriteArraySet<Bullet> bullets, final CopyOnWriteArraySet<Entity> powerups) {
    this.bullets = bullets;
    logger = Logger.getLogger(getClass());
    this.powerups = powerups;
  }

  @Subscribe
  public void processPlayerCollision(TankCollisionEvent e) {
    switch (e.getHit().type) {
      case NONE:
        break;
      case TANK:
        break;
      case BULLET:
        break;
      case WALL:
        break;
      case OUTOFBOUNDS:
        break;
      case AMMO_POWERUP:
      case SPEED_POWERUP:
      case HEALTH_POWERUP:
      case DAMAGE_POWERUP:
        this.logger.info(e);
        DataServer.INSTANCE.removeBoxFromData(e.getHit().getCollisionBox());
        EventBusFactory.getEventBus().post(new PowerupsChangedEvent(PowerupsChangedEvent.Action.REMOVE, e.getHit().getContainer()));
        powerups.remove(e.getHit());
        break;
    }
  }

  //  @Subscribe
//  public void processPlayerCollision(TankCollisionEvent tankCollisionEvent) {
//    if (tankCollisionEvent.getHit().type != EntityType.NONE) {
//      if (tankCollisionEvent.getHit().type.isPowerUp()) {
//        for (Entity p : powerups) {
////          if (p.equals(tankCollisionEvent.getHit())) {
//          if (p.equals(tankCollisionEvent.getHit())) {
//            logger.info(tankCollisionEvent);
//            DataServer.INSTANCE.removeBoxFromData(new CollisionBox(p.getPosition(), p.type));
//            powerups.remove(p);
//          }
//        }
//      }
//    }
//  }
  @Subscribe
  public void processPlayerOutput(ShootEvent output) {
    bullets.add(output.getBullet());
  }
}
