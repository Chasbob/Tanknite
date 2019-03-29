package com.aticatac.client.server.objectsystem.interfaces;

/**
 * The interface Hurtable.
 */
public interface Hurtable extends Collidable {
  /**
   * Gets health.
   *
   * @return the health
   */
  int getHealth();

  //int hit(int damage);

  /**
   * Hit int.
   *
   * @param damage       the damage
   * @param freezeBullet the freeze bullet
   * @return the int
   */
  int hit (int damage, boolean freezeBullet);

  /**
   * Heal int.
   *
   * @param amount the amount
   * @return the int
   */
  int heal(int amount);
}
