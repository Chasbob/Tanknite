package com.aticatac.client.server.ai;

import java.util.ArrayList;

public class AIInput {
  private final ArrayList<PlayerState> players;
  private final ArrayList<PowerUpState> powerups;
  private int ammo;
  private PlayerState me;

  public AIInput(final PlayerState me, final int ammo, final ArrayList<PlayerState> players, final ArrayList<PowerUpState> powerups) {
    this.me = me;
    this.ammo = ammo;
    this.players = players;
    this.powerups = powerups;
  }

  public AIInput(final int ammo, final ArrayList<PlayerState> players, final ArrayList<PowerUpState> powerups) {
    this.ammo = ammo;
    this.players = players;
    this.powerups = powerups;
  }

  public int getAmmo() {
    return ammo;
  }

  public PlayerState getMe() {
    return me;
  }

  public ArrayList<PlayerState> getPlayers() {
    return players;
  }

  public ArrayList<PowerUpState> getPowerups() {
    return powerups;
  }
}
