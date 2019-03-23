package com.aticatac.server.objectsystem.interfaces;

public interface DependantTickable<I> extends Tickable {
  void addFrame(I frame);

  void addAndConsume(I frame);
}
