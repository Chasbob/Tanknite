//package com.aticatac.client.server.ai;
//
//import com.aticatac.common.objectsystem.GameObject;
//import com.aticatac.common.objectsystem.ObjectType;
//import com.aticatac.client.server.networking.Server;
//import java.util.concurrent.ConcurrentHashMap;
//import org.apache.log4j.Logger;
//
///**
// * The type Run ai.
// */
//public class RunAI implements Runnable {
//  private final Logger logger;
//  private final ConcurrentHashMap<String, GameObject> players;
//  private boolean run;
//
//  /**
//   * Instantiates a new Run ai.
//   */
//  public RunAI() {
//    run = true;
//    logger = Logger.getLogger(getClass());
////    this.players = Server.ServerData.INSTANCE.getGame().getRoot().findObject(ObjectType.PLAYER_CONTAINER).getChildren();
//  }
//
//  /**
//   * Stop.
//   */
//  public void stop() {
//    this.run = false;
//  }
//
//  @Override
//  public void run() {
//    while (!Thread.currentThread().isInterrupted() && run) {
//      double nanoTime = System.nanoTime();
////      for (GameObject g : Server.ServerData.INSTANCE.getGame().getAI()) {
////        Server.ServerData.INSTANCE.getGame().playerInput(g.getName(), g.getComponent(AI.class).getDecision().getCommand());
////      }
//      while (System.nanoTime() - nanoTime < 1000000000 / 60) {
//        try {
//          Thread.sleep(0);
//        } catch (InterruptedException e) {
//          e.printStackTrace();
//        }
//      }
//    }
//  }
//}
