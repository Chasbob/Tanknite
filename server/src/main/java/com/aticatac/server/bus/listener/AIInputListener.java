package com.aticatac.server.bus.listener;

import com.aticatac.server.ai.AIInput;
import com.aticatac.server.ai.PlayerState;
import com.aticatac.server.transform.Position;
import com.google.common.eventbus.Subscribe;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AIInputListener {
  private final ConcurrentLinkedQueue<AIInput> frames;
  private final PlayerState me;

  public AIInputListener(final ConcurrentLinkedQueue<AIInput> frames, final Position p, final int health) {
    this.frames = frames;
    me = new PlayerState(p, health);
  }

  @Subscribe
  private void aiInput(AIInput input) {
    AIInput i = new AIInput(me, 30, input.getPlayers(), input.getPowerups());
    frames.add(i);
  }
}
