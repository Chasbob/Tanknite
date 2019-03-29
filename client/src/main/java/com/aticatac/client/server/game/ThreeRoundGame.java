package com.aticatac.client.server.game;

import com.aticatac.common.GameResult;

/**
 * The type Three round game.
 */
public class ThreeRoundGame extends BaseGame {
  private int round;
  private GameResult results;

  /**
   * Instantiates a new Three round game.
   */
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
        while (System.nanoTime() - nanoTime < 5000000000d) {
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
