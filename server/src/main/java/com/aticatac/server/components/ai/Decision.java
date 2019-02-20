package com.aticatac.server.components.ai;

import com.aticatac.common.model.Command;

public class Decision {
    private final Command command;
    private final int angleChange;

    public Decision(Command command, int angleChange) {
        this.command = command;
        this.angleChange = angleChange;
    }

    public Command getCommand() {
        return command;
    }

    public int getAngleChange() {
        return angleChange;
    }
}
