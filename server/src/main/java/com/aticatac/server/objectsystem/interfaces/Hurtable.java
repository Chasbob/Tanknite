package com.aticatac.server.objectsystem.interfaces;

public interface Hurtable extends Collidable {
  int getHealth();

  //int hit(int damage);

  int hit (int damage, boolean freezeBullet);

  int heal(int amount);
}
