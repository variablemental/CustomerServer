package com.example.coder_z.customserver;

import com.emotibot.xychatlib.controllers.XYlibChatController;

import java.util.List;

/**
 * Created by ldy on 2017/2/23.
 */

public abstract class BaseProcessor {
    protected String mType;
    protected String mCmd;
    protected String mValue;
    protected String mJson;

    public void process(ChatController controller, String type, String cmd, String value, String json){
        mType = type;
        mValue = value;
        mCmd = cmd;
        mJson = json;
    }
    public void process(ChatController controller, List<String> answers){}
}
