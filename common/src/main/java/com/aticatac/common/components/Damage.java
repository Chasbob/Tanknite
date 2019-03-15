package com.aticatac.common.components;

import com.aticatac.common.components.Component;
import com.aticatac.common.objectsystem.GameObject;

public class Damage extends Component{
  private int damage = 10;

public Damage(GameObject parent){
  super(parent);
}

public int getDamage() {
  return damage;
}

public void setDamage(int damage) {
  this.damage = damage;
}


}