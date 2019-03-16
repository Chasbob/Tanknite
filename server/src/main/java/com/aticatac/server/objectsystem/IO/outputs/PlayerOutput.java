package com.aticatac.server.objectsystem.IO.outputs;

import com.aticatac.server.objectsystem.Entity;
import com.aticatac.server.objectsystem.entities.Bullet;
import java.util.ArrayList;

/**
 * The type Player output.
 */
public class PlayerOutput extends Output {
  private Entity hit;
  private ArrayList<Bullet> newBullets;

  /**
   * Instantiates a new Player output.
   */
  public PlayerOutput() {
    newBullets = new ArrayList<>();
  }

  /**
   * Add bullet.
   *
   * @param b the b
   */
  public void addBullet(Bullet b) {
    newBullets.add(b);
  }

  /**
   * Is hit entity.
   *
   * @return the entity
   */
  public Entity isHit() {
    return hit;
  }

  @Override
  public PlayerOutput finalise() {
    if (hit == null) {
      hit = Entity.empty;
    }
    return this;
  }

  @Override
  public void reset() {
    newBullets.clear();
    hit = Entity.empty;
  }

  public ArrayList<Bullet> getNewBullets() {
    return newBullets;
  }
}
