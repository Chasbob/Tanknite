package com.aticatac.common.model;

import com.aticatac.common.mappers.Player;
import java.util.ArrayList;
import java.util.HashMap;

public class LobbyPlayers extends Model{
  private ArrayList<String> names;
  private HashMap<String,Player> players;

  public LobbyPlayers(final ArrayList<String> names) {
    this.names = names;
  }

  public HashMap<String,Player> getPlayers() {
    return players;
  }

  public void setPlayers(final HashMap<String,Player> players) {
    this.players = players;
  }

  public ArrayList<String> getNames() {
    return names;
  }
}
