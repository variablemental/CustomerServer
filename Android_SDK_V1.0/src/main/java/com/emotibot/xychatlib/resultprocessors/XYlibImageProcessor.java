package com.emotibot.xychatlib.resultprocessors;

import android.text.TextUtils;

import com.emotibot.xychatlib.XYlibConfig;
import com.emotibot.xychatlib.constants.URLConstants;
import com.emotibot.xychatlib.controllers.XYlibChatController;
import com.emotibot.xychatlib.factorys.ResultProcessorFactory;

import static com.emotibot.xychatlib.utils.XYlibChatMessageUtils.IMAGE;

/**
 * Created by ldy on 2017/3/6.
 */

public class XYlibImageProcessor extends XYlibBaseProcessor {
    @Override
    public void process(XYlibChatController controller, String type, String cmd, String value, String json){
        if (!TextUtils.isEmpty(cmd) && !ResultProcessorFactory.processorMap.containsKey(cmd)) {
            controller.showRobotSay(XYlibConfig.ERR_UNSUPPORT_FAIL);
            return;
        }

        if (type.equals(URLConstants.TYPE_URL)) {
            if (TextUtils.isEmpty(value.trim())) {
                controller.showRobotSay(XYlibConfig.ERR_REPLY_FAIL);
            } else {
                controller.showRobotSay(value, IMAGE);
            }
        } else {
            controller.showRobotSay(XYlibConfig.ERR_REPLY_FAIL);
        }
    }
}
