package com.aticatac.server.bus.service;

import com.aticatac.common.objectsystem.Container;
import com.aticatac.server.bus.EventBusFactory;
import com.aticatac.server.bus.event.PlayersChangedEvent;
import com.aticatac.server.bus.event.ShootEvent;
import com.aticatac.server.bus.event.TankCollisionEvent;
import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.entities.Bullet;
import com.google.common.eventbus.EventBus;

/**
 * The type Player output service.
 */
public class PlayerOutputService {
  private final EventBus bus;
  private final Entity me;

  /**
   * Instantiates a new Player output service.
   *
   * @param me the player running this service
   */
  public PlayerOutputService(final Entity me) {
    this.me = me;
    bus = EventBusFactory.getEventBus();
  }

  /**
   * Add bullet.
   *
   * @param bullet the bullet
   */
  public void addBullet(Bullet bullet) {
    bus.post(new ShootEvent(me, bullet));
//    output.addBullet(bullet);
  }

  public void onPlayerHit(Entity entity, final Container container) {
    bus.post(new PlayersChangedEvent(PlayersChangedEvent.Action.UPDATE, container));
    bus.post(new TankCollisionEvent(me, entity));
  }
}
