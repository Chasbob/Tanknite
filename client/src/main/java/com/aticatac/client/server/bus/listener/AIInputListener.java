package com.aticatac.client.server.bus.listener;

import com.aticatac.client.server.ai.AIInput;
import com.google.common.eventbus.Subscribe;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AIInputListener {
  private final ConcurrentLinkedQueue<AIInput> frames;
//  private final PlayerState me;

  public AIInputListener(final ConcurrentLinkedQueue<AIInput> frames) {
    this.frames = frames;
//    me = new PlayerState(p, health);
  }

  @Subscribe
  private void aiInput(AIInput input) {
    AIInput i = new AIInput(30, input.getPlayers(), input.getPowerups());
    frames.add(i);
  }
}
