package com.aticatac.client.server.objectsystem.entities;

import com.aticatac.client.server.Position;
import com.aticatac.client.server.ai.PlayerState;
import com.aticatac.client.server.bus.event.BulletsChangedEvent;
import com.aticatac.client.server.bus.event.PlayersChangedEvent;
import com.aticatac.client.server.bus.event.ShootEvent;
import com.aticatac.client.server.bus.event.TankCollisionEvent;
import com.aticatac.client.server.networking.Server;
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
import com.aticatac.common.objectsystem.Container;
import com.aticatac.common.objectsystem.EntityType;
import com.google.common.eventbus.EventBus;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.aticatac.client.server.bus.EventBusFactory.eventBus;
/**
 * The type Tank.
 */
public class Tank extends Entity implements DependantTickable<CommandModel>, Hurtable {
  private final ConcurrentLinkedQueue<CommandModel> frames;
  private final Physics physics;
  private CommandModel input;
  private int health;
  private int maxHealth;
  private int maxAmmo;
  private int ammo;
  private int damageIncrease = -1;
  private int speedIncrease = -1;
  private int deathCountdown = -1;
  private int bulletSprays = 0;
  private int freezeBullets = 10;
  protected int frozen = -1;
  private int framesToShoot;

  /**
   * Instantiates a new Tank.
   *
   * @param name   the name
   * @param p      the p
   * @param health the health
   * @param ammo   the ammo
   */
  public Tank(String name, Position p, int health, int ammo) {
    super(name, EntityType.TANK, p);
    frames = new ConcurrentLinkedQueue<>();
    setInput(new CommandModel("", Command.DEFAULT));
    this.setMaxHealth(100);
    this.setHealth(health);
    this.setAmmo(ammo);
    this.setMaxAmmo(30);
    physics = new Physics(getPosition(), getType(), name);
    setFramesToShoot(120);
  }

  @Override
  public void tick() {
    logger.trace("tick");
    frozen--;
    damageIncrease--;
    speedIncrease--;
    deathCountdown--;
    if(deathCountdown == 0){
      hit(10,false);
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
        addBullet(new Bullet(this, getPosition().copy(), getInput().getBearing(), 0, true));
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
          addBullet(new Bullet(this, getPosition().copy(), getInput().getBearing(), 20, false));
        } else {
          addBullet(new Bullet(this, getPosition().copy(), getInput().getBearing(), 10, false));
        }
        setFramesToShoot(30);
      }
    }
    if (getFramesToShoot() == 1) {
      this.logger.trace("Ready to fire!");
    }
    setFramesToShoot(getFramesToShoot() - 1);
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
            eventBus.post(new TankCollisionEvent(this, e));
            break;
          case SPEED_POWERUP:
            onPlayerHit(e);
            eventBus.post(new TankCollisionEvent(this, e));
            break;
          case HEALTH_POWERUP:
            onPlayerHit(e);
            eventBus.post(new TankCollisionEvent(this, e));
            break;
          case DAMAGE_POWERUP:
            onPlayerHit(e);
            eventBus.post(new TankCollisionEvent(this, e));
            break;
          case BULLETSPRAY_POWERUP:
            onPlayerHit(e);
            eventBus.post(new TankCollisionEvent(this, e));
            break;
          case FREEZEBULLET_POWERUP:
            onPlayerHit(e);
            eventBus.post(new TankCollisionEvent(this, e));
            break;
        }
      }
      setPosition(response.getPosition(), true);
    }
  }

  /**
   * Move.
   *
   * @param vector        the vector
   */
  void move(Vector vector) {
    if (!vector.equals(Vector.Zero)) {
      var response = getPhysics().move(vector.angle(), getPosition(), speedIncrease);
      checkCollisions(response);
    }
    eventBus.post(new PlayersChangedEvent(PlayersChangedEvent.Action.UPDATE, getContainer()));
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
    if (freezeBullet){
      frozen = 300;
    }
    if (getHealth() <= 10 && getHealth() > 0) {
      deathCountdown = 1200;
    } else if (getHealth() <= 0) {
      Server.ServerData.INSTANCE.getGame().removePlayer(this.getName());
    }
    setHealth(clamp(getHealth() - damage));
    return getHealth();
  }

  @Override
  public int heal(final int amount) {
    setHealth(clamp(getHealth() + amount));
    return getHealth();
  }

  public void ammoIncrease(final int amount){
    if (getAmmo() + amount >= maxAmmo){
      setAmmo(getMaxAmmo());
    }
    else setAmmo(getAmmo() + amount);
  }

  /**
   * Sets health.
   *
   * @param health the health
   */
  public void setHealth(int health) {
    this.health = health;
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

  public Container getContainer() {
    return new Container(this.getPosition().getX(),
        this.getPosition().getY(),
        this.getRotation(),
        this.getHealth(),
        this.getAmmo(),
        this.getName(),
        EntityType.TANK);
  }

  @Override
  public boolean intersects(final Collidable collidable) {
    HashSet<Position> other = collidable.getCollisionBox().getBox();
    other.addAll(getCollisionBox().getBox());
    return other.size() != (new HashSet<>(other).size());
  }
//  public void move(int x, int y) {
//    setPosition(new Position(x, y), true);
//    eventBus.post(new PlayersChangedEvent(PlayersChangedEvent.Action.UPDATE, getContainer()));
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
    getBus().post(new ShootEvent(this, bullet));
    getBus().post(new BulletsChangedEvent(BulletsChangedEvent.Action.ADD, bullet.getContainer()));
//    output.addBullet(bullet);
  }

  /**
   * On player hit.
   *
   * @param entity the entity
   */
  public void onPlayerHit(Entity entity) {
//    getBus().post(new PlayersChangedEvent(PlayersChangedEvent.Action.UPDATE, getContainer()));
    getBus().post(new TankCollisionEvent(this, entity));
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
   * Gets bus.
   *
   * @return the bus
   */
  public EventBus getBus() {
    return eventBus;
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

  public void setSpeedIncrease(int value){
    speedIncrease = value;

  }
  public void setDamageIncrease(int value){
    damageIncrease = value;
  }

  public int getSpeedIncrease(){
    return speedIncrease;
  }

  public int getDamageIncrease(){
    return damageIncrease;
  }

  public void setBulletSprays(int value){
    bulletSprays = value;
  }

  public int getBulletSprays(){
    return bulletSprays;
  }

  public int getFrozen() {
    return frozen;
  }

  public int getFreezeBullets() {
    return freezeBullets;
  }

  public void setFrozen(int value){
    frozen = value;
  }

  public void setFreezeBullets(int value){
    freezeBullets = value;
  }

}