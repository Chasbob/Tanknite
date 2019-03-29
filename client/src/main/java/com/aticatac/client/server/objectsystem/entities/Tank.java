package com.aticatac.client.server.objectsystem.entities;

import com.aticatac.client.server.Position;
import com.aticatac.client.server.ai.PlayerState;
import com.aticatac.client.server.bus.event.BulletsChangedEvent;
import com.aticatac.client.server.bus.event.PlayersChangedEvent;
import com.aticatac.client.server.bus.event.ShootEvent;
import com.aticatac.client.server.bus.event.TankCollisionEvent;
import com.aticatac.client.server.objectsystem.DataServer;
import com.aticatac.client.server.objectsystem.Entity;
import com.aticatac.client.server.objectsystem.interfaces.Collidable;
import com.aticatac.client.server.objectsystem.interfaces.DependantTickable;
import com.aticatac.client.server.objectsystem.interfaces.Hurtable;
import com.aticatac.client.server.objectsystem.physics.Physics;
import com.aticatac.client.server.objectsystem.physics.PhysicsResponse;
import com.aticatac.common.model.Command;
import com.aticatac.common.model.CommandModel;
import com.aticatac.common.model.Vector;
import com.aticatac.common.objectsystem.EntityType;
import com.aticatac.common.objectsystem.containers.PlayerContainer;
import com.google.common.eventbus.EventBus;

import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jetbrains.annotations.NotNull;

import static com.aticatac.client.bus.EventBusFactory.serverEventBus;

/**
 * The type Tank.
 */
public class Tank extends Entity implements DependantTickable<CommandModel>, Hurtable, Comparable<Tank> {
  private final ConcurrentLinkedQueue<CommandModel> frames;
  private final Physics physics;
  private CommandModel input;
  private int health;
  private int maxHealth;
  private int maxAmmo;
  private int ammo;
  private int damageIncrease;
  private int speedIncrease;
  private int deathCountdown;
  private int bulletSprays;
  private int freezeBullets;
  private int frozen;
  private int framesToShoot;
  private int killCount;
  private boolean alive;
  private int playerNo;

  /**
   * Instantiates a new Tank.
   *
   * @param name     the name
   * @param p        the p
   * @param health   the health
   * @param ammo     the ammo
   * @param playerNo the player no
   */
  public Tank(String name, Position p, int health, int ammo, int playerNo) {
    super(name, EntityType.TANK, p);
    rotation = (int) (Math.random() * 360);
    this.playerNo = playerNo;
    frames = new ConcurrentLinkedQueue<>();
    setInput(new CommandModel("", Command.DEFAULT));
    this.setMaxHealth(100);
    this.setHealth(health);
    this.setAmmo(ammo);
    this.setMaxAmmo(30);
    physics = new Physics(getType(), name);
    setFramesToShoot(120);
    killCount = 0;
    alive = true;
    damageIncrease = -1;
    speedIncrease = -1;
    deathCountdown = -1;
    bulletSprays = 0;
    freezeBullets = 0;
    frozen = -1;
  }

  /**
   * Gets player no.
   *
   * @return the player no
   */
  public int getPlayerNo() {
    return playerNo;
  }

  /**
   * Is alive boolean.
   *
   * @return the boolean
   */
  public boolean isAlive() {
    return alive;
  }

  @Override
  public void tick() {
    if (!alive) {
      return;
    }
    logger.trace("tick");
    frozen--;
    damageIncrease--;
    speedIncrease--;
    deathCountdown--;
    if (deathCountdown == 0) {
      hit(10, false);
    }
    if (getFrames().size() > 5) {
      getFrames().clear();
    }
    if (!getFrames().isEmpty()) {
      setInput(getFrames().poll());
      setRotation(getInput().getBearing());
      try {
        if (getHealth() > 10 && getFrozen() < 0) {
          move(getInput().getVector());
        }
      } catch (Exception e) {
        this.logger.error(e);
        this.logger.error("Error while moving.");
      }
    }
    if (getInput().getCommand() == Command.FREEZE_BULLET) {
      this.logger.trace("freeze bullet");
      if (getFreezeBullets() > 0 && getFrozen() < 0) {
        addBullet(new Bullet(this, turretCalculation(getPosition().copy(), getInput().getBearing()), getInput().getBearing(), 0, true));
        // TODO: Make tank turn blue while it is frozen
        freezeBullets--;
      }
    }
    if (getInput().getCommand() == Command.BULLET_SPRAY) {
      this.logger.trace("bullet spray");
      if (getBulletSprays() > 0 && getFrozen() < 0) {
        for (int i = 0; i < 375; i = i + 15) {
          addBullet(new Bullet(this, getPosition().copy(), getInput().getBearing() + i, 5, false));
        }
        bulletSprays--;
      }
    }
    if (getInput().getCommand() == Command.SHOOT) {
      this.logger.trace("shoot");
      if (!(getAmmo() == 0 || getHealth() == 0) && getFramesToShoot() < 0 && getFrozen() < 0) {
        setAmmo(getAmmo() - 1);
        if (damageIncrease > 0) {
          addBullet(new Bullet(this, turretCalculation(getPosition().copy(), getInput().getBearing()), getInput().getBearing(), 20, false));
        } else {
          addBullet(new Bullet(this, turretCalculation(getPosition().copy(), getInput().getBearing()), getInput().getBearing(), 10, false));
        }
        setFramesToShoot(30);
      }
    }
    if (getFramesToShoot() == 1) {
      this.logger.trace("Ready to fire!");
    }
    setFramesToShoot(getFramesToShoot() - 1);
  }

  /**
   * Turret calculation position.
   *
   * @param tankPosition the tank position
   * @param rotation     the rotation
   * @return position
   */
  Position turretCalculation(Position tankPosition, int rotation) {
    double distance = 50;
    double dX = Math.cos(Math.toRadians(rotation));
    double distanceX = distance * -dX;
    double dY = Math.sin(Math.toRadians(rotation));
    double distanceY = distance * -dY;
    double newX = tankPosition.getX() + distanceX;
    double newY = tankPosition.getY() + distanceY;
    return new Position((int) newX, (int) newY);
  }

  private void checkCollisions(PhysicsResponse response) {
    if (!response.getCollisions().contains(outOfBounds)) {
      for (Entity e :
        response.getCollisions()) {
        if (e.getType() != EntityType.NONE && e.getType() != EntityType.WALL && !e.getName().equals(getName())) {
          this.logger.error(e);
        }
        switch (e.getType()) {
          case NONE:
            break;
          case TANK:
            if (!e.getName().equals(getName())) {
              return;
            }
            break;
          case BULLET:
            break;
          case WALL:
            return;
          case OUTOFBOUNDS:
            return;
          case AMMO_POWERUP:
            onPlayerHit(e);
            serverEventBus.post(new TankCollisionEvent(this, e));
            break;
          case SPEED_POWERUP:
            onPlayerHit(e);
            serverEventBus.post(new TankCollisionEvent(this, e));
            break;
          case HEALTH_POWERUP:
            onPlayerHit(e);
            serverEventBus.post(new TankCollisionEvent(this, e));
            break;
          case DAMAGE_POWERUP:
            onPlayerHit(e);
            serverEventBus.post(new TankCollisionEvent(this, e));
            break;
          case BULLETSPRAY_POWERUP:
            onPlayerHit(e);
            serverEventBus.post(new TankCollisionEvent(this, e));
            break;
          case FREEZEBULLET_POWERUP:
            onPlayerHit(e);
            serverEventBus.post(new TankCollisionEvent(this, e));
            break;
        }
      }
      setPosition(response.getPosition(), true);
    }
  }

  /**
   * Move.
   *
   * @param vector the vector
   */
  void move(Vector vector) {
    if (!vector.equals(Vector.Zero)) {
      var response = getPhysics().move(vector.angle(), getPosition(), speedIncrease);
      checkCollisions(response);
    }
    serverEventBus.post(new PlayersChangedEvent(PlayersChangedEvent.Action.UPDATE, getPlayerContainer()));
  }

  /**
   * Gets ammo.
   *
   * @return the ammo
   */
  public int getAmmo() {
    return ammo;
  }

  /**
   * Sets ammo.
   *
   * @param ammo the ammo
   */
  public void setAmmo(int ammo) {
    this.ammo = ammo;
  }

  @Override
  public int getHealth() {
    return health;
  }

  @Override
  public int hit(final int damage, boolean freezeBullet) {
    this.logger.trace(clamp(getHealth() - damage));
    setHealth(clamp(getHealth() - damage));
    if (freezeBullet) {
      frozen = 300;
    }
    if (getHealth() <= 10 && getHealth() > 0) {
      deathCountdown = 1200;
    } else if (getHealth() <= 0) {
      serverEventBus.post(new PlayersChangedEvent(PlayersChangedEvent.Action.REMOVE, getPlayerContainer()));
      DataServer.INSTANCE.removeBoxFromData(getCollisionBox());
      alive = false;
//      Server.ServerData.INSTANCE.getGame().removePlayer(this.getName());
    }
    return getHealth();
  }

  @Override
  public int heal(final int amount) {
    setHealth(clamp(getHealth() + amount));
    return getHealth();
  }

  /**
   * Sets health.
   *
   * @param health the health
   */
  public void setHealth(int health) {
    this.health = health;
  }

  /**
   * Ammo increase.
   *
   * @param amount the amount
   */
  public void ammoIncrease(final int amount) {
    if (getAmmo() + amount >= maxAmmo) {
      setAmmo(getMaxAmmo());
    } else {
      setAmmo(getAmmo() + amount);
    }
  }

  private int clamp(int value) {
    if (value < 0) {
      return 0;
    }
    if (value > getMaxHealth()) {
      return getMaxHealth();
    }
    return value;
  }

  /**
   * Gets player container.
   *
   * @return the player container
   */
  public PlayerContainer getPlayerContainer() {
    return new PlayerContainer(this.getPosition().getX(),
      this.getPosition().getY(),
      this.getRotation(),
      this.getName(),
      EntityType.TANK, this.health, this.ammo, this.alive);
  }

  @Override
  public boolean intersects(final Collidable collidable) {
    HashSet<Position> other = collidable.getCollisionBox().getBox();
    other.addAll(getCollisionBox().getBox());
    return other.size() != (new HashSet<>(other).size());
  }
//  public void move(int x, int y) {
//    setPosition(new Position(x, y), true);
//    eventBus.post(new PlayersChangedEvent(PlayersChangedEvent.Action.UPDATE, getPlayerContainer()));
//  }

  @Override
  public void addToData() {
    DataServer.INSTANCE.addBoxToData(getCollisionBox(), this);
  }

  /**
   * Gets player state.
   *
   * @return the player state
   */
  public PlayerState getPlayerState() {
    return new PlayerState(getPosition(), getHealth());
  }

  @Override
  public void addFrame(final CommandModel frame) {
    this.getFrames().add(frame);
  }

  @Override
  public void addAndConsume(final CommandModel frame) {
//    this.frames.add(frame);
//    this.tick();
  }

  /**
   * Add bullet.
   *
   * @param bullet the bullet
   */
  public void addBullet(Bullet bullet) {
    serverEventBus.post(new ShootEvent(this, bullet));
    serverEventBus.post(new BulletsChangedEvent(BulletsChangedEvent.Action.ADD, bullet.getContainer()));
  }

  /**
   * On player hit.
   *
   * @param entity the entity
   */
  public void onPlayerHit(Entity entity) {
    serverEventBus.post(new TankCollisionEvent(this, entity));
  }

  /**
   * Gets frames.
   *
   * @return the frames
   */
  public ConcurrentLinkedQueue<CommandModel> getFrames() {
    return frames;
  }

  /**
   * Gets physics.
   *
   * @return the physics
   */
  public Physics getPhysics() {
    return physics;
  }


  /**
   * Gets input.
   *
   * @return the input
   */
  public CommandModel getInput() {
    return input;
  }

  /**
   * Sets input.
   *
   * @param input the input
   */
  public void setInput(CommandModel input) {
    this.input = input;
  }

  /**
   * Gets max health.
   *
   * @return the max health
   */
  public int getMaxHealth() {
    return maxHealth;
  }

  /**
   * Sets max health.
   *
   * @param maxHealth the max health
   */
  public void setMaxHealth(int maxHealth) {
    this.maxHealth = maxHealth;
  }

  /**
   * Gets max ammo.
   *
   * @return the max ammo
   */
  public int getMaxAmmo() {
    return maxAmmo;
  }

  /**
   * Sets max ammo.
   *
   * @param maxAmmo the max ammo
   */
  public void setMaxAmmo(int maxAmmo) {
    this.maxAmmo = maxAmmo;
  }

  /**
   * Gets frames to shoot.
   *
   * @return the frames to shoot
   */
  public int getFramesToShoot() {
    return framesToShoot;
  }

  /**
   * Sets frames to shoot.
   *
   * @param framesToShoot the frames to shoot
   */
  public void setFramesToShoot(int framesToShoot) {
    this.framesToShoot = framesToShoot;
  }

  /**
   * Gets speed increase.
   *
   * @return the speed increase
   */
  public int getSpeedIncrease() {
    return speedIncrease;
  }

  /**
   * Sets speed increase.
   *
   * @param value the value
   */
  public void setSpeedIncrease(int value) {
    speedIncrease = value;
  }

  /**
   * Gets damage increase.
   *
   * @return the damage increase
   */
  public int getDamageIncrease() {
    return damageIncrease;
  }

  /**
   * Sets damage increase.
   *
   * @param value the value
   */
  public void setDamageIncrease(int value) {
    damageIncrease = value;
  }

  /**
   * Gets bullet sprays.
   *
   * @return the bullet sprays
   */
  public int getBulletSprays() {
    return bulletSprays;
  }

  /**
   * Sets bullet sprays.
   *
   * @param value the value
   */
  public void setBulletSprays(int value) {
    bulletSprays = value;
  }

  /**
   * Gets frozen.
   *
   * @return the frozen
   */
  public int getFrozen() {
    return frozen;
  }

  /**
   * Sets frozen.
   *
   * @param value the value
   */
  public void setFrozen(int value) {
    frozen = value;
  }

  /**
   * Gets freeze bullets.
   *
   * @return the freeze bullets
   */
  public int getFreezeBullets() {
    return freezeBullets;
  }

  /**
   * Sets freeze bullets.
   *
   * @param value the value
   */
  public void setFreezeBullets(int value) {
    freezeBullets = value;
  }

  /**
   * Gets death countdown.
   *
   * @return the death countdown
   */
  public int getDeathCountdown() {
    return deathCountdown;
  }

  /**
   * Sets death countdown.
   *
   * @param value the value
   */
  public void setDeathCountdown(int value) {
    deathCountdown = value;
  }

  @Override
  public int compareTo(@NotNull final Tank o) {
    return this.playerNo - o.playerNo;
  }

  /**
   * Add kill.
   */
  public void addKill() {
    killCount++;
  }

  /**
   * Gets kill count.
   *
   * @return the kill count
   */
  public int getKillCount() {
    return killCount;
  }

  /**
   * Deactivate.
   */
  public void deactivate() {
    alive = false;
  }

  /**
   * Reset.
   *
   * @param position the position
   */
  public void reset(Position position) {

    setPosition(position);
    frames.clear();
    setInput(new CommandModel("", Command.DEFAULT));
    this.setMaxHealth(100);
    this.setHealth(health);
    this.setAmmo(ammo);
    this.setMaxAmmo(30);
    setFramesToShoot(120);
    killCount = 0;
    alive = true;
    damageIncrease = -1;
    speedIncrease = -1;
    deathCountdown = -1;
    bulletSprays = 0;
    freezeBullets = 0;
    frozen = -1;
  }
}