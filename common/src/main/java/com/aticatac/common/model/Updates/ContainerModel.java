package com.aticatac.common.model.Updates;

import com.aticatac.common.model.Model;
import com.aticatac.common.objectsystem.Container;

import java.util.ArrayList;

public class ContainerModel extends Model {
    public ArrayList<Container> Containers;

    /**
     * Instantiates a new Model.
     *
     * @param id the id
     */
    public ContainerModel(String id, ArrayList<Container> containers) {
        super(id);
        Containers = containers;
    }
}
