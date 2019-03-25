package com.aticatac.server.bus.service;

import com.aticatac.server.bus.EventBusFactory;
import com.aticatac.server.objectsystem.Entity;
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


}
