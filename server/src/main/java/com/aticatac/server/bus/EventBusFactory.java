package com.aticatac.server.bus;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import java.util.concurrent.Executors;

public class EventBusFactory {
  //hold the instance of the bus bus here
  private static EventBus eventBus = new AsyncEventBus(Executors.newCachedThreadPool());

  public static EventBus getEventBus() {
    return eventBus;
  }
}


