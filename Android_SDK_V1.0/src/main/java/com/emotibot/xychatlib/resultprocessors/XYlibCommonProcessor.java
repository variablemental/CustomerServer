package com.emotibot.xychatlib.resultprocessors;

import android.text.TextUtils;

import com.emotibot.xychatlib.XYlibConfig;
import com.emotibot.xychatlib.constants.URLConstants;
import com.emotibot.xychatlib.controllers.XYlibChatController;
import com.emotibot.xychatlib.factorys.ResultProcessorFactory;

/**
 * Created by ldy on 2017/2/24.
 */

public class XYlibCommonProcessor extends XYlibBaseProcessor {

    @Override
    public void process(XYlibChatController controller, String type, String cmd, String value,
                        String json){
        if (!TextUtils.isEmpty(cmd) && !ResultProcessorFactory.processorMap.containsKey(cmd)) {
            controller.showRobotSay(XYlibConfig.ERR_UNSUPPORT_FAIL);
            return;
        }

        if (type.equals(URLConstants.TYPE_TEXT)) {
            if (TextUtils.isEmpty(value.trim())) {
                value = XYlibConfig.ERR_REPLY_FAIL;
            }

            controller.showRobotSay(value);
        } else {
            controller.showRobotSay(XYlibConfig.ERR_REPLY_FAIL);
        }
    }
}
