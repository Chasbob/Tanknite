package com.aticatac.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import static com.aticatac.database.bus.EventBusFactory.eventBus;

public class Server implements Runnable {
  private final Logger logger;
  private List<Runnable> runnables;
  private Listener listener;

  public Server() {
    eventBus.register(this);
    logger = Logger.getLogger(getClass());
    runnables = new ArrayList<>();
    try {
      listener = new Listener();
      runnables.add(listener);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    while (true) {
      runnables.parallelStream().forEach(Runnable::run);
    }
  }
}
