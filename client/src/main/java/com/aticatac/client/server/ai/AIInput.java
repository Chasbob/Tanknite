package com.aticatac.client.server.ai;

import java.util.ArrayList;

/**
 * The type Ai input.
 */
public class AIInput {
  private final ArrayList<PlayerState> players;
  private final ArrayList<PowerUpState> powerups;
  private int ammo;
  private int freezeAmmo;
  private int sprayAmmo;
  private PlayerState me;

  /**
   * Instantiates a new Ai input.
   *
   * @param me         the me
   * @param ammo       the ammo
   * @param freezeAmmo the freeze ammo
   * @param sprayAmmo  the spray ammo
   * @param players    the players
   * @param powerups   the powerups
   */
  public AIInput(final PlayerState me, final int ammo, final int freezeAmmo, final int sprayAmmo, final ArrayList<PlayerState> players, final ArrayList<PowerUpState> powerups) {
    this.me = me;
    this.ammo = ammo;
    this.freezeAmmo = freezeAmmo;
    this.sprayAmmo = sprayAmmo;
    this.players = players;
    this.powerups = powerups;
  }

  /**
   * Instantiates a new Ai input.
   *
   * @param ammo     the ammo
   * @param players  the players
   * @param powerups the powerups
   */
  public AIInput(final int ammo, final ArrayList<PlayerState> players, final ArrayList<PowerUpState> powerups) {
    this.ammo = ammo;
    this.players = players;
    this.powerups = powerups;
  }

  /**
   * Gets ammo.
   *
   * @return the ammo
   */
  public int getAmmo() {
    return ammo;
  }

  /**
   * Gets freeze ammo.
   *
   * @return the freeze ammo
   */
  public int getFreezeAmmo() {
    return freezeAmmo;
  }

  /**
   * Gets spray ammo.
   *
   * @return the spray ammo
   */
  public int getSprayAmmo() {
    return sprayAmmo;
  }

  /**
   * Gets me.
   *
   * @return the me
   */
  public PlayerState getMe() {
    return me;
  }

  /**
   * Gets players.
   *
   * @return the players
   */
  public ArrayList<PlayerState> getPlayers() {
    return players;
  }

  /**
   * Gets powerups.
   *
   * @return the powerups
   */
  public ArrayList<PowerUpState> getPowerups() {
    return powerups;
  }
}
