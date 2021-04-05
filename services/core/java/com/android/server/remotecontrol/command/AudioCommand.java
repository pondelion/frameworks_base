package com.android.server.remotecontrol.command;

public enum AudioCommand implements BaseCommand {
    START_RECORD_AUDIO("start_record_audio"),
    END_RECORD_AUDIO("end_record_audio"),
    ;

    private final String text;
    private AudioCommand(final String text) {
        this.text = text;
    }

    public String getString() {
        return this.text;
    }

    @Override
    public BaseCommand str2Cmd(String cmdStr) {
        for (AudioCommand cmd : AudioCommand.values()) {
            if (cmdStr.equals(cmd.getString())) {
                return cmd;
            }
        }
        return null;
    }
}
