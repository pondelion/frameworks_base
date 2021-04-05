package com.android.server.remotecontrol.command;

public enum LoggingCommand implements BaseCommand {
    START_LOGGING("start_logging"),
    STOP_LOGGING("stop_logging"),
    ;

    private final String text;
    private LoggingCommand(final String text) {
        this.text = text;
    }

    public String getString() {
        return this.text;
    }

    @Override
    public BaseCommand str2Cmd(String cmdStr) {
        for (LoggingCommand cmd : LoggingCommand.values()) {
            if (cmdStr.equals(cmd.getString())) {
                return cmd;
            }
        }
        return null;
    }
}
