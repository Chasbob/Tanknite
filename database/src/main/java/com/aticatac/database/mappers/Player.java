package com.aticatac.database.mappers;

/**
 * The type Player.
 */
public class Player {
  private int id;
  private String username;
  private String password;
  private int tank_colour;
  private int top_kill_streak;
  private int kills;
  private int deaths;
  private int win;
  private int loss;

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
  public Player(final int id, final String username, final String password, final int tank_colour, final int win, final int loss, final int kills, final int deaths, final int top_kill_streak) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.tank_colour = tank_colour;
    this.win = win;
    this.loss = loss;
    this.kills = kills;
    this.deaths = deaths;
    this.top_kill_streak = top_kill_streak;
  }

  public Player() {
  }

  public Player(final String username, final String password) {
    this.username = username;
    this.password = password;
  }

  public void applyKillDeath(KillDeath killDeath) {
    kills += killDeath.getKill();
    deaths += killDeath.getDeath();
  }

  public void addWin() {
    win++;
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * Sets id.
   *
   * @param id the id
   */
  public void setId(final int id) {
    this.id = id;
  }

  /**
   * Gets username.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets username.
   *
   * @param username the username
   */
  public void setUsername(final String username) {
    this.username = username;
  }

  /**
   * Gets password.
   *
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets password.
   *
   * @param password the password
   */
  public void setPassword(final String password) {
    this.password = password;
  }

  /**
   * Gets tank colour.
   *
   * @return the tank colour
   */
  public int getTank_colour() {
    return tank_colour;
  }

  /**
   * Sets tank colour.
   *
   * @param tank_colour the tank colour
   */
  public void setTank_colour(final int tank_colour) {
    this.tank_colour = tank_colour;
  }

  /**
   * Gets win.
   *
   * @return the win
   */
  public int getWin() {
    return win;
  }

  /**
   * Sets win.
   *
   * @param win the win
   */
  public void setWin(final int win) {
    this.win = win;
  }

  /**
   * Gets loss.
   *
   * @return the loss
   */
  public int getLoss() {
    return loss;
  }

  /**
   * Sets loss.
   *
   * @param loss the loss
   */
  public void setLoss(final int loss) {
    this.loss = loss;
  }

  /**
   * Gets kills.
   *
   * @return the kills
   */
  public int getKills() {
    return kills;
  }

  /**
   * Sets kills.
   *
   * @param kills the kills
   */
  public void setKills(final int kills) {
    this.kills = kills;
  }

  /**
   * Gets deaths.
   *
   * @return the deaths
   */
  public int getDeaths() {
    return deaths;
  }

  /**
   * Sets deaths.
   *
   * @param deaths the deaths
   */
  public void setDeaths(final int deaths) {
    this.deaths = deaths;
  }

  /**
   * Gets top kill streak.
   *
   * @return the top kill streak
   */
  public int getTop_kill_streak() {
    return top_kill_streak;
  }

  /**
   * Sets top kill streak.
   *
   * @param top_kill_streak the top kill streak
   */
  public void setTop_kill_streak(final int top_kill_streak) {
    this.top_kill_streak = top_kill_streak;
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
