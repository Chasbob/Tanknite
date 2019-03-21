package com.aticatac.server.ai;

import com.aticatac.server.objectsystem.IO.inputs.PlayerInput;
import java.util.ArrayList;

public class AIInput extends PlayerInput {
  private final ArrayList<PlayerState> players;
  private final ArrayList<PowerUpState> powerups;
  public int ammo;
  public PlayerState me;

  public AIInput(final PlayerState me, final int ammo, final ArrayList<PlayerState> players, final ArrayList<PowerUpState> powerups) {
    this.me = me;
    this.ammo = ammo;
    this.players = players;
    this.powerups = powerups;
  }

  public ArrayList<PlayerState> getPlayers() {
    return players;
  }

  public ArrayList<PowerUpState> getPowerups() {
    return powerups;
  }
}
