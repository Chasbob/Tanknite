package com.aticatac.server.prefabs;

import com.aticatac.common.components.Texture;
import com.aticatac.common.components.transform.Position;
import com.aticatac.common.components.transform.Transform;
import com.aticatac.common.exceptions.ComponentExistsException;
import com.aticatac.common.exceptions.InvalidClassInstance;
import com.aticatac.common.objectsystem.GameObject;
import com.aticatac.common.objectsystem.ObjectType;
import com.aticatac.server.components.*;
import com.aticatac.server.components.ai.AI;
import com.aticatac.server.components.controller.TankController;
import com.aticatac.server.components.controller.TurretController;


public class TankObject extends GameObject {
  //TODO add in the parameter changes everywhere
  public TankObject(GameObject parent, String name, Position p, int health, int ammo, boolean isAI) throws InvalidClassInstance, ComponentExistsException {
    super(name, parent, ObjectType.TANK);
    this.getComponent(Transform.class).setPosition(p.getX(), p.getY());
    new GameObject("TankBottom", this);
    new GameObject("TankTop", this);
    this.getChildren().get("TankTop").getComponent(Transform.class).setPosition(-14, 0);
    this.getChildren().get("TankBottom").addComponent(Texture.class).setTexture("img/tank.png");
    this.getChildren().get("TankTop").addComponent(Texture.class).setTexture("img/top.png");
    this.addComponent(Health.class).setHealth(health);
    this.addComponent(Ammo.class).setAmmo(ammo);
    this.addComponent(Physics.class);
    this.addComponent(Time.class);
    this.addComponent(Acceleration.class);
    this.addComponent(BulletDamage.class);
    this.addComponent(TankController.class);
    this.addComponent(TurretController.class); // Have TurretController in TankObject rather than a TurretObject?
    if (isAI) {
      this.addComponent(AI.class);
    }
  }
}
