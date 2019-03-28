package com.aticatac.client.server.game;

import com.aticatac.common.GameResult;

public class ThreeRoundGame extends BaseGame {
  private int round;
  private GameResult results;

  public ThreeRoundGame() {
    super();
    round = 1;
    results = new GameResult();
  }

  @Override
  public GameResult call() {
    while (round <= 3) {
      try {
        this.logger.trace("Round: " + round);
        results.add(super.call());
        round++;
        double nanoTime = System.nanoTime();
        while (System.nanoTime() - nanoTime < 1000000000/*0d*/) {
          //wait 10 seconds
          try {
            Thread.sleep(0);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return results;
  }
}
