package com.aticatac.server.objectsystem.interfaces;

import com.aticatac.server.objectsystem.IO.outputs.Output;

public interface Tickable {
  <O extends Output> O tick();
}
