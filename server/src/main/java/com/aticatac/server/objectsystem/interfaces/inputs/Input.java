package com.aticatac.server.objectsystem.interfaces.inputs;

public interface Input<T extends Input<T>> {
  T set(T t);
}
