package com.aticatac.common.model;

import com.aticatac.common.mappers.Player;
import java.util.ArrayList;

public class Leaderboard extends Model{
  private ArrayList<Player> leaderboard;

  public ArrayList<Player> getLeaderboard() {
    return leaderboard;
  }

  public Leaderboard() {
  }

  @Override
  public String toString() {
    return "Leaderboard{" +
        "leaderboard=" + leaderboard +
        '}';
  }

  public Leaderboard(final ArrayList<Player> leaderboard) {
    this.leaderboard = leaderboard;
  }
}
