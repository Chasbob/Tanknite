package com.aticatac.common.mappers;

import java.sql.Timestamp;

/**
 * The type Player.
 */
public class Challenge {
  public Timestamp date;
  public String description;
  public int reward;


  /**
   * Instantiates a new Challenge
   * *
   * @param date             the date
   * @param description      the description
   * @param reward           the reward

   */
  public Challenge(final Timestamp date, final String description, final int reward) {
    this.date = date;
    this.description = description;
    this.reward = reward;
  }

  public Challenge() {
  }

  @Override
  public String toString() {
    return "Challenge{" +
        "date=" + date +
        ", description='" + description +
        ", reward='" + reward +
        '}';
  }
}