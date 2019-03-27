package com.aticatac.client.server.bus.listener;

import com.aticatac.common.model.Updates.Update;
import org.apache.log4j.Logger;

public class UpdateChangesListener {
  private final Update update;
  private final Logger logger;

  public UpdateChangesListener(final Update update) {
    this.update = update;
    this.logger = Logger.getLogger(getClass());
  }

//  @Subscribe
//  private void playersChanged(PlayersChangedEvent e) {
//    switch (e.action) {
//      case ADD:
//        update.addPlayer(e.getContainer());
//        break;
//      case REMOVE:
//        update.removePlayer(e.getContainer());
//        break;
//      case UPDATE:
//        update.addPlayer(e.getContainer());
//        break;
//    }
//  }
//
//  @Subscribe
//  private void powerupsChanged(PowerupsChangedEvent e) {
//    switch (e.getAction()) {
//      case ADD:
//        update.addPowerup(e.getContainer());
//        break;
//      case REMOVE:
//        update.removePowerup((e.getContainer()));
//        break;
//      case UPDATE:
//        update.addPowerup(e.getContainer());
//        break;
//    }
//  }
//
//  @Subscribe
//  private void bulletsChanged(BulletsChangedEvent e) {
//    switch (e.getAction()) {
//      case ADD:
//        update.addProjectile(e.getBullet());
//        new Thread(() -> {
//          update.addNewShot(e.getBullet());
//          double nanoTime = System.nanoTime();
//          while (System.nanoTime() - nanoTime < 5000000000d) {
//            try {
//              Thread.sleep(0);
//            } catch (InterruptedException er) {
//              er.printStackTrace();
//            }
//          }
//          update.removeNewShot(e.getBullet());
//        }).start();
//        break;
//      case REMOVE:
//        update.removeProjectile(e.getBullet());
//        break;
//      case UPDATE:
//        update.addProjectile(e.getBullet());
//        break;
//    }
//  }
}
