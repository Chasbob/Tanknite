package com.aticatac.common.model.Updates;

import com.aticatac.common.model.Model;

/**
 * The type Update.
 */
public class Update extends Model {
    private final boolean changed;


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
