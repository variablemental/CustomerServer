package com.emotibot.xychatlib.resultprocessors;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import com.emotibot.xychatlib.XYlibConfig;
import com.emotibot.xychatlib.constants.URLConstants;
import com.emotibot.xychatlib.controllers.XYlibChatController;
import com.emotibot.xychatlib.factorys.ResultProcessorFactory;
import com.emotibot.xychatlib.helpers.XYlibTextlinkHelper;
import com.emotibot.xychatlib.openapiresult.XYlibResult;
import com.emotibot.xychatlib.openapiresult.items.XYlibNBAData;
import com.emotibot.xychatlib.utils.XYlibChatMessageUtils;

/**
 * Created by ldy on 2017/3/6.
 */

public class XYlibNBAProcessor extends XYlibBaseProcessor {
    @Override
    public void process(XYlibChatController controller, String type, String cmd, String value,
                        String json){
        if (!TextUtils.isEmpty(cmd) && !ResultProcessorFactory.processorMap.containsKey(cmd)) {
            controller.showRobotSay(XYlibConfig.ERR_UNSUPPORT_FAIL);
            return;
        }

        if (!type.equals(URLConstants.TYPE_TEXT)) {
            return;
        }

        if (!cmd.equals(URLConstants.CMD_NBA)) {
            controller.showRobotSay(XYlibConfig.ERR_REPLY_FAIL);
            return;
        }

        Type jsonType = new TypeToken<XYlibResult<XYlibNBAData>>() {}.getType();
        XYlibResult<XYlibNBAData> result  = controller.getActivity().getGson().fromJson(json,
                                                                                        jsonType);
        XYlibNBAData data = result.getData();

        if (!TextUtils.isEmpty(data.getAnswer())) {
            String answer = data.getAnswer();
            String url = data.getUrl();

            XYlibTextlinkHelper tl = new XYlibTextlinkHelper();
            tl.setLink(url);
            tl.setMsg(answer);
            tl.setClickText("腾讯NBA");
            String json1 = controller.getActivity().getGson().toJson(tl);

            if (!TextUtils.isEmpty(tl.getLink()) && !TextUtils.isEmpty(tl.getMsg())) {
                controller.showRobotSay(json1, XYlibChatMessageUtils.TEXT_LINK);
            } else {
                controller.showRobotSay(tl.getMsg());
            }
        }

        if (data.getMatchlist().size() < 1) {
            return;
        }

        controller.showRobotSay(json, XYlibChatMessageUtils.NBA);
    }
}
