package com.aticatac.common.objectsystem;

import java.util.Optional;

public class Container {
    private final Optional<String> texture;
    private final double x, y, r;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    public Container(double x, double y, double r, Optional<String> texture) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.texture = texture;
    }

    public Container(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.texture = Optional.empty();
    }

    public Optional<String> getTexture() {
        return texture;
    }

    @Override
    public String toString() {
        return "Container{" +
                ", texture=" + texture +
                '}';
    }
}
