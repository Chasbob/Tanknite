package com.aticatac.common.model;

import com.aticatac.common.mappers.Player;

public class DBResponse extends Model{
  private Player player;
  private Response response;

  public DBResponse(Player player, Response response) {
    this.player = player;
    this.response = response;
  }

  public DBResponse(Response response) {
    this.response = response;
  }

  public Player getPlayer() {
    return player;
  }

  public Response getResponse() {
    return response;
  }

  public enum Response {
    accepted, wrong_password, username_taken, no_user
  }
}
