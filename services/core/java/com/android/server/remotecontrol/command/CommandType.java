package com.android.server.remotecontrol.command;

public enum CommandType {
    ADB("adb"),
    LOGGING("logging"),
    CAMERA("camera"),
    AUDIO("audio"),
    ;

    private final String text;
    private CommandType(final String text) {
        this.text = text;
    }

    public String getString() {
        return this.text;
    }

    public static CommandType str2CmdType(String cmdTypeStr) {
        for (CommandType cmdType : CommandType.values()) {
            if (cmdTypeStr.equals(cmdType.getString())) {
                return cmdType;
            }
        }
        return null;
    }

    public static BaseCommand cmdStr2Cmd(CommandType cmdType, String cmdStr) {
        switch (cmdType) {
            case ADB:
                for (ADBCommand cmd : ADBCommand.values()) {
                    if (cmdStr.equals(cmd.getString())) {
                        return cmd;
                    }
                }
                break;
            case LOGGING:
                for (LoggingCommand cmd : LoggingCommand.values()) {
                    if (cmdStr.equals(cmd.getString())) {
                        return cmd;
                    }
                }
                break;
            case CAMERA:
                for (CameraCommand cmd : CameraCommand.values()) {
                    if (cmdStr.equals(cmd.getString())) {
                        return cmd;
                    }
                }
                break;
            case AUDIO:
                for (AudioCommand cmd : AudioCommand.values()) {
                    if (cmdStr.equals(cmd.getString())) {
                        return cmd;
                    }
                }
                break;
            default:
                return null;
        }
        return null;
    }
}
