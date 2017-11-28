package com.emotibot.xychatlib.resultprocessors;

import com.emotibot.xychatlib.controllers.XYlibChatController;
import com.emotibot.xychatlib.utils.XYlibChatMessageUtils;

/**
 * Created by Administrator on 2017\11\28 0028.
 */

public class XYlibResponseProcessor extends XYlibBaseProcessor {
    @Override
    public void process(XYlibChatController controller, String type, String cmd, String value,
                        String content){
        controller.showRobotSay(content, XYlibChatMessageUtils.RESPONSE);
    }
}
