package com.android.server.remotecontrol;

import com.android.server.remotecontrol.command.BaseCommand;
import com.android.server.remotecontrol.command.RemoteCommand;
import com.android.server.remotecontrol.command.CommandType;

import org.josn.JSONObject;
import org.json.JSONException;

public final class JsonCommandParser implements ICommandParser {
    @Override
    public RemoteCommand parseCommand(String cmdMsg) {
        try {
            JSONObject jsonObj = new JSONObject(cmdMsg);
            String cmdTypeStr = jsonObj.getString("cmd_type");
            CommandType cmdType = CommandType.str2CmdType(cmdTypeStr);
            String cmdStr = jsonObj.getString("cmd");
            BaseCommand cmd = CommandType.cmdStr2Cmd(cmdType, cmdStr);
            RemoteCommand remoteCmd = new RemoteCommand(cmdType, cmd);
            return remoteCmd;
        } catch (JSONException e) {
            return null;
        }
    }
}