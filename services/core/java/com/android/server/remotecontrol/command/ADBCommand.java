package com.android.server.remotecontrol.command;

public enum ADBCommand implements BaseCommand {
    ADB_SHELL("adb_shell")
    ;

    private final String text;
    private ADBCommand(final String text) {
        this.text = text;
    }

    public String getString() {
        return this.text;
    }

    @Override
    public BaseCommand str2Cmd(String cmdStr) {
        for (ADBCommand cmd : ADBCommand.values()) {
            if (cmdStr.equals(cmd.getString())) {
                return cmd;
            }
        }
        return null;
    }
}
