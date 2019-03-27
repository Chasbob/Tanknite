package com.aticatac.common;

import java.util.ArrayList;
import java.util.HashMap;

public class GameResult {
  private ArrayList<String> winners;
  private HashMap<String, KillDeath> kd;

  public GameResult(final String winners, final HashMap<String, KillDeath> kd) {
    this.winners = new ArrayList<>();
    this.winners.add(winners);
    this.kd = kd;
  }

  public GameResult() {
    winners=new ArrayList<>();
    kd = new HashMap<>();
  }

  public ArrayList<String> getWinners() {
    return winners;
  }

  public HashMap<String, KillDeath> getKd() {
    return kd;
  }

  public void add(GameResult newResults) {
    winners.addAll(newResults.winners);
    for (String player : newResults.getKd().keySet()) {
      if (kd.containsKey(player)) {
        kd.get(player).addStats(newResults.getKd().get(player));
      } else {
        kd.put(player, newResults.kd.get(player));
      }
    }
  }
}
