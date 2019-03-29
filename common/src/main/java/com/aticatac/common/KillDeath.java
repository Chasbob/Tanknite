package com.aticatac.common;

public class KillDeath {
  private int kill;
  private int death;

  public KillDeath(final int kill, final int death) {
    this.kill = kill;
    this.death = death;
  }

  public int getKill() {
    return kill;
  }

  public int getDeath() {
    return death;
  }

  public void addKills(final int n) {
    kill += n;
  }

  public void addDeaths(final int n) {
    death += n;
  }

  public void addStats(KillDeath newKD) {
    addStats(newKD.kill, newKD.death);
  }

  @Override
  public String toString() {
    return "KillDeath{" +
        "kill=" + kill +
        ", death=" + death +
        '}';
  }

  public void addStats(final int kills, final int deaths) {
    addDeaths(deaths);
    addKills(kills);
  }
}
