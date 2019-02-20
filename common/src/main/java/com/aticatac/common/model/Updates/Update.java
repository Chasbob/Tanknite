package com.aticatac.common.model.Updates;

import com.aticatac.common.model.Model;
import com.aticatac.common.objectsystem.Container;

import java.util.ArrayList;

/**
 * The type Update.
 */
public class Update extends Model {
    private final boolean changed;
    private ArrayList<Container> obj;

    public ArrayList<Container> getObj() {
        return obj;
    }

    public void setObj(ArrayList<Container> obj) {
        this.obj = obj;
    }

    /**
     * Instantiates a new Model.
     *
     * @param changed the changed
     */
    public Update(boolean changed) {
        super("update");
        this.changed = changed;
    }

    /**
     * Is changed boolean.
     *
     * @return the boolean
     */
    public boolean isChanged() {
        return changed;
    }
}
