package com.android.server.remotecontrol.command;

public enum CameraCommand implements BaseCommand {
    TAKE_PICTURE("take_picture"),
    ;

    private final String text;
    private CameraCommand(final String text) {
        this.text = text;
    }

    public String getString() {
        return this.text;
    }

    @Override
    public BaseCommand str2Cmd(String cmdStr) {
        for (CameraCommand cmd : CameraCommand.values()) {
            if (cmdStr == cmd.getString()) {
                return cmd;
            }
        }
        return null;
    }
}
