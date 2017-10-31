package com.emotibot.xychatlib.resultprocessors;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import com.emotibot.xychatlib.XYlibConfig;
import com.emotibot.xychatlib.constants.URLConstants;
import com.emotibot.xychatlib.controllers.XYlibChatController;
import com.emotibot.xychatlib.factorys.ResultProcessorFactory;
import com.emotibot.xychatlib.openapiresult.XYlibResult;
import com.emotibot.xychatlib.openapiresult.items.XYlibNewsItem;
import com.emotibot.xychatlib.openapiresult.items.XYlibResultData;
import com.emotibot.xychatlib.utils.XYlibChatMessageUtils;

/**
 * Created by ldy on 2017/3/6.
 */

public class XYlibNewsProcessor extends XYlibBaseProcessor {
    @Override
    public void process(XYlibChatController controller, String type, String cmd, String value,
                        String json){
        if (!TextUtils.isEmpty(cmd) && !ResultProcessorFactory.processorMap.containsKey(cmd)) {
            controller.showRobotSay(XYlibConfig.ERR_UNSUPPORT_FAIL);
            return;
        }

        if (!type.equals(URLConstants.TYPE_TEXT)) {
            controller.showRobotSay(XYlibConfig.ERR_REPLY_FAIL);
            return;
        }

        if (!cmd.equals(URLConstants.CMD_NEWS)) {
            controller.showRobotSay(XYlibConfig.ERR_REPLY_FAIL);
            return;
        }

        Type jsonType = new TypeToken<XYlibResult<XYlibResultData<XYlibNewsItem>>>() {}.getType();
        XYlibResult<XYlibResultData<XYlibNewsItem>> newsResult = controller.getActivity().getGson()
                                                                        .fromJson(json, jsonType);

        if (newsResult.getData().getItems() == null || newsResult.getData().getItems().size() <= 0){
            controller.showRobotSay(XYlibConfig.ERR_REPLY_FAIL);
            return;
        }

        if (!TextUtils.isEmpty(newsResult.getValue())) {
            controller.showRobotSay(value);
        }

        controller.showRobotSay(json, XYlibChatMessageUtils.NEWS_PAGES);
    }
}
