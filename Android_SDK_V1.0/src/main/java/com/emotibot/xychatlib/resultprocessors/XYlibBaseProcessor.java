package com.emotibot.xychatlib.resultprocessors;

import java.util.List;

import com.emotibot.xychatlib.controllers.XYlibChatController;

/**
 * Created by ldy on 2017/2/23.
 */

public abstract class XYlibBaseProcessor {
    protected String mType;
    protected String mCmd;
    protected String mValue;
    protected String mJson;

    public void process(XYlibChatController controller, String type, String cmd, String value, String json){
        mType = type;
        mValue = value;
        mCmd = cmd;
        mJson = json;
    }
    public void process(XYlibChatController controller, List<String> answers){}
}
