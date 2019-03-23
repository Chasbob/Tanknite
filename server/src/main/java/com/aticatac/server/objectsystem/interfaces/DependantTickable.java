package com.aticatac.server.objectsystem.interfaces;

import com.aticatac.server.objectsystem.interfaces.inputs.Input;

public interface DependantTickable<I> extends Tickable {
  void addFrame(I frame);

  void addAndConsume(I frame);
}
