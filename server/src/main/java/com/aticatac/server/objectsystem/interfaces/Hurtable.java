package com.aticatac.server.objectsystem.interfaces;

public interface Hurtable extends Collidable {
  int getHealth();

  int hit(int damage);

  int heal(int amount);
}
