package com.aticatac.common.mappers;

/**
 * The type Player.
 */
public class Player {
  public int id;
  public String username;
  public String password;
  public int tank_colour;
  public int top_kill_streak;
  public int kills;
  public int deaths;
  public int win;
  public int loss;
  public int score;
  public int xp;

  /**
   * Instantiates a new Player.
   *
   * @param id              the id
   * @param username        the username
   * @param password        the password
   * @param tank_colour     the tank colour
   * @param win             the win
   * @param loss            the loss
   * @param kills           the kills
   * @param deaths          the deaths
   * @param top_kill_streak the top kill streak
   */
  public Player(final int id, final String username, final String password, final int tank_colour, final int win, final int loss, final int kills, final int deaths, final int top_kill_streak, final int score, final int xp) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.tank_colour = tank_colour;
    this.win = win;
    this.loss = loss;
    this.kills = kills;
    this.deaths = deaths;
    this.top_kill_streak = top_kill_streak;
    this.score = score;
    this.xp = xp;
  }

  public Player() {
  }

  public Player(final String username, final String password) {
    this.username = username;
    this.password = password;
  }

  @Override
  public String toString() {
    return "Player{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", tank_colour=" + tank_colour +
        ", top_kill_streak=" + top_kill_streak +
        ", kills=" + kills +
        ", deaths=" + deaths +
        ", win=" + win +
        ", loss=" + loss +
        '}';
  }
}
