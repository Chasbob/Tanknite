package com.aticatac.client.server.objectsystem.IO.outputs;

import com.aticatac.client.server.objectsystem.Entity;
import com.aticatac.client.server.objectsystem.entities.Bullet;
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
   * Gets hit.
   *
   * @return the hit
   */
  public Entity getHit() {
    return hit;
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
   * Sets hit.
   *
   * @param hit the hit
   */
  public void setHit(final Entity hit) {
    this.hit = hit;
  }

  /**
   * Add bullets.
   *
   * @param bullets the bullets
   */
  public void addBullets(ArrayList<Bullet> bullets) {
    newBullets.addAll(bullets);
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

  /**
   * Gets new bullets.
   *
   * @return the new bullets
   */
  public ArrayList<Bullet> getNewBullets() {
    return newBullets;
  }
}
