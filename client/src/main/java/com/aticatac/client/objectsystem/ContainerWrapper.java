package com.aticatac.client.objectsystem;

import com.aticatac.common.model.Model;
import com.aticatac.common.objectsystem.Container;

import java.util.ArrayList;

public class ContainerWrapper extends Model {
    ArrayList<Container> containers;

    public ContainerWrapper(ArrayList<Container> containers) {
        super("wrapper");
        this.containers = containers;
    }
}
