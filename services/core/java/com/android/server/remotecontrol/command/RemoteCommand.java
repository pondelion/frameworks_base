package com.android.server.remotecontrol.command;

public class RemoteCommand {
    CommandType cmdType;
    BaseCommand cmd;

    public RemoteCommand(final CommandType cmdType, final BaseCommand cmd) {
        this.cmdType = cmdType;
        this.cmd = cmd;
    }
}
