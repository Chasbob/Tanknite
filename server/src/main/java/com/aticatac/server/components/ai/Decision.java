package com.aticatac.server.components.ai;

import com.aticatac.common.model.Command;

public class Decision {
    private final Command command;
    private final int angle;

    public Decision(Command command, int angle) {
        this.command = command;
        this.angle = angle;
    }

    public Command getCommand() {
        return command;
    }

    public int getAngle() {
        return angle;
    }
}
