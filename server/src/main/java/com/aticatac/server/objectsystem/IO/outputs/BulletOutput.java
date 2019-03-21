package com.aticatac.server.objectsystem.IO.outputs;

import com.aticatac.server.transform.Position;
import com.aticatac.server.objectsystem.Entity;

public class BulletOutput extends Output {
  public Position position;
  public Entity hit;

  public BulletOutput(final Position position, final Entity hit) {
    this.position = position;
    this.hit = hit;
  }

  public BulletOutput(final Position position) {
    this(position, Entity.empty);
  }

  public BulletOutput() {
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(final Position position) {
    this.position = position;
  }

  public Entity getHit() {
    return hit;
  }

  public void setHit(final Entity hit) {
    this.hit = hit;
  }

  @Override
  public BulletOutput finalise() {
    if (hit == null) {
      hit = Entity.empty;
    }
    if (position == null) {
      position = new Position();
    }
    return this;
  }

  @Override
  public void reset() {
    hit = Entity.empty;
  }
}
