package com.android.server.remotecontrol;

import com.android.server.remotecontrol.command.RemoteCommand;

public interface ICommandParser {
    abstract public RemoteCommand parseCommand(String cmdMsg);
}
