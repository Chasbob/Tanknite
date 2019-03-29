package com.aticatac.client.bus;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import java.util.concurrent.Executors;

/**
 * The type Event bus factory.
 */
public class EventBusFactory {
  /**
   * The constant eventBus.
   */
//hold the instance of the bus bus here
  public static EventBus eventBus = new AsyncEventBus(Executors.newCachedThreadPool());
  /**
   * The constant serverEventBus.
   */
  public static EventBus serverEventBus = new AsyncEventBus(Executors.newCachedThreadPool());
}


