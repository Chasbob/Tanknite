package com.aticatac.server.objectsystem.entities;

import com.aticatac.common.model.Command;
import com.aticatac.server.ai.AI;
import com.aticatac.server.ai.AIInput;
import com.aticatac.server.ai.Decision;
import com.aticatac.server.ai.PlayerState;
import com.aticatac.server.bus.EventBusFactory;
import com.aticatac.server.bus.listener.AIInputListener;
import com.aticatac.server.transform.Position;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;

@SuppressWarnings("ALL")
public class AITank extends Tank {
  protected final ConcurrentLinkedQueue<AIInput> frames;
  protected final Logger logger;
  private final AI ai;
  protected AIInput input;

  public AITank(String name, Position p, int health, int ammo) {
    super(name, p, health, ammo);
    frames = new ConcurrentLinkedQueue<>();
    setPosition(p);
    logger = Logger.getLogger(getClass());
    this.setMaxHealth(100);
    this.setHealth(health);
    this.setAmmo(ammo);
    this.setMaxAmmo(30);
    ai = new AI();
    EventBusFactory.getEventBus().register(new AIInputListener(frames));
  }

  @Override
  public void tick() {
    logger.trace("tick");
    if (!frames.isEmpty()) {
      input = frames.poll();
      AIInput i = new AIInput(new PlayerState(getPosition(), getHealth()), 30, input.getPlayers(), input.getPowerups());
      Decision decision = ai.getDecision(i);
      if (decision.getCommand() != null && decision.getCommand() != Command.DEFAULT) {
        this.logger.trace(decision.getCommand());
        try {
          move(decision.getCommand().vector, isSpeedIncrease());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
}
